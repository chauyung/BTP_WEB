package nccc.btp.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import nccc.btp.dto.NcccUserDto;
import nccc.btp.dto.OperateItemsSaveRequest;
import nccc.btp.entity.NcccBudgetActual;
import nccc.btp.repository.BudgetActualRepository;
import nccc.btp.service.BudgetActualMaintenanceService;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.BudgetActualVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class BudgetActualMaintenanceServiceImpl implements BudgetActualMaintenanceService {

    private static final Logger log = LoggerFactory.getLogger(BudgetActualMaintenanceServiceImpl.class);

    @Autowired
    private BudgetActualRepository repository;

    @Override
    public List<BudgetActualVo> search(BudgetActualVo vo) {
        String yymm = val(vo.getYymm());
        String ver = val(vo.getVersion());
        if (yymm == null || ver == null) return new ArrayList<BudgetActualVo>();

        String appr = null;
        if (StringUtils.hasText(vo.getApproation())) {
            String ap = vo.getApproation().trim().toUpperCase();
            if ("BOTH".equals(ap)) {
                appr = null;
            } else {
                appr = ap;
            }
        }

        List<String> ouList = null;
        Integer ouEmpty = 1;
        if (vo.getOuCodes() != null) {
            ouList = vo.getOuCodes().stream()
                    .filter(Objects::nonNull).map(String::trim)
                    .filter(s -> !s.isEmpty()).map(String::toUpperCase)
                    .distinct().collect(Collectors.<String>toList());
            ouEmpty = (ouList == null || ouList.isEmpty()) ? 1 : 0;
        }

        String accFrom = StringUtils.hasText(vo.getAccountingStart())
                ? leftPadZeros(vo.getAccountingStart().trim(), 8) : null;
        String accTo = StringUtils.hasText(vo.getAccountingEnd())
                ? leftPadZeros(vo.getAccountingEnd().trim(), 8) : null;

        List<NcccBudgetActual> rows = repository.searchBudgetActual(
                yymm, ver, appr, ouList, ouEmpty, accFrom, accTo
        );

        List<BudgetActualVo> out = new ArrayList<BudgetActualVo>(rows == null ? 0 : rows.size());
        if (rows != null) {
            for (NcccBudgetActual e : rows) {
                if (e != null) out.add(new BudgetActualVo(e));
            }
        }
        return out;
    }

    private String normalizeApproation(String ap) {
        if (ap == null) return null;
        String v = ap.trim().toUpperCase();
        if ("A".equals(v)) return "AFTER";
        if ("B".equals(v)) return "BEFORE";
        if ("AFTER".equals(v) || "BEFORE".equals(v)) return v;
        if ("BOTH".equals(v) || "ALL".equals(v)) return "BOTH";
        return v;
    }
    private String val(String s) {
        if (s == null) return null;
        String v = s.trim();
        return v.isEmpty() ? null : v;
    }
    private String leftPadZeros(String s, int len) {
        if (s == null) return null;
        String v = s.trim();
        if (v.length() >= len) return v;
        StringBuilder sb = new StringBuilder(len);
        for (int i = v.length(); i < len; i++) sb.append('0');
        sb.append(v);
        return sb.toString();
    }
    private String normUpperTrim(String s) {
        if (s == null) return null;
        String v = s.trim();
        return v.isEmpty() ? null : v.toUpperCase();
    }
    private String normTrim(String s) {
        if (s == null) return null;
        String v = s.trim();
        return v.isEmpty() ? null : v;
    }
    private String firstNonBlank(String a, String b) {
        if (a != null && a.trim().length() > 0) return a;
        if (b != null && b.trim().length() > 0) return b;
        return null;
    }
    private BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    @Override
    @Transactional
    public List<BudgetActualVo> saveOperateItems(OperateItemsSaveRequest req) {
        OperateItemsSaveRequest.Key k = req.getKey();

        String yymm = val(k.getYymm());
        String version = val(k.getVersion());
        String approation = normalizeApproation(k.getApproation());
        String ouCode = normUpperTrim(k.getOuCode());
        String accounting = leftPadZeros(k.getAccounting(), 8);

        if (yymm == null || version == null || ouCode == null || accounting == null) {
            throw new IllegalArgumentException("yymm/version/ouCode/accounting 不可為空");
        }

        NcccUserDto user = SecurityUtil.getCurrentUser();
        LocalDate today = LocalDate.now();
        java.util.Date todayDate = Date.valueOf(today);

        List<NcccBudgetActual> groupSnapshot = repository.findAll((root, q, cb) -> cb.and(
                cb.equal(root.get("id").get("yymm"), yymm),
                cb.equal(root.get("id").get("version"), version),
                cb.equal(cb.upper(cb.trim(root.get("id").get("approation"))), approation == null ? null : approation.toUpperCase()),
                cb.equal(cb.upper(cb.trim(root.get("id").get("ouCode"))), ouCode),
                cb.equal(root.get("id").get("accounting"), accounting)
        ));
        String ouNameSeed = groupSnapshot.isEmpty() ? null : groupSnapshot.get(0).getOuName();
        String subjectSeed = groupSnapshot.isEmpty() ? null : groupSnapshot.get(0).getSubject();

        if (req.getUpdateItems() != null) {
            for (OperateItemsSaveRequest.UpdateItem u : req.getUpdateItems()) {
                String itemCodeRaw = firstNonBlank(u.getOperateItemCode(), u.getOperateItemCode());
                String itemCode = normUpperTrim(itemCodeRaw);
                if (itemCode == null) continue;

                java.util.Optional<NcccBudgetActual> opt = findOneByKey(
                        yymm, version, approation, ouCode, accounting, itemCode
                );
                if (!opt.isPresent()) continue;

                NcccBudgetActual e = opt.get();
                if (e.getOuName() == null) e.setOuName(ouNameSeed);
                if (e.getSubject() == null) e.setSubject(subjectSeed);

                e.setAmount(nz(u.getAmount()));
                e.setRemark(u.getRemark());
                e.setUpdateUser(user.getHrid());
                e.setUpdateDate(todayDate);
                repository.save(e);
            }
        }

        if (req.getCreateItems() != null) {
            for (OperateItemsSaveRequest.CreateItem c : req.getCreateItems()) {
                String itemCodeRaw = c.getOperateItemCode();
                String itemCode = normUpperTrim(itemCodeRaw);
                if (itemCode == null) throw new IllegalArgumentException("作業項目代號不可為空");

                String itemName = c.getOperateItem();
                itemName = itemName == null ? "" : itemName.trim();

                java.util.Optional<NcccBudgetActual> maybe = findOneByKey(
                        yymm, version, approation, ouCode, accounting, itemCode
                );

                NcccBudgetActual e = maybe.isPresent() ? maybe.get() : new NcccBudgetActual();
                if (!maybe.isPresent()) {
                    e.setKey(yymm, version, approation, ouCode, accounting, itemCode);
                }

                if (e.getOuName() == null) e.setOuName(ouNameSeed);
                if (e.getSubject() == null) e.setSubject(subjectSeed);

                e.setOperateItem(itemName);
                e.setAmount(nz(c.getAmount()));
                e.setRemark(c.getRemark());

                if (!maybe.isPresent()) {
                    e.setCreateUser(user.getHrid());
                    e.setCreateDate(todayDate);
                }
                e.setUpdateUser(user.getHrid());
                e.setUpdateDate(todayDate);

                repository.save(e);
            }
        }

        List<NcccBudgetActual> entities = repository.findAll((root, q, cb) -> {
            q.orderBy(
                    cb.asc(root.get("id").get("accounting")),
                    cb.asc(root.get("id").get("operateItemCode"))
            );
            return cb.and(
                    cb.equal(root.get("id").get("yymm"), yymm),
                    cb.equal(root.get("id").get("version"), version),
                    cb.equal(cb.upper(cb.trim(root.get("id").get("approation"))), approation == null ? null : approation.toUpperCase()),
                    cb.equal(cb.upper(cb.trim(root.get("id").get("ouCode"))), ouCode),
                    cb.equal(root.get("id").get("accounting"), accounting)
            );
        });

        List<BudgetActualVo> vos = new ArrayList<BudgetActualVo>(entities.size());
        for (NcccBudgetActual e : entities) {
            vos.add(new BudgetActualVo(e));
        }
        return vos;
    }

    private Optional<NcccBudgetActual> findOneByKey(
            String yymm, String version, String approation,
            String ouCode, String accounting, String itemCode) {

        final String yymmN = val(yymm);
        final String versionN = val(version);
        final String apprN = approation == null ? null : approation.trim().toUpperCase();
        final String ouN = normUpperTrim(ouCode);
        final String accN = accounting == null ? null : accounting.trim();
        final String itemN = normUpperTrim(itemCode);

        return repository.findOne((root, q, cb) -> {
            Path<?> id = root.get("id");
            Expression<String> appr = cb.upper(cb.trim(id.get("approation")));
            Expression<String> apprHead = apprN == null ? null : cb.substring(cb.literal(apprN), 1, 1);

            List<Predicate> ps = new ArrayList<javax.persistence.criteria.Predicate>();
            ps.add(cb.equal(id.get("yymm"), yymmN));
            ps.add(cb.equal(id.get("version"), versionN));
            ps.add(cb.equal(cb.upper(cb.trim(id.get("ouCode"))), ouN));
            ps.add(cb.equal(id.get("accounting"), accN));
            ps.add(cb.equal(cb.upper(cb.trim(id.get("operateItemCode"))), itemN));
            if (apprN != null) {
                ps.add(cb.or(cb.equal(appr, apprN), cb.equal(appr, apprHead)));
            }
            return cb.and(ps.toArray(new Predicate[ps.size()]));
        });
    }
}
