package nccc.btp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetActualAllocationBatchRequest;
import nccc.btp.entity.NcccApportionmentRuleD;
import nccc.btp.entity.NcccApportionmentRuleM;
import nccc.btp.entity.NcccBudgetActual;
import nccc.btp.entity.SyncOU;
import nccc.btp.repository.*;
import nccc.btp.repository.NcccAllocationD1Repository.OperItemRatio;
import nccc.btp.service.BudgetActualAllocationBatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetActualAllocationBatchServiceImpl implements BudgetActualAllocationBatchService {

	private final SyncOURepository syncOURepo;
	private final NcccAccountingListRepository accListRepo;
	private final BudgetActualRepository actualRepo;
	private final NcccAllocationDRepository allocDRepo;
	private final NcccAllocationD1Repository allocD1Repo;
	private final NcccProvisionExpenseMRepository provMRepo;
	private final NcccProvisionExpenseD1Repository provD1Repo;
	private final NcccBudgetDRepository budgetDRepo;
	private final NcccAssetsRepository assetsRepo;
	private final NcccAssetsDRepository assetsDRepo;
	private final BpmBtMD1Repository btD1Repo;
	private final BpmRevMDRepository revDRepo;
	private final BpmRevMDDRepository revDDRepo;
	private final BpmExMD1Repository exD1Repo;
	private final BpmSplitMRepository splitRepo;
	private final BpmTaskItemRepository taskRepo;

	private final NcccApportionmentRuleMRepository ruleMRepo;
	private final NcccApportionmentRuleDRepository ruleDRepo;
	private final NcccOuOperateDRepository ouOpRepo;

	private static final BigDecimal HUNDRED = new BigDecimal("100");
	private static final String BUDGET_VERSION = "2";

	@Override
	@Transactional
	public int run(BudgetActualAllocationBatchRequest req) {
		String yymm = req.getBudgetYm();
		String ver = req.getVersion();
		if (yymm == null || !yymm.matches("^[0-9]{6}$"))
			throw new IllegalArgumentException("budgetYm 需 YYYYMM");
		if (ver == null || !ver.matches("^[0-9A-Za-z]{1,4}$"))
			throw new IllegalArgumentException("version 1-4 碼");

		boolean force = req.getForceClear() != null && req.getForceClear().booleanValue();
		long exists = actualRepo.countByYmVer(yymm, ver);

		if (exists > 0 && !force) {
			throw new IllegalStateException("該年月已經執行報表批次作業，按「確定」清除該年月資料，按「取消」結束程式");
		}
		if (exists > 0 && force) {
			int deleted = actualRepo.deleteByYmVer(yymm, ver);
			log.info("purge NCCC_BUDGET_ACTUAL yymm={}, ver={}, deleted={}", yymm, ver, deleted);
		}

		int total = 0;

		RunCache cache = new RunCache(syncOURepo, accListRepo);

		// 分攤前
		total += accrual(yymm, ver, req.getOperator(), cache, "BEFORE"); //權責分攤
		total += deprAmort(yymm, ver, req.getOperator(), cache, "BEFORE"); //折舊與攤銷
		total += travel(yymm, ver, req.getOperator(), cache, "BEFORE"); //差旅
		total += before_provision(yymm, ver, req.getOperator(), cache); //提列費用
		total += before_acceptance(yymm, ver, req.getOperator(), cache); //驗收
		total += before_expenseApplication(yymm, ver, req.getOperator(), cache); //費用申請

		// 分攤後
		total += accrual(yymm, ver, req.getOperator(), cache, "AFTER"); //權責分攤
		total += deprAmort(yymm, ver, req.getOperator(), cache, "AFTER"); //折舊與攤銷
		total += travel(yymm, ver, req.getOperator(), cache, "AFTER"); //差旅
		total += after_provision(yymm, ver, req.getOperator(), cache); //提列費用
		total += after_acceptance(yymm, ver, req.getOperator(), cache); //驗收
		total += after_expenseApplication(yymm, ver, req.getOperator(), cache); //費用申請
		total += after_rules(yymm, ver, req.getOperator(), cache); // 分攤規則

		return total;
	}

	/** 分攤前後：權責分攤（FINAL_TOTAL * OPERATE_RATIO，差額補最大筆） */
	protected int accrual(String yymm, String ver, String op, RunCache cache, String approation) {
		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION;

		List<NcccAllocationDRepository.AccrualRow> rows = allocDRepo.findRowsByYm(yymm);
		if (rows == null || rows.isEmpty())
			return 0;

		int inserted = 0;

		for (NcccAllocationDRepository.AccrualRow r : rows) {
			String ou = r.getOuCode();
			String ouName = cache.getOuName(ou);
			String acc = r.getAccounting();
			String subject = cache.getSubject(acc);

			BigDecimal totalAmt = nz(r.getFinalTotal());
			if (totalAmt.signum() == 0)
				continue;

			List<OperItemRatio> items = allocD1Repo.findRatiosByPo(r.getPoNo(), r.getPoItemNo());
			if (items == null || items.isEmpty())
				continue;

			List<BigDecimal> allocs = new ArrayList<BigDecimal>(items.size());
			BigDecimal allocatedSum = BigDecimal.ZERO;
			int maxIdx = -1;
			BigDecimal maxAmt = new BigDecimal(Long.MIN_VALUE);

			for (int i = 0; i < items.size(); i++) {
				OperItemRatio it = items.get(i);
				BigDecimal ratio = toRatio(it.getOperateRatio());
				BigDecimal amt = totalAmt.multiply(ratio).setScale(0, java.math.RoundingMode.HALF_UP);
				allocs.add(amt);
				allocatedSum = allocatedSum.add(amt);
				if (amt.compareTo(maxAmt) > 0) {
					maxAmt = amt;
					maxIdx = i;
				}
			}

			BigDecimal diff = totalAmt.subtract(allocatedSum);
			if (diff.signum() != 0 && maxIdx >= 0) {
				allocs.set(maxIdx, allocs.get(maxIdx).add(diff));
			}

			List<NcccBudgetActual> batch = new ArrayList<NcccBudgetActual>(items.size());
			for (int i = 0; i < items.size(); i++) {
				BigDecimal amt = allocs.get(i);
				if (amt == null || amt.signum() == 0)
					continue;

				OperItemRatio it = items.get(i);

				NcccBudgetActual a = new NcccBudgetActual();
				a.setKey(yymm, ver1, approation, ou, acc, it.getOperateItemCode());
				a.setOuName(ouName);
				a.setSubject(subject);
				a.setOperateItem(it.getOperateItem());
				a.setAmount(amt);
				a.setRemark("SAP=" + r.getSapDocNo() + ";PO=" + r.getPoNo() + "-" + r.getPoItemNo());
				a.setCreateUser(createUser);
				a.setCreateDate(new java.util.Date());
				batch.add(a);
			}

			if (!batch.isEmpty()) {
				actualRepo.saveAll(batch);
				inserted += batch.size();
			}
		}
		return inserted;
	}

	/** 分攤前後：折舊與攤銷（WRBTR*100，資產作業項目「百分比」→小數後分攤） */
	protected int deprAmort(String yymm, String ver, String op, RunCache cache, String approation) {

		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION;

		final String yyStr = yymm.substring(0, 4);
		final String mmStr = yymm.substring(4, 6);

		List<BudgetActualRepository.Zfit0006Row> rows = actualRepo.findZfit0006(yyStr, mmStr);
		if (rows == null || rows.isEmpty())
			return 0;

		int inserted = 0;

		for (BudgetActualRepository.Zfit0006Row r : rows) {
			String assetsCode = lz(r.getAnln1());
			String acc = lz(r.getHkont());
			if (assetsCode.isEmpty() || acc.isEmpty())
				continue;

			NcccAssetsRepository.AssetOU asset = assetsRepo.findOuByAssetsCode(assetsCode);
			if (asset == null || asset.getOuCode() == null || asset.getOuCode().trim().isEmpty())
				continue;

			String ou = asset.getOuCode().trim();
			String ouName = cache.getOuName(ou);
			String subject = cache.getSubject(acc);
			BigDecimal totalAmt = nz(r.getWrbtr());
			if (totalAmt.signum() == 0)
				continue;
			totalAmt = totalAmt.multiply(HUNDRED).setScale(0, java.math.RoundingMode.HALF_UP);
			if (totalAmt.signum() == 0)
				continue;

			List<NcccAssetsDRepository.OperItemRatio> items = assetsDRepo.findRatiosByAssets(assetsCode);
			if (items == null || items.isEmpty())
				continue;

			List<BigDecimal> allocs = new ArrayList<BigDecimal>(items.size());
			BigDecimal allocatedSum = BigDecimal.ZERO;
			int maxIdx = -1;
			BigDecimal maxAbs = BigDecimal.ZERO;

			for (int i = 0; i < items.size(); i++) {
				NcccAssetsDRepository.OperItemRatio it = items.get(i);
				BigDecimal ratio = toRatio(it.getOperateRatio());
				BigDecimal amt = BigDecimal.ZERO;
				if (ratio.signum() > 0) {
					amt = totalAmt.multiply(ratio).setScale(0, java.math.RoundingMode.HALF_UP);
				}

				allocs.add(amt);
				allocatedSum = allocatedSum.add(amt);

				BigDecimal curAbs = amt.abs();
				if (curAbs.compareTo(maxAbs) > 0) {
					maxAbs = curAbs;
					maxIdx = i;
				}
			}

			BigDecimal diff = totalAmt.subtract(allocatedSum);
			if (diff.signum() != 0 && maxIdx >= 0) {
				allocs.set(maxIdx, allocs.get(maxIdx).add(diff));
			}

			List<NcccBudgetActual> batch = new ArrayList<NcccBudgetActual>(items.size());
			for (int i = 0; i < items.size(); i++) {
				BigDecimal amt = allocs.get(i);
				if (amt.signum() == 0)
					continue;
				NcccAssetsDRepository.OperItemRatio it = items.get(i);

				NcccBudgetActual a = new NcccBudgetActual();
				a.setKey(yymm, ver1, approation, ou, acc, it.getOperateItemCode());
				a.setOuName(ouName);
				a.setSubject(subject);
				a.setOperateItem(it.getOperateItem());
				a.setAmount(amt);
				a.setRemark("ASSET=" + assetsCode);
				a.setCreateUser(createUser);
				a.setCreateDate(new java.util.Date());
				batch.add(a);
			}

			if (!batch.isEmpty()) {
				actualRepo.saveAll(batch);
				inserted += batch.size();
			}
		}
		return inserted;
	}

	/** 分攤前後：差旅（未稅金額 * 預算作業項目比例，差額補最大筆） */
	protected int travel(String yymm, String ver, String op, RunCache cache, String approation) {
		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION;

		final String yyStr = yymm.substring(0, 4);
		final int yy = Integer.parseInt(yyStr);

		List<BpmBtMD1Repository.TravelRow> rows = btD1Repo.findTravelRows(yymm);
		if (rows == null || rows.isEmpty())
			return 0;

		int inserted = 0;

		for (BpmBtMD1Repository.TravelRow r : rows) {
			String ou = r.getOuCode();
			String ouName = cache.getOuName(ou);
			String acc = r.getAccounting();
			String subject = cache.getSubject(acc);
			List<NcccAccountingListRepository.AccName> accNames = accListRepo
					.findNamesBySubjectIn(Collections.singleton(acc));
			if (accNames != null && !accNames.isEmpty() && accNames.get(0).getName() != null
					&& !accNames.get(0).getName().trim().isEmpty()) {
				subject = accNames.get(0).getName().trim();
			}

			BigDecimal totalAmt = nz(r.getUnTaxAmount());
			if (totalAmt.signum() == 0)
				continue;

			List<NcccBudgetDRepository.BudgetItem> items = budgetDRepo.findBudgetItems(yy, ver1, ou, acc);
			if (items == null || items.isEmpty())
				continue;

			List<BigDecimal> allocs = new ArrayList<BigDecimal>(items.size());
			BigDecimal allocatedSum = BigDecimal.ZERO;
			int maxIdx = -1;
			BigDecimal maxAmt = BigDecimal.valueOf(-1);

			for (int i = 0; i < items.size(); i++) {
				NcccBudgetDRepository.BudgetItem it = items.get(i);
				BigDecimal ratio = toRatio(it.getOperateRatio());
				BigDecimal amt = BigDecimal.ZERO;
				if (ratio.signum() > 0) {
					amt = totalAmt.multiply(ratio).setScale(0, java.math.RoundingMode.HALF_UP);
				}
				allocs.add(amt);
				allocatedSum = allocatedSum.add(amt);
				if (amt.compareTo(maxAmt) > 0) {
					maxAmt = amt;
					maxIdx = i;
				}
			}

			BigDecimal diff = totalAmt.subtract(allocatedSum);
			if (diff.signum() != 0 && maxIdx >= 0) {
				allocs.set(maxIdx, allocs.get(maxIdx).add(diff));
			}

			List<NcccBudgetActual> batch = new ArrayList<NcccBudgetActual>(items.size());
			for (int i = 0; i < items.size(); i++) {
				BigDecimal amt = allocs.get(i);
				if (amt == null || amt.signum() == 0)
					continue;

				NcccBudgetDRepository.BudgetItem it = items.get(i);

				NcccBudgetActual a = new NcccBudgetActual();
				a.setKey(yymm, ver1, approation, ou, acc, it.getOperateItemCode());
				a.setOuName(ouName);
				a.setSubject(subject);
				a.setOperateItem(it.getOperateItem());
				a.setAmount(amt);
				a.setRemark("BT=" + r.getBtNo());
				a.setCreateUser(createUser);
				a.setCreateDate(new java.util.Date());
				batch.add(a);
			}

			if (!batch.isEmpty()) {
				actualRepo.saveAll(batch);
				inserted += batch.size();
			}
		}
		return inserted;
	}

	/** 分攤前：提列費用 */
	protected int before_provision(String yymm, String ver, String op, RunCache cache) {
		final String approation = "BEFORE";
		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION;

		final String yyStr = yymm.substring(0, 4);
		final String mmStr = yymm.substring(4, 6);
		final int yy = Integer.parseInt(yyStr);

		List<NcccProvisionExpenseMRepository.HeaderRow> headers = provMRepo.findHeaders(yyStr, mmStr);
		if (headers == null || headers.isEmpty())
			return 0;

		int inserted = 0;

		for (NcccProvisionExpenseMRepository.HeaderRow h : headers) {
			String ou = h.getOuCode();
			String ouName = cache.getOuName(ou);
			String acc = h.getAccounting();
			String subject = cache.getSubject(acc);
			String remarkPrefix = "PROVISION=" + h.getProvisionNo() + "-" + h.getSeqNo();
			BigDecimal totalAmt = nz(h.getAmount());
			List<NcccBudgetActual> batch = new ArrayList<NcccBudgetActual>();

			String allocMethod = h.getAllocationMethod() == null ? "" : h.getAllocationMethod().trim();
			if ("1".equals(allocMethod)) {
				List<NcccProvisionExpenseD1Repository.OperItemAmt> items = provD1Repo.findItems(h.getProvisionNo(),
						h.getSeqNo());
				if (items != null) {
					for (NcccProvisionExpenseD1Repository.OperItemAmt it : items) {
						BigDecimal amt = nz(it.getOperateAmt());
						if (amt.signum() == 0)
							continue;

						NcccBudgetActual a = new NcccBudgetActual();
						a.setKey(yymm, ver1, approation, ou, acc, it.getOperateItemCode());
						a.setOuName(ouName);
						a.setSubject(subject);
						a.setOperateItem(it.getOperateItem());
						a.setAmount(amt);
						a.setRemark(remarkPrefix);
						a.setCreateUser(createUser);
						a.setCreateDate(new java.util.Date());
						batch.add(a);
					}
				}
			} else {
				List<NcccBudgetDRepository.BudgetItem> items = budgetDRepo.findBudgetItems(yy, ver1, ou, acc);
				if (items == null || items.isEmpty())
					continue;

				BigDecimal base = BigDecimal.ZERO;
				for (NcccBudgetDRepository.BudgetItem i : items) {
					base = base.add(nz(i.getFinalTotal()).multiply(toRatio(i.getOperateRatio())));
				}
				if (base.signum() == 0)
					continue;

				BigDecimal allocatedSum = BigDecimal.ZERO;
				int maxIdx = -1;
				BigDecimal maxAmt = BigDecimal.valueOf(-1);
				List<BigDecimal> allocs = new ArrayList<BigDecimal>(items.size());

				for (int i = 0; i < items.size(); i++) {
					NcccBudgetDRepository.BudgetItem it = items.get(i);
					BigDecimal w = nz(it.getFinalTotal()).multiply(toRatio(it.getOperateRatio()));
					BigDecimal amt = totalAmt.multiply(w).divide(base, 0, java.math.RoundingMode.HALF_UP);
					allocs.add(amt);
					allocatedSum = allocatedSum.add(amt);
					if (amt.compareTo(maxAmt) > 0) {
						maxAmt = amt;
						maxIdx = i;
					}
				}
				BigDecimal diff = totalAmt.subtract(allocatedSum);
				if (diff.signum() != 0 && maxIdx >= 0) {
					allocs.set(maxIdx, allocs.get(maxIdx).add(diff));
				}

				for (int i = 0; i < items.size(); i++) {
					NcccBudgetDRepository.BudgetItem it = items.get(i);
					BigDecimal amt = allocs.get(i);
					if (amt.signum() == 0)
						continue;

					NcccBudgetActual a = new NcccBudgetActual();
					a.setKey(yymm, ver1, approation, ou, acc, it.getOperateItemCode());
					a.setOuName(ouName);
					a.setSubject(subject);
					a.setOperateItem(it.getOperateItem());
					a.setAmount(amt);
					a.setRemark(remarkPrefix);
					a.setCreateUser(createUser);
					a.setCreateDate(new java.util.Date());
					batch.add(a);
				}
			}

			if (!batch.isEmpty()) {
				actualRepo.saveAll(batch);
				inserted += batch.size();
			}
		}
		return inserted;
	}

	/** 分攤後：提列費用 */
	protected int after_provision(String yymm, String ver, String op, RunCache cache) {
		final String approation = "AFTER";
		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION;

		final String yy = yymm.substring(0, 4);
		final String mm = yymm.substring(4, 6);

		// 僅取 FLOW_STATUS='2'、未作廢 的單據
		List<NcccProvisionExpenseMRepository.HeaderRow> rows = provMRepo.findHeaders(yy, mm);
		if (rows == null || rows.isEmpty())
			return 0;

		int inserted = 0;

		for (NcccProvisionExpenseMRepository.HeaderRow r : rows) {
			String srcOu = nvl(r.getOuCode());
			String acc = nvl(r.getAccounting());
			BigDecimal amt = nz(r.getAmount());
			if (srcOu.isEmpty() || acc.isEmpty() || amt.signum() == 0)
				continue;

			String provNo = nvl(r.getProvisionNo());
			String seqNo = nvl(r.getSeqNo());
			String acc4 = acc.length() >= 4 ? acc.substring(0, 4) : acc;

			// 指定作業項目：直接乘 D1 比率 + 補差
			if ("1".equals(nvl(r.getAllocationMethod()))) {
				List<NcccProvisionExpenseD1Repository.D1Row> its = provD1Repo.findItemsRatio(provNo, seqNo);
				if (its == null || its.isEmpty())
					continue;

				List<BigDecimal> allocs = new ArrayList<BigDecimal>(its.size());
				BigDecimal sum = BigDecimal.ZERO;
				int maxIdx = -1;
				BigDecimal maxAbs = BigDecimal.ZERO;

				for (int i = 0; i < its.size(); i++) {
					BigDecimal a = amt.multiply(toRatio(its.get(i).getOperateRatio())).setScale(0,
							java.math.RoundingMode.HALF_UP);
					allocs.add(a);
					sum = sum.add(a);
					if (a.abs().compareTo(maxAbs) > 0) {
						maxAbs = a.abs();
						maxIdx = i;
					}
				}
				BigDecimal diff = amt.subtract(sum);
				if (diff.signum() != 0 && maxIdx >= 0)
					allocs.set(maxIdx, allocs.get(maxIdx).add(diff));

				for (int i = 0; i < its.size(); i++) {
					BigDecimal a = allocs.get(i);
					if (a.signum() == 0)
						continue;
					NcccProvisionExpenseD1Repository.D1Row it = its.get(i);
					inserted += writeActual(yymm, ver1, approation, srcOu, cache.getOuName(srcOu), acc,
							cache.getSubject(acc), nvl(it.getOperateItemCode()), nvl(it.getOperateItem()), a,
							createUser, "PROVISION="+provNo+"-"+seqNo);
				}
				continue;
			}

			// 原比例：先查 OU 分攤規則，命中則先 OU 拆分，再依作業項目比例；否則直接依作業項目比例
			final String accTrim = nvl(acc).trim();

			// M repo 的 findByYMA()
			NcccApportionmentRuleM m = ruleMRepo.findByYMA(yy, mm, accTrim);
			if (m == null)
				m = ruleMRepo.findByYMA(yy, "00", accTrim);

			if (m != null) {
				// D 要用「命中的 M 的年月」查，否則 "00" 會失效
				final String hitYY = nvl(m.getYear());
				final String hitMM = nvl(m.getMonth());
				final String opType = m.getOperationType();

				// 5101 但未設定 1/2 → 跳過此規則
				if (acc4.startsWith("5101") && !("1".equals(opType) || "2".equals(opType))) {
				    log.warn("skip 5101 without OPERATION_TYPE: year={}, month={}, acc={}", hitYY, hitMM, accTrim);
				    continue;
				}
				
				List<NcccApportionmentRuleD> dlist = ruleDRepo.findByYearAndMonthAndAccounting(hitYY, hitMM, accTrim);

				if (dlist == null || dlist.isEmpty()) {
					inserted += splitOperateAndWrite(yymm, yy, ver1, approation, srcOu, accTrim, acc4, amt, opType,
							cache, createUser, "PROVISION="+provNo+"-"+seqNo);
				} else {
					List<BigDecimal> weights = new ArrayList<BigDecimal>(dlist.size());
					for (NcccApportionmentRuleD d : dlist) {
						BigDecimal w = d.getUnitQty() == null ? BigDecimal.ZERO : d.getUnitQty();
						weights.add(w.max(BigDecimal.ZERO));
					}
					List<BigDecimal> ouAmts = allocByWeights(amt, weights);
					for (int i = 0; i < dlist.size(); i++) {
						BigDecimal ouAmt = ouAmts.get(i);
						if (ouAmt.signum() == 0)
							continue;
						String dstOu = nvl(dlist.get(i).getOuCode());
						if (dstOu.isEmpty())
							continue;

						inserted += splitOperateAndWrite(yymm, yy, ver1, approation, dstOu, accTrim, acc4, ouAmt,
								opType, cache, createUser, "PROVISION="+provNo+"-"+seqNo);
					}
				}
			} else {
				inserted += splitOperateAndWrite(yymm, yy, ver1, approation, srcOu, accTrim, acc4, amt, null, cache,
						createUser, "PROVISION="+provNo+"-"+seqNo);
			}
		}

		return inserted;
	}

	/** 分攤前：驗收 */
	protected int before_acceptance(String yymm, String ver, String op, RunCache cache) {
		final String approation = "BEFORE";
		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION;

		List<BpmRevMDRepository.AcceptanceRow> rows = revDRepo.findAcceptanceRows(yymm);
		if (rows == null || rows.isEmpty())
			return 0;

		int inserted = 0;

		for (BpmRevMDRepository.AcceptanceRow r : rows) {
			String acc = nvl(r.getAccounting());
			if (acc.length() < 2 || !"51".equals(acc.substring(0, 2)))
				continue;
			String ou = r.getOuCode();
			String ouName = cache.getOuName(ou);
			String subject = cache.getSubject(acc);
			BigDecimal totalAmt = nz(r.getTotal());
			if (totalAmt.signum() == 0)
				continue;

			String remarkPrefix = "REV=" + nvl(r.getRevNo()) + ";PI=" + nvl(r.getPoItemNo()) + ";RI="
					+ nvl(r.getRevItemNo());
			List<NcccBudgetActual> batch = new ArrayList<NcccBudgetActual>();

			boolean isFixed = "1".equals(nvl(r.getIsFixedAsset()));
			if (isFixed) {
				String fixedAssetNo = r.getFixedAssetNo();
				if (fixedAssetNo == null || fixedAssetNo.trim().isEmpty())
					continue;
				List<NcccAssetsDRepository.OperItemAmt> items = assetsDRepo.findAmtsByAssets(fixedAssetNo.trim());
				if (items == null || items.isEmpty())
					continue;

				for (NcccAssetsDRepository.OperItemAmt it : items) {
					BigDecimal amt = nz(it.getOperateAmt());
					if (amt.signum() == 0)
						continue;

					NcccBudgetActual a = new NcccBudgetActual();
					a.setKey(yymm, ver1, approation, ou, acc, it.getOperateItemCode());
					a.setOuName(ouName);
					a.setSubject(subject);
					a.setOperateItem(it.getOperateItem());
					a.setAmount(amt);
					a.setRemark(remarkPrefix + ";ASSET=" + fixedAssetNo);
					a.setCreateUser(createUser);
					a.setCreateDate(new java.util.Date());
					batch.add(a);
				}
			} else if ("1".equals(nvl(r.getAllocationMethod()))) {
				List<BpmRevMDDRepository.DesignatedItem> items = revDDRepo.findItems(nvl(r.getRevNo()),
						String.valueOf(r.getPoItemNo()), String.valueOf(r.getRevItemNo()));
				if (items != null) {
					for (BpmRevMDDRepository.DesignatedItem it : items) {
						BigDecimal amt = nz(it.getTotal());
						if (amt.signum() == 0)
							continue;

						NcccBudgetActual a = new NcccBudgetActual();
						a.setKey(yymm, ver1, approation, ou, acc, it.getOperateItemCode());
						a.setOuName(ouName);
						a.setSubject(subject);
						a.setOperateItem(it.getOperateItem());
						a.setAmount(amt);
						a.setRemark(remarkPrefix);
						a.setCreateUser(createUser);
						a.setCreateDate(new java.util.Date());
						batch.add(a);
					}
				}
			} else {
				int year = r.getYear() == null ? Integer.parseInt(yymm.substring(0, 4)) : r.getYear().intValue();
				List<NcccBudgetDRepository.BudgetItem> items = budgetDRepo.findBudgetItems(year, ver1, ou, acc);
				if (items == null || items.isEmpty())
					continue;

				List<BigDecimal> allocs = new ArrayList<BigDecimal>(items.size());
				BigDecimal allocatedSum = BigDecimal.ZERO;
				int maxIdx = -1;
				BigDecimal maxAmt = BigDecimal.valueOf(-1);

				for (int i = 0; i < items.size(); i++) {
					NcccBudgetDRepository.BudgetItem it = items.get(i);
					BigDecimal ratio = toRatio(it.getOperateRatio());
					BigDecimal amt = BigDecimal.ZERO;
					if (ratio.signum() > 0) {
						amt = totalAmt.multiply(ratio).setScale(0, java.math.RoundingMode.HALF_UP);
					}
					allocs.add(amt);
					allocatedSum = allocatedSum.add(amt);
					if (amt.compareTo(maxAmt) > 0) {
						maxAmt = amt;
						maxIdx = i;
					}
				}
				BigDecimal diff = totalAmt.subtract(allocatedSum);
				if (diff.signum() != 0 && maxIdx >= 0) {
					allocs.set(maxIdx, allocs.get(maxIdx).add(diff));
				}

				for (int i = 0; i < items.size(); i++) {
					BigDecimal amt = allocs.get(i);
					if (amt == null || amt.signum() == 0)
						continue;

					NcccBudgetDRepository.BudgetItem it = items.get(i);
					NcccBudgetActual a = new NcccBudgetActual();
					a.setKey(yymm, ver1, approation, ou, acc, it.getOperateItemCode());
					a.setOuName(ouName);
					a.setSubject(subject);
					a.setOperateItem(it.getOperateItem());
					a.setAmount(amt);
					a.setRemark(remarkPrefix);
					a.setCreateUser(createUser);
					a.setCreateDate(new java.util.Date());
					batch.add(a);
				}
			}

			if (!batch.isEmpty()) {
				actualRepo.saveAll(batch);
				inserted += batch.size();
			}
		}
		return inserted;
	}

	/** 分攤後：驗收 */
	protected int after_acceptance(String yymm, String ver, String op, RunCache cache) {
		final String approation = "AFTER";
		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION; // 固定 "2"

		final String yy = yymm.substring(0, 4);
		final String mm = yymm.substring(4, 6);

		// 只取：主檔 FLOW_STATUS='2'、POSTING_DATE 月份=輸入年月、且排除權責分攤單
		// 明細再過濾：IS_RESPONSIBILITY <> 'Y'、ACCOUNTING 前2碼=51、TOTAL != 0
		List<BpmRevMDRepository.AcceptanceRow> rows = revDRepo.findAcceptanceRowsAfter(yymm);
		if (rows == null || rows.isEmpty())
			return 0;

		int inserted = 0;

		for (BpmRevMDRepository.AcceptanceRow r : rows) {
			String acc = nvl(r.getAccounting());
			if (acc.length() < 2 || !"51".equals(acc.substring(0, 2)))
				continue;
			if ("Y".equalsIgnoreCase(nvl(r.getIsResponsibility())))
				continue;

			String ou = nvl(r.getOuCode());
			if (ou.isEmpty())
				continue;

			int budgetYear = (r.getYear() != null) ? r.getYear().intValue() : Integer.parseInt(yy);
			String by = String.valueOf(budgetYear); // budget year

			BigDecimal totalAmt = nz(r.getTotal());
			if (totalAmt.signum() == 0)
				continue;

			String remarkPrefix = "REV=" + nvl(r.getRevNo()) + ";PI=" + nvl(r.getPoItemNo()) + ";RI="
					+ nvl(r.getRevItemNo());

			String acc4 = acc.length() >= 4 ? acc.substring(0, 4) : acc;

			// 固定資產：直接取資產作業項目明細金額
			boolean isFixed = "1".equals(nvl(r.getIsFixedAsset()));
			if (isFixed) {
				String fixedAssetNo = nvl(r.getFixedAssetNo());
				if (fixedAssetNo.isEmpty())
					continue;

				List<NcccAssetsDRepository.OperItemAmt> items = assetsDRepo.findAmtsByAssets(fixedAssetNo);
				if (items == null || items.isEmpty())
					continue;

				List<NcccBudgetActual> batch = new ArrayList<NcccBudgetActual>();
				String ouName = cache.getOuName(ou);
				String subject = cache.getSubject(acc);

				for (NcccAssetsDRepository.OperItemAmt it : items) {
					BigDecimal amt = nz(it.getOperateAmt());
					if (amt.signum() == 0)
						continue;

					NcccBudgetActual a = new NcccBudgetActual();
					a.setKey(yymm, ver1, approation, ou, acc, nvl(it.getOperateItemCode()));
					a.setOuName(ouName);
					a.setSubject(subject);
					a.setOperateItem(nvl(it.getOperateItem()));
					a.setAmount(amt);
					a.setRemark(remarkPrefix + ";ASSET=" + fixedAssetNo);
					a.setCreateUser(createUser);
					a.setCreateDate(new java.util.Date());
					batch.add(a);
				}
				if (!batch.isEmpty()) {
					actualRepo.saveAll(batch);
					inserted += batch.size();
				}
				continue;
			}

			// 一般驗收：依 ALLOCATION_METHOD 分支
			String allocMethod = nvl(r.getAllocationMethod());

			// 1 = 指定作業項目：直寫 D_D 的各作業項目金額
			if ("1".equals(allocMethod)) {
				List<BpmRevMDDRepository.DesignatedItem> items = revDDRepo.findItems(r.getRevNo(), r.getPoItemNo(),
						r.getRevItemNo());
				if (items == null || items.isEmpty())
					continue;

				List<NcccBudgetActual> batch = new ArrayList<NcccBudgetActual>();
				String ouName = cache.getOuName(ou);
				String subject = cache.getSubject(acc);

				for (BpmRevMDDRepository.DesignatedItem it : items) {
					BigDecimal amt = nz(it.getTotal());
					if (amt.signum() == 0)
						continue;

					NcccBudgetActual a = new NcccBudgetActual();
					a.setKey(yymm, ver1, approation, ou, acc, nvl(it.getOperateItemCode()));
					a.setOuName(ouName);
					a.setSubject(subject);
					a.setOperateItem(nvl(it.getOperateItem()));
					a.setAmount(amt);
					a.setRemark(remarkPrefix);
					a.setCreateUser(createUser);
					a.setCreateDate(new java.util.Date());
					batch.add(a);
				}
				if (!batch.isEmpty()) {
					actualRepo.saveAll(batch);
					inserted += batch.size();
				}
				continue;
			}

			// 2 = 原比例：先查 OU 分攤規則（命中則先 OU 分攤，再按作業項目規則；未命中則同分攤前）
			// 優先用當月，無則用 "00"
			NcccApportionmentRuleM m = ruleMRepo.findByYMA(by, mm, acc);
			if (m == null)
				m = ruleMRepo.findByYMA(by, "00", acc);

			if (m != null) {
				String hitYY = nvl(m.getYear());
				String hitMM = nvl(m.getMonth());
				String opType = nvl(m.getOperationType());

				// 5101 但未設定 1/2 → 跳過此規則
				if (acc4.startsWith("5101") && !("1".equals(opType) || "2".equals(opType))) {
				    log.warn("skip 5101 without OPERATION_TYPE: year={}, month={}, acc={}", hitYY, hitMM, acc);
				    continue;
				}
				
				List<NcccApportionmentRuleD> dlist = ruleDRepo.findByYearAndMonthAndAccounting(hitYY, hitMM, acc);

				if (dlist == null || dlist.isEmpty()) {
					// 無 D，直接對來源 OU 依作業項目規則拆分
					inserted += splitOperateAndWrite(yymm, by, ver1, approation, ou, acc, acc4, totalAmt, opType, cache,
							createUser, remarkPrefix);
				} else {
					// 依 UNIT_QTY 權重拆 OU，再各自依作業項目規則拆
					List<BigDecimal> weights = new ArrayList<BigDecimal>(dlist.size());
					for (NcccApportionmentRuleD d : dlist) {
						BigDecimal w = d.getUnitQty() == null ? BigDecimal.ZERO : d.getUnitQty();
						weights.add(w.max(BigDecimal.ZERO));
					}
					List<BigDecimal> ouAmts = allocByWeights(totalAmt, weights);

					for (int i = 0; i < dlist.size(); i++) {
						BigDecimal ouAmt = ouAmts.get(i);
						if (ouAmt.signum() == 0)
							continue;
						String dstOu = nvl(dlist.get(i).getOuCode());
						if (dstOu.isEmpty())
							continue;

						inserted += splitOperateAndWrite(yymm, by, ver1, approation, dstOu, acc, acc4, ouAmt, opType,
								cache, createUser, remarkPrefix);
					}
				}
			} else {
				// 無 M，直接同分攤前（不做 OU 分攤），依預算或 5101 規則拆作業項目
				inserted += splitOperateAndWrite(yymm, by, ver1, approation, ou, acc, acc4, totalAmt, null, cache,
						createUser, remarkPrefix);
			}
		}
		return inserted;
	}

	/** 分攤前：費用申請（ACCOUNTING 前2碼=51；FLOW_STATUS=2；POSTING_DATE=輸入年月） */
	protected int before_expenseApplication(String yymm, String ver, String op, RunCache cache) {
		final String approation = "BEFORE";
		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION;

		final String yyStr = yymm.substring(0, 4);
		final String mmStr = yymm.substring(4, 6);
		final int budgetYearDefault = Integer.parseInt(yyStr);

		int inserted = 0;

		// A. 非多重分攤：MULTI_SHARE <> 'Y'（含 NULL/空字串）
		List<BpmExMD1Repository.NonMultiRow> baseRows = exD1Repo.findNonMultiRows(yymm);
		if (baseRows != null && !baseRows.isEmpty()) {
			for (BpmExMD1Repository.NonMultiRow r : baseRows) {
				String ou = nvl(r.getOuCode());
				String acc = nvl(r.getAccounting());
				BigDecimal totalAmt = nz(r.getUnTaxAmount());
				if (ou.isEmpty() || acc.isEmpty() || totalAmt.signum() == 0)
					continue;

				int budgetYear = (r.getYear() == null) ? budgetYearDefault : r.getYear().intValue();
				String acc4 = acc.length() >= 4 ? acc.substring(0, 4) : acc;

				// 先找 RULE_M（當月→00）
				NcccApportionmentRuleM m = ruleMRepo.findByYMA(String.valueOf(budgetYear), mmStr, acc);
				if (m == null)
					m = ruleMRepo.findByYMA(String.valueOf(budgetYear), "00", acc);

				if (m != null) {
					String hitYY = nvl(m.getYear());
					String hitMM = nvl(m.getMonth());
					String opType = nvl(m.getOperationType());

					List<NcccApportionmentRuleD> dlist = ruleDRepo.findByYearAndMonthAndAccounting(hitYY, hitMM, acc);

					if (dlist == null || dlist.isEmpty()) {
						// 有 M 無 D → 對來源 OU 直接按作業項目規則拆
						inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, ou, acc,
								acc4, totalAmt, opType, cache, createUser, "EX="+nvl(r.getExNo()));
					} else {
						// 依 D.UNIT_QTY 權重先拆 OU，再各自按作業項目規則拆
						List<BigDecimal> weights = new ArrayList<BigDecimal>(dlist.size());
						for (NcccApportionmentRuleD d : dlist) {
							BigDecimal w = d.getUnitQty() == null ? BigDecimal.ZERO : d.getUnitQty();
							weights.add(w.max(BigDecimal.ZERO));
						}
						List<BigDecimal> ouAmts = allocByWeights(totalAmt, weights);
						for (int i = 0; i < dlist.size(); i++) {
							BigDecimal ouAmt = ouAmts.get(i);
							if (ouAmt.signum() == 0)
								continue;
							String dstOu = nvl(dlist.get(i).getOuCode());
							if (dstOu.isEmpty())
								continue;

							inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, dstOu,
									acc, acc4, ouAmt, opType, cache, createUser, "EX="+nvl(r.getExNo()));
						}
					}
				} else {
					// 無 M → 同分攤前規則：直接依預算/5101 規則拆作業項目
					inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, ou, acc, acc4,
							totalAmt, null, cache, createUser, "EX="+nvl(r.getExNo()));
				}
			}
		}

		// B. 多重分攤：MULTI_SHARE='Y'
		List<BpmSplitMRepository.SplitRow> splits = splitRepo.findSplitRows(yymm);
		if (splits != null && !splits.isEmpty()) {
			for (BpmSplitMRepository.SplitRow s : splits) {
				String exNo = nvl(s.getExNo());
				String ou = nvl(s.getOuCode());
				String acc = nvl(s.getAccounting());
				BigDecimal totalAmt = nz(s.getUnTaxAmount());
				if (exNo.isEmpty() || ou.isEmpty() || acc.isEmpty() || totalAmt.signum() == 0)
					continue;

				String ouName = cache.getOuName(ou);
				String subject = cache.getSubject(acc);
				String allocMethod = nvl(s.getAllocationMethod());
				int budgetYear = (s.getYear() == null) ? budgetYearDefault : s.getYear().intValue();
				String acc4 = acc.length() >= 4 ? acc.substring(0, 4) : acc;

				// B1. 指定作業項目：BPM_TASK_ITEM 直寫
				if ("1".equals(allocMethod)) {
					String mid = (s.getMId() == null) ? "" : String.valueOf(s.getMId());
					if (mid.isEmpty())
						continue;
					List<BpmTaskItemRepository.TaskItemRow> items = taskRepo.findItems(exNo, mid);
					if (items == null || items.isEmpty())
						continue;

					for (BpmTaskItemRepository.TaskItemRow it : items) {
						BigDecimal a = nz(it.getUnTaxAmount());
						if (a.signum() == 0)
							continue;
						inserted += writeActual(yymm, ver1, approation, ou, ouName, acc, subject,
								nvl(it.getOperateItemCode()),
								nvl(it.getOperateItem()).isEmpty() ? nvl(it.getOperateItemCode())
										: nvl(it.getOperateItem()),
								a, createUser, "EX="+exNo+";EX_ITEM="+mid);
					}
					continue;
				}

				// B2. 原比例：先查 RULE_M → 有則先按 D 拆 OU；無則同分攤前
				NcccApportionmentRuleM m = ruleMRepo.findByYMA(String.valueOf(budgetYear), mmStr, acc);
				if (m == null)
					m = ruleMRepo.findByYMA(String.valueOf(budgetYear), "00", acc);

				if (m != null) {
					String hitYY = nvl(m.getYear());
					String hitMM = nvl(m.getMonth());
					String opType = nvl(m.getOperationType());

					List<NcccApportionmentRuleD> dlist = ruleDRepo.findByYearAndMonthAndAccounting(hitYY, hitMM, acc);

					if (dlist == null || dlist.isEmpty()) {
						inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, ou, acc,
								acc4, totalAmt, opType, cache, createUser, "EX="+exNo);
					} else {
						List<BigDecimal> weights = new ArrayList<BigDecimal>(dlist.size());
						for (NcccApportionmentRuleD d : dlist) {
							BigDecimal w = d.getUnitQty() == null ? BigDecimal.ZERO : d.getUnitQty();
							weights.add(w.max(BigDecimal.ZERO));
						}
						List<BigDecimal> ouAmts = allocByWeights(totalAmt, weights);
						for (int i = 0; i < dlist.size(); i++) {
							BigDecimal ouAmt = ouAmts.get(i);
							if (ouAmt.signum() == 0)
								continue;
							String dstOu = nvl(dlist.get(i).getOuCode());
							if (dstOu.isEmpty())
								continue;

							inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, dstOu,
									acc, acc4, ouAmt, opType, cache, createUser, "EX="+exNo);
						}
					}
				} else {
					inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, ou, acc, acc4,
							totalAmt, null, cache, createUser, "EX="+exNo);
				}
			}
		}

		return inserted;
	}

	/** 分攤後：費用申請（FLOW_STATUS=2；POSTING_DATE=輸入年月；ACCOUNTING 前2碼=51） */
	protected int after_expenseApplication(String yymm, String ver, String op, RunCache cache) {
		final String approation = "AFTER";
		final String createUser = (op == null || op.isEmpty()) ? "SYSTEM" : op;
		final String ver1 = BUDGET_VERSION;

		final String yyStr = yymm.substring(0, 4);
		final String mmStr = yymm.substring(4, 6);
		final int budgetYearDefault = Integer.parseInt(yyStr);

		int inserted = 0;

		// A. 非多重分攤：MULTI_SHARE <> 'Y'（含 NULL/空字串）
		List<BpmExMD1Repository.NonMultiRow> baseRows = exD1Repo.findNonMultiRows(yymm);
		if (baseRows != null && !baseRows.isEmpty()) {
			for (BpmExMD1Repository.NonMultiRow r : baseRows) {
				String ou = nvl(r.getOuCode());
				String acc = nvl(r.getAccounting());
				BigDecimal totalAmt = nz(r.getUnTaxAmount());
				if (ou.isEmpty() || acc.isEmpty() || totalAmt.signum() == 0)
					continue;

				int budgetYear = (r.getYear() == null) ? budgetYearDefault : r.getYear().intValue();
				String acc4 = acc.length() >= 4 ? acc.substring(0, 4) : acc;

				// 先找 RULE_M（當月→00）
				NcccApportionmentRuleM m = ruleMRepo.findByYMA(String.valueOf(budgetYear), mmStr, acc);
				if (m == null)
					m = ruleMRepo.findByYMA(String.valueOf(budgetYear), "00", acc);

				if (m != null) {
					String hitYY = nvl(m.getYear());
					String hitMM = nvl(m.getMonth());
					String opType = nvl(m.getOperationType());

					List<NcccApportionmentRuleD> dlist = ruleDRepo.findByYearAndMonthAndAccounting(hitYY, hitMM, acc);

					if (dlist == null || dlist.isEmpty()) {
						inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, ou, acc,
								acc4, totalAmt, opType, cache, createUser, "EX="+nvl(r.getExNo()));
					} else {
						List<BigDecimal> weights = new ArrayList<BigDecimal>(dlist.size());
						for (NcccApportionmentRuleD d : dlist) {
							BigDecimal w = d.getUnitQty() == null ? BigDecimal.ZERO : d.getUnitQty();
							weights.add(w.max(BigDecimal.ZERO));
						}
						List<BigDecimal> ouAmts = allocByWeights(totalAmt, weights);
						for (int i = 0; i < dlist.size(); i++) {
							BigDecimal ouAmt = ouAmts.get(i);
							if (ouAmt.signum() == 0)
								continue;
							String dstOu = nvl(dlist.get(i).getOuCode());
							if (dstOu.isEmpty())
								continue;

							inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, dstOu,
									acc, acc4, ouAmt, opType, cache, createUser, "EX="+nvl(r.getExNo()));
						}
					}
				} else {
					inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, ou, acc, acc4,
							totalAmt, null, cache, createUser, "EX="+nvl(r.getExNo()));
				}
			}
		}

		// B. 多重分攤：MULTI_SHARE='Y'
		List<BpmSplitMRepository.SplitRow> splits = splitRepo.findSplitRows(yymm);
		if (splits != null && !splits.isEmpty()) {
			for (BpmSplitMRepository.SplitRow s : splits) {
				String exNo = nvl(s.getExNo());
				String ou = nvl(s.getOuCode());
				String acc = nvl(s.getAccounting());
				BigDecimal totalAmt = nz(s.getUnTaxAmount());
				if (exNo.isEmpty() || ou.isEmpty() || acc.isEmpty() || totalAmt.signum() == 0)
					continue;

				String ouName = cache.getOuName(ou);
				String subject = cache.getSubject(acc);
				String allocMethod = nvl(s.getAllocationMethod());
				int budgetYear = (s.getYear() == null) ? budgetYearDefault : s.getYear().intValue();
				String acc4 = acc.length() >= 4 ? acc.substring(0, 4) : acc;

				// B1. 指定作業項目：BPM_TASK_ITEM 直寫
				if ("1".equals(allocMethod)) {
					String mid = (s.getMId() == null) ? "" : String.valueOf(s.getMId());
					if (mid.isEmpty())
						continue;
					List<BpmTaskItemRepository.TaskItemRow> items = taskRepo.findItems(exNo, mid);
					if (items == null || items.isEmpty())
						continue;

					for (BpmTaskItemRepository.TaskItemRow it : items) {
						BigDecimal a = nz(it.getUnTaxAmount());
						if (a.signum() == 0)
							continue;
						inserted += writeActual(yymm, ver1, approation, ou, ouName, acc, subject,
								nvl(it.getOperateItemCode()),
								nvl(it.getOperateItem()).isEmpty() ? nvl(it.getOperateItemCode())
										: nvl(it.getOperateItem()),
								a, createUser, "EX="+exNo+";EX_ITEM="+mid);
					}
					continue;
				}

				// B2. 原比例：先查 RULE_M → 有則先按 D 拆 OU；無則同分攤前
				NcccApportionmentRuleM m = ruleMRepo.findByYMA(String.valueOf(budgetYear), mmStr, acc);
				if (m == null)
					m = ruleMRepo.findByYMA(String.valueOf(budgetYear), "00", acc);

				if (m != null) {
					String hitYY = nvl(m.getYear());
					String hitMM = nvl(m.getMonth());
					String opType = nvl(m.getOperationType());

					List<NcccApportionmentRuleD> dlist = ruleDRepo.findByYearAndMonthAndAccounting(hitYY, hitMM, acc);

					if (dlist == null || dlist.isEmpty()) {
						inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, ou, acc,
								acc4, totalAmt, opType, cache, createUser, "EX="+exNo);
					} else {
						List<BigDecimal> weights = new ArrayList<BigDecimal>(dlist.size());
						for (NcccApportionmentRuleD d : dlist) {
							BigDecimal w = d.getUnitQty() == null ? BigDecimal.ZERO : d.getUnitQty();
							weights.add(w.max(BigDecimal.ZERO));
						}
						List<BigDecimal> ouAmts = allocByWeights(totalAmt, weights);
						for (int i = 0; i < dlist.size(); i++) {
							BigDecimal ouAmt = ouAmts.get(i);
							if (ouAmt.signum() == 0)
								continue;
							String dstOu = nvl(dlist.get(i).getOuCode());
							if (dstOu.isEmpty())
								continue;

							inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, dstOu,
									acc, acc4, ouAmt, opType, cache, createUser, "EX="+exNo);
						}
					}
				} else {
					inserted += splitOperateAndWrite(yymm, String.valueOf(budgetYear), ver1, approation, ou, acc, acc4,
							totalAmt, null, cache, createUser, "EX="+exNo);
				}
			}
		}

		return inserted;
	}

	/** 分攤後：分攤規則（僅取 ACTUAL_QTY_FLAG='Y'；當月無規則則回退 '00' 月） */
	protected int after_rules(String yymm, String version, String operator, RunCache cache) {
	    final String approation = "AFTER";
	    final String ver1 = BUDGET_VERSION;
	    final String createUser = (operator == null || operator.isEmpty()) ? "SYSTEM" : operator;

	    final String year = yymm.substring(0, 4);
	    final String month = yymm.substring(4, 6);

	    // 先取當月 + ACTUAL_QTY_FLAG='Y'
	    List<NcccApportionmentRuleM> masters =
	            ruleMRepo.findByYearAndMonthAndActualQtyFlag(year, month, "Y");
	    // 若當月沒有，回退到 '00' 月
	    if (masters == null || masters.isEmpty()) {
	        masters = ruleMRepo.findByYearAndMonthAndActualQtyFlag(year, "00", "Y");
	    }
	    if (masters == null || masters.isEmpty()) return 0;

	    int inserted = 0;

	    for (NcccApportionmentRuleM m : masters) {
	        String belongOu = nvl(m.getBelongOuCode());
	        String accounting = nvl(m.getAccounting());
	        if (belongOu.isEmpty() || accounting.isEmpty()) continue;

	        List<NcccApportionmentRuleD> details =
	                ruleDRepo.findByYearAndMonthAndAccounting(m.getYear(), m.getMonth(), m.getAccounting());
	        if (details == null || details.isEmpty()) continue;

	        final boolean is5101 = accounting.length() >= 4 && "5101".equals(accounting.substring(0, 4));
	        final String opType = nvl(m.getOperationType()); // 1:人數 2:金額

	        if (is5101 && !"1".equals(opType) && !"2".equals(opType)) {
	            log.warn("5101 without OPERATION_TYPE: year={}, month={}, acc={}", year, month, accounting);
	            continue; // 略過此主檔
	        }
	        
	        for (NcccApportionmentRuleD d : details) {
	            String dstOu = nvl(d.getOuCode());
	            if (dstOu.isEmpty()) continue;

	            final String remark = "RULE=" + m.getYear() + m.getMonth() + ";" +
                        "SRC=" + belongOu + "->DST=" + dstOu;
	            
	            if (is5101 && ("1".equals(opType) || "2".equals(opType))) {
	                BigDecimal unitQty = nz(d.getUnitQty());

	                if ("1".equals(opType)) {
	                    List<NcccOuOperateDRepository.OperQtyRatioRow> ratioRows =
	                            ouOpRepo.findItemsQtyRatio(year, ver1, dstOu, accounting);
	                    if (ratioRows != null && !ratioRows.isEmpty()) {
	                        List<BigDecimal> allocs = new java.util.ArrayList<BigDecimal>(ratioRows.size());
	                        BigDecimal sum = BigDecimal.ZERO; int maxIdx = -1; BigDecimal maxAbs = BigDecimal.ZERO;
	                        for (int i = 0; i < ratioRows.size(); i++) {
	                            BigDecimal a = unitQty.multiply(toRatio(ratioRows.get(i).getOperateQtyRatio()))
	                                                  .setScale(0, java.math.RoundingMode.HALF_UP);
	                            allocs.add(a); sum = sum.add(a);
	                            if (a.abs().compareTo(maxAbs) > 0) { maxAbs = a.abs(); maxIdx = i; }
	                        }
	                        BigDecimal diff = unitQty.subtract(sum);
	                        if (diff.signum() != 0 && maxIdx >= 0) allocs.set(maxIdx, allocs.get(maxIdx).add(diff));
	                        for (int i = 0; i < ratioRows.size(); i++) {
	                            BigDecimal a = allocs.get(i);
	                            if (a.signum() == 0) continue;
	                            NcccOuOperateDRepository.OperQtyRatioRow r = ratioRows.get(i);
	                            inserted += writeActual(yymm, ver1, approation, dstOu, cache.getOuName(dstOu),
	                                    accounting, cache.getSubject(accounting),
	                                    nvl(r.getOperateItemCode()),
	                                    nvl(r.getOperateItem()).isEmpty() ? nvl(r.getOperateItemCode()) : nvl(r.getOperateItem()),
	                                    a, createUser, remark);
	                        }
	                    } else {
	                        List<NcccOuOperateDRepository.OperQtyRow> wr =
	                                ouOpRepo.findItems(year, ver1, dstOu, accounting);
	                        inserted += splitByWeightsFallback(
	                                yymm, ver1, approation, dstOu, accounting, cache, createUser, wr, unitQty, remark);
	                    }
	                } else { // "2" 金額
	                    List<NcccOuOperateDRepository.OperAmtRatioRow> ratioRows =
	                            ouOpRepo.findItemsAmtRatio(year, ver1, dstOu, accounting);
	                    if (ratioRows != null && !ratioRows.isEmpty()) {
	                        List<BigDecimal> allocs = new java.util.ArrayList<BigDecimal>(ratioRows.size());
	                        BigDecimal sum = BigDecimal.ZERO; int maxIdx = -1; BigDecimal maxAbs = BigDecimal.ZERO;
	                        for (int i = 0; i < ratioRows.size(); i++) {
	                            BigDecimal a = unitQty.multiply(toRatio(ratioRows.get(i).getOperateAmtRatio()))
	                                                  .setScale(0, java.math.RoundingMode.HALF_UP);
	                            allocs.add(a); sum = sum.add(a);
	                            if (a.abs().compareTo(maxAbs) > 0) { maxAbs = a.abs(); maxIdx = i; }
	                        }
	                        BigDecimal diff = unitQty.subtract(sum);
	                        if (diff.signum() != 0 && maxIdx >= 0) allocs.set(maxIdx, allocs.get(maxIdx).add(diff));
	                        for (int i = 0; i < ratioRows.size(); i++) {
	                            BigDecimal a = allocs.get(i);
	                            if (a.signum() == 0) continue;
	                            NcccOuOperateDRepository.OperAmtRatioRow r = ratioRows.get(i);
	                            inserted += writeActual(yymm, ver1, approation, dstOu, cache.getOuName(dstOu),
	                                    accounting, cache.getSubject(accounting),
	                                    nvl(r.getOperateItemCode()),
	                                    nvl(r.getOperateItem()).isEmpty() ? nvl(r.getOperateItemCode()) : nvl(r.getOperateItem()),
	                                    a, createUser, remark);
	                        }
	                    } else {
	                        List<NcccOuOperateDRepository.OperAmtRow> wr =
	                                ouOpRepo.findItemsAmt(year, ver1, dstOu, accounting);
	                        inserted += splitByWeightsFallback(
	                                yymm, ver1, approation, dstOu, accounting, cache, createUser, wr, unitQty, remark);
	                    }
	                }
	                continue;
	            }

	            BigDecimal sourceAmt = nz(budgetDRepo.sumFinalTotal(year, ver1, belongOu, accounting));
	            if (sourceAmt.signum() == 0) continue;

	            List<NcccBudgetDRepository.BudgetItem> items =
	                    budgetDRepo.findBudgetItems(Integer.parseInt(year), ver1, belongOu, accounting);
	            if (items == null || items.isEmpty()) continue;

	            List<BigDecimal> allocs = new java.util.ArrayList<BigDecimal>(items.size());
	            BigDecimal sum = BigDecimal.ZERO; int maxIdx = -1; BigDecimal maxAbs = BigDecimal.ZERO;
	            for (int i = 0; i < items.size(); i++) {
	                NcccBudgetDRepository.BudgetItem it = items.get(i);
	                BigDecimal a = sourceAmt.multiply(toRatio(it.getOperateRatio()))
	                                        .setScale(0, java.math.RoundingMode.HALF_UP);
	                allocs.add(a); sum = sum.add(a);
	                if (a.abs().compareTo(maxAbs) > 0) { maxAbs = a.abs(); maxIdx = i; }
	            }
	            BigDecimal diff = sourceAmt.subtract(sum);
	            if (diff.signum() != 0 && maxIdx >= 0) allocs.set(maxIdx, allocs.get(maxIdx).add(diff));

	            String ouName = cache.getOuName(dstOu);
	            String subject = cache.getSubject(accounting);
	            for (int i = 0; i < items.size(); i++) {
	                BigDecimal a = allocs.get(i);
	                if (a.signum() == 0) continue;
	                NcccBudgetDRepository.BudgetItem it = items.get(i);
	                inserted += writeActual(yymm, ver1, approation, dstOu, ouName, accounting, subject,
	                        nvl(it.getOperateItemCode()),
	                        nvl(it.getOperateItem()).isEmpty() ? nvl(it.getOperateItemCode()) : nvl(it.getOperateItem()),
	                        a, createUser, remark);
	            }
	        }
	    }
	    return inserted;
	}


	/**
	 * 依作業項目規則分配： 5101 且 OPERATION_TYPE=1 → 用 OPERATE_QTY_RATIO 直乘 5101 且
	 * OPERATION_TYPE=2 → 用 OPERATE_AMT_RATIO 直乘 其他或無比率 → 用預算作業項目比例 VERSION 固定 "2"
	 * 若比率不存在才退回數值權重正規化。
	 */
	private int splitOperateAndWrite(String yymm, String yy, String ver1, String approation, String ou, String acc,
			String acc4, BigDecimal totalAmt, String operationType, RunCache cache, String createUser, String remark) {

		final List<String> codes = new ArrayList<String>();
		final List<String> names = new ArrayList<String>();
		final List<BigDecimal> ratios = new ArrayList<BigDecimal>();
		boolean usedRatio = false;

		if ("5101".equals(acc4)) {
			String ot = nvl(operationType);
			if ("1".equals(ot)) {
				// 人數比例：優先使用比例欄位
				List<NcccOuOperateDRepository.OperQtyRatioRow> rows = ouOpRepo.findItemsQtyRatio(yy, ver1, ou, acc);
				if (rows != null && !rows.isEmpty()) {
					for (NcccOuOperateDRepository.OperQtyRatioRow r : rows) {
						String code = nvl(r.getOperateItemCode());
						if (code.isEmpty())
							continue;
						codes.add(code);
						names.add(nvl(r.getOperateItem()).isEmpty() ? code : nvl(r.getOperateItem()));
						ratios.add(toRatio(r.getOperateQtyRatio()));
					}
					usedRatio = true;
				} else {
					// 用數值權重
					List<NcccOuOperateDRepository.OperQtyRow> wr = ouOpRepo.findItems(yy, ver1, ou, acc);
					return splitByWeightsFallback(yymm, ver1, approation, ou, acc, cache, createUser, wr, totalAmt,
							remark);
				}
			} else if ("2".equals(ot)) {
				// 金額比例：優先使用比例欄位
				List<NcccOuOperateDRepository.OperAmtRatioRow> rows = ouOpRepo.findItemsAmtRatio(yy, ver1, ou, acc);
				if (rows != null && !rows.isEmpty()) {
					for (NcccOuOperateDRepository.OperAmtRatioRow r : rows) {
						String code = nvl(r.getOperateItemCode());
						if (code.isEmpty())
							continue;
						codes.add(code);
						names.add(nvl(r.getOperateItem()).isEmpty() ? code : nvl(r.getOperateItem()));
						ratios.add(toRatio(r.getOperateAmtRatio()));
					}
					usedRatio = true;
				} else {
					// 用數值權重
					List<NcccOuOperateDRepository.OperAmtRow> wr = ouOpRepo.findItemsAmt(yy, ver1, ou, acc);
					return splitByWeightsFallback(yymm, ver1, approation, ou, acc, cache, createUser, wr, totalAmt,
							remark);
				}
			}
		}

		if (!usedRatio) {
			// 非 5101 或未指定 OPERATION_TYPE → 預算作業項目比例 VERSION 固定 "2"
			List<NcccBudgetDRepository.BudgetItem> rows = budgetDRepo.findBudgetItems(Integer.parseInt(yy),
					BUDGET_VERSION, ou, acc);
			if (rows == null || rows.isEmpty())
				return 0;
			for (NcccBudgetDRepository.BudgetItem it : rows) {
				String code = nvl(it.getOperateItemCode());
				if (code.isEmpty())
					continue;
				codes.add(code);
				names.add(nvl(it.getOperateItem()).isEmpty() ? code : nvl(it.getOperateItem()));
				ratios.add(toRatio(it.getOperateRatio()));
			}
		}

		// 比例直乘並補差到絕對值最大
		List<BigDecimal> allocs = new ArrayList<BigDecimal>(codes.size());
		BigDecimal sum = BigDecimal.ZERO;
		int maxIdx = -1;
		BigDecimal maxAbs = BigDecimal.ZERO;

		for (int i = 0; i < codes.size(); i++) {
			BigDecimal a = totalAmt.multiply(ratios.get(i)).setScale(0, java.math.RoundingMode.HALF_UP);
			allocs.add(a);
			sum = sum.add(a);
			if (a.abs().compareTo(maxAbs) > 0) {
				maxAbs = a.abs();
				maxIdx = i;
			}
		}
		BigDecimal diff = totalAmt.subtract(sum);
		if (diff.signum() != 0 && maxIdx >= 0)
			allocs.set(maxIdx, allocs.get(maxIdx).add(diff));

		int inserted = 0;
		for (int i = 0; i < codes.size(); i++) {
			BigDecimal a = allocs.get(i);
			if (a.signum() == 0)
				continue;
			inserted += writeActual(yymm, ver1, approation, ou, cache.getOuName(ou), acc, cache.getSubject(acc),
					codes.get(i), names.get(i), a, createUser, remark);
		}
		return inserted;
	}

	private int splitOperateAndWrite(String yymm, String yy, String ver1, String approation, String ou, String acc,
			String acc4, BigDecimal totalAmt, String operationType, RunCache cache, String createUser) {
		return splitOperateAndWrite(yymm, yy, ver1, approation, ou, acc, acc4, totalAmt, operationType, cache,
				createUser, null);
	}

	/** 僅在沒有比率欄位時使用：以數值權重正規化 + 補差 */
	private int splitByWeightsFallback(String yymm, String ver1, String approation, String ou, String acc,
			RunCache cache, String createUser, List<? extends NcccOuOperateDRepository.WeightedRow> rows,
			BigDecimal totalAmt, String remark) {

		if (rows == null || rows.isEmpty())
			return 0;

		List<BigDecimal> weights = new ArrayList<BigDecimal>(rows.size());
		List<String> codes = new ArrayList<String>(rows.size());
		List<String> names = new ArrayList<String>(rows.size());

		for (NcccOuOperateDRepository.WeightedRow r : rows) {
			String code = nvl(r.getOperateItemCode());
			if (code.isEmpty())
				continue;
			codes.add(code);
			names.add(nvl(r.getOperateItem()).isEmpty() ? code : nvl(r.getOperateItem()));
			weights.add(nz(r.getWeight()).max(BigDecimal.ZERO));
		}

		List<BigDecimal> alloc = allocByWeights(totalAmt, weights);

		int inserted = 0;
		for (int i = 0; i < codes.size(); i++) {
			BigDecimal a = alloc.get(i);
			if (a.signum() == 0)
				continue;
			inserted += writeActual(yymm, ver1, approation, ou, cache.getOuName(ou), acc, cache.getSubject(acc),
					codes.get(i), names.get(i), a, createUser, remark);
		}
		return inserted;
	}

	/** 權重分配到整數，補差到絕對值最大筆 */
	private static List<BigDecimal> allocByWeights(BigDecimal total, List<BigDecimal> weights) {
		List<BigDecimal> out = new ArrayList<BigDecimal>(weights.size());
		BigDecimal sumW = BigDecimal.ZERO;
		for (BigDecimal w : weights)
			sumW = sumW.add((w == null) ? BigDecimal.ZERO : w.max(BigDecimal.ZERO));
		if (sumW.signum() == 0) {
			for (int i = 0; i < weights.size(); i++)
				out.add(BigDecimal.ZERO);
			return out;
		}
		BigDecimal sumAlloc = BigDecimal.ZERO;
		int maxIdx = -1;
		BigDecimal maxAbs = BigDecimal.ZERO;

		for (int i = 0; i < weights.size(); i++) {
			BigDecimal w = (weights.get(i) == null) ? BigDecimal.ZERO : weights.get(i).max(BigDecimal.ZERO);
			BigDecimal v = total.multiply(w).divide(sumW, 0, java.math.RoundingMode.HALF_UP);
			out.add(v);
			sumAlloc = sumAlloc.add(v);
			if (v.abs().compareTo(maxAbs) > 0) {
				maxAbs = v.abs();
				maxIdx = i;
			}
		}
		BigDecimal diff = total.subtract(sumAlloc);
		if (diff.signum() != 0 && maxIdx >= 0)
			out.set(maxIdx, out.get(maxIdx).add(diff));
		return out;
	}

	private int writeActual(String yymm, String ver1, String approation, String ou, String ouName, String acc,
			String subject, String operateItemCode, String operateItem, BigDecimal amount, String createUser,
			String remark) {

		NcccBudgetActual a = new NcccBudgetActual();
		a.setKey(yymm, ver1, approation, ou, acc, operateItemCode);
		a.setOuName(ouName);
		a.setSubject(subject);
		a.setOperateItem(operateItem);
		a.setAmount(amount);
		a.setRemark(remark);
		a.setCreateUser(createUser);
		a.setUpdateUser(createUser);
		a.setCreateDate(new java.util.Date());
		a.setUpdateDate(new java.util.Date());
		actualRepo.save(a);
		return 1;
	}
	
	private static BigDecimal nz(BigDecimal v) {
		return v == null ? BigDecimal.ZERO : v;
	}

	// 去掉前置 0
	private static String lz(String s) {
		if (s == null)
			return "";
		String t = s.replaceFirst("^0+", "");
		return t == null ? "" : t;
	}

	// 將百分比或小數轉為小數；>1 視為百分比
	private static BigDecimal toRatio(BigDecimal v) {
		if (v == null || v.signum() <= 0)
			return BigDecimal.ZERO;
		return v.compareTo(BigDecimal.ONE) > 0 ? v.divide(HUNDRED, 8, java.math.RoundingMode.HALF_UP) : v;
	}

	private static String nvl(String s) {
		return s == null ? "" : s.trim();
	}

	private static final class RunCache {
		private final SyncOURepository syncOURepo;
		private final NcccAccountingListRepository accListRepo;
		private final java.util.Map<String, String> ouName = new java.util.HashMap<String, String>();
		private final java.util.Map<String, String> subject = new java.util.HashMap<String, String>();

		RunCache(SyncOURepository syncOURepo, NcccAccountingListRepository accListRepo) {
			this.syncOURepo = syncOURepo;
			this.accListRepo = accListRepo;
		}

		String getOuName(String ou) {
			if (ou == null)
				return "";
			String key = ou.trim();
			String cached = ouName.get(key);
			if (cached != null)
				return cached;
			SyncOU e = syncOURepo.findByOuCode(key);
			String val = (e == null || e.getOuName() == null || e.getOuName().trim().isEmpty()) ? key
					: e.getOuName().trim();
			ouName.put(key, val);
			return val;
		}

		String getSubject(String acc) {
			if (acc == null)
				return "";
			String key = acc.trim();
			String cached = subject.get(key);
			if (cached != null)
				return cached;
			java.util.List<NcccAccountingListRepository.AccName> list = accListRepo
					.findNamesBySubjectIn(java.util.Collections.singleton(key));
			String val = key;
			if (list != null && !list.isEmpty()) {
				String name = list.get(0).getName();
				if (name != null && !name.trim().isEmpty())
					val = name.trim();
			}
			subject.put(key, val);
			return val;
		}
	}
}
