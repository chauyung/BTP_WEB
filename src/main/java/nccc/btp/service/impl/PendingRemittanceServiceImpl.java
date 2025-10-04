package nccc.btp.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccDepositBank;
import nccc.btp.entity.NcccPendingRemittance;
import nccc.btp.entity.SapPendingRemittanceStatus;
import nccc.btp.repository.NcccDepositBankRepository;
import nccc.btp.repository.NcccPendingRemittanceRepository;
import nccc.btp.repository.SapPendingRemittanceStatusRepository;
import nccc.btp.rfc.SapUtil;
import nccc.btp.rfc.ZHeader;
import nccc.btp.rfc.ZItem;
import nccc.btp.rfc.ZReturn;
import nccc.btp.service.PendingRemittanceService;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.PendingRemittanceQueryVo;
import nccc.btp.vo.PendingRemittanceVo;

@Service
@Transactional
public class PendingRemittanceServiceImpl implements PendingRemittanceService {

  protected static Logger LOG = LoggerFactory.getLogger(PendingRemittanceServiceImpl.class);

  @Autowired
  private NcccPendingRemittanceRepository ncccPendingRemittanceRepository;

  @Autowired
  private NcccDepositBankRepository ncccDepositBankRepository;

  @Autowired
  private SapPendingRemittanceStatusRepository sapPendingRemittanceStatusRepository;

  @Autowired
  private SapUtil sapUtil;

  @Override
  public List<NcccDepositBank> getBankNo() {
    return ncccDepositBankRepository.findAll(Sort.by(Sort.Direction.ASC, "account"));
  }

  @Override
  public List<PendingRemittanceVo> search(PendingRemittanceQueryVo pendingRemittanceQueryVo) {
    Specification<NcccPendingRemittance> spec = (root, query, cb) -> {
      query.distinct(true);
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.hasText(pendingRemittanceQueryVo.getBankNo())) {
        predicates.add(cb.equal(root.get("bankNo"), pendingRemittanceQueryVo.getBankNo()));
      }
      if (StringUtils.hasText(pendingRemittanceQueryVo.getStartDate())) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("depositDate"),
            pendingRemittanceQueryVo.getStartDate()));
      }

      if (StringUtils.hasText(pendingRemittanceQueryVo.getEndDate())) {
        predicates.add(
            cb.lessThanOrEqualTo(root.get("depositDate"), pendingRemittanceQueryVo.getEndDate()));
      }
      if (StringUtils.hasText(pendingRemittanceQueryVo.getDepositAmount())) {
        BigDecimal depositAmt = new BigDecimal(pendingRemittanceQueryVo.getDepositAmount());
        predicates.add(cb.equal(root.get("depositAmount"), depositAmt));
      }
      if (StringUtils.hasText(pendingRemittanceQueryVo.getWriteOffAmount())) {
        BigDecimal writeOffAmt = new BigDecimal(pendingRemittanceQueryVo.getWriteOffAmount());
        predicates.add(cb.equal(root.get("writeOffAmount"), writeOffAmt));
      }
      if (StringUtils.hasText(pendingRemittanceQueryVo.getDepositDesc())) {
        predicates.add(cb.like(root.get("depositDesc"),
            "%" + pendingRemittanceQueryVo.getDepositDesc() + "%"));
      }

      Join<NcccPendingRemittance, SapPendingRemittanceStatus> s =
          root.join("sapPendingRemittanceStatus", JoinType.LEFT);
      if (StringUtils.hasText(pendingRemittanceQueryVo.getBelnr())) {
        predicates.add(cb.equal(s.get("belnr"), pendingRemittanceQueryVo.getBelnr()));
      }
      if (StringUtils.hasText(pendingRemittanceQueryVo.getType())) {
        predicates.add(cb.equal(s.get("type"), pendingRemittanceQueryVo.getType()));
      }

      query.orderBy(cb.asc(root.get("bankNo")), cb.asc(root.get("depositDate")));

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    List<NcccPendingRemittance> entities = ncccPendingRemittanceRepository.findAll(spec);
    return entities.stream().map(n -> new PendingRemittanceVo(n, n.getSapPendingRemittanceStatus()))
        .collect(Collectors.toList());
  }

  @Override
  public NcccPendingRemittance add(NcccPendingRemittance ncccPendingRemittance) {
    NcccUserDto user = SecurityUtil.getCurrentUser();
    ncccPendingRemittance.setCreateUser(user.getHrid());
    ncccPendingRemittance.setCreateDate(LocalDate.now());
    if (ncccPendingRemittance.getDescription() != null) {
      ncccPendingRemittance.setDescriptionUser(user.getHrid());
      ncccPendingRemittance.setDescriptionDate(LocalDate.now());
      ncccPendingRemittance.setDescriptionOuCode(user.getOuCode());
    }

    // 取得年月 (yymm)
    String prefix = DateTimeFormatter.ofPattern("yyMM").format(LocalDate.now());

    // 查出 prefix 開頭的最大 id
    Long maxId = ncccPendingRemittanceRepository.findMaxIdLike(prefix + "%");

    int nextSeq = 1;
    if (maxId != null) {
      String seqStr = String.valueOf(maxId).substring(4); // 後四碼是流水號
      nextSeq = Integer.parseInt(seqStr) + 1;
    }

    String newIdStr = prefix + String.format("%04d", nextSeq);
    Long newId = Long.valueOf(newIdStr);
    ncccPendingRemittance.setId(newId);
    return ncccPendingRemittanceRepository.save(ncccPendingRemittance);
  }

  @Override
  public NcccPendingRemittance update(NcccPendingRemittance ncccPendingRemittance) {
    NcccUserDto user = SecurityUtil.getCurrentUser();
    boolean isC102 = "C102".equalsIgnoreCase(user.getOuCode());
    LocalDate today = LocalDate.now();

    NcccPendingRemittance original =
        ncccPendingRemittanceRepository.findById(ncccPendingRemittance.getId())
            .orElseThrow(() -> new RuntimeException(ncccPendingRemittance.getId() + " not found"));

    if (isC102) {
      // 出納單位：可改所有欄位
      if (!Objects.equals(original.getDescription(), ncccPendingRemittance.getDescription())) {
        ncccPendingRemittance.setDescriptionUser(user.getHrid());
        ncccPendingRemittance.setDescriptionDate(today);
        ncccPendingRemittance.setDescriptionOuCode(user.getOuCode());
      }
      ncccPendingRemittance.setUpdateUser(user.getHrid());
      ncccPendingRemittance.setUpdateDate(today);

      return ncccPendingRemittanceRepository.save(ncccPendingRemittance);
    } else {
      // 非出納單位：只能改款項說明
      ncccPendingRemittanceRepository.updateDescriptionById(ncccPendingRemittance.getDescription(),
          ncccPendingRemittance.getDescriptionUser(), today,
          ncccPendingRemittance.getDescriptionOuCode(), ncccPendingRemittance.getId());

      return ncccPendingRemittanceRepository.findById(ncccPendingRemittance.getId())
          .orElseThrow(() -> new RuntimeException(ncccPendingRemittance.getId() + " not found"));
    }
  }

  @Override
  public String delete(Long id) {
    if (!ncccPendingRemittanceRepository.existsById(id)) {
      throw new RuntimeException(id + " not found");
    }
    ncccPendingRemittanceRepository.deleteById(id);
    return "deleted successfully";
  }

  @Override
  public String checkout(PendingRemittanceQueryVo pendingRemittanceQueryVo) {

    List<Long> ids = pendingRemittanceQueryVo.getIds();
    if (ids == null || ids.isEmpty()) {
      throw new RuntimeException("清單中沒有可用的 id");
    }

    // 先檢核 id 是否存在（
    long existCount = ncccPendingRemittanceRepository.countByIdIn(ids);
    if (existCount == 0) {
      throw new RuntimeException("找不到任何可結帳的資料");
    }

    String dateStr = pendingRemittanceQueryVo.getCheckoutDate();

    int updatedRow = ncccPendingRemittanceRepository.updateCheckoutDateByIds(dateStr, ids);

    return "結帳了 " + updatedRow + " 筆資料,請重新查詢";
  }

  @Override
  public List<PendingRemittanceVo> toSAP(List<NcccPendingRemittance> list) {
    NcccUserDto user = SecurityUtil.getCurrentUser();
    NcccPendingRemittance first = list.get(0);
    ZHeader header = new ZHeader();
    header.setZSYSTEM("BTP");
    header.setBUKRS("1010");
    header.setBLDAT(first.getCheckoutDate());
    header.setBUDAT(first.getCheckoutDate());
    header.setBLART("SA");
    header.setWAERS("TWD");
    header.setXBLNR("");
    header.setBKTXT("待查匯入款");
    header.setXREF1_HD(first.getCheckoutDate() + first.getId().toString());
    ArrayList<ZItem> zitemList = new ArrayList<ZItem>();

    // 40處理 待沖金額依據銀行加總 先用map整理
    Map<String, ZItem> sumMap = new LinkedHashMap<>();
    for (NcccPendingRemittance obj : list) {
      ZItem zitem = new ZItem();
      if (sumMap.get(obj.getBankNo()) != null) {
        ZItem item = sumMap.get(obj.getBankNo());
        String OffsetAmountStr = item.getWRBTR();
        BigDecimal OffsetAmount = new BigDecimal(OffsetAmountStr).add(obj.getOffsetAmount());
        zitem.setBSCHL("40");
        zitem.setNEWKO(obj.getBankNo());
        zitem.setWRBTR(OffsetAmount.toString());
        zitem.setSGTXT(item.getSGTXT());
        sumMap.put(obj.getBankNo(), zitem);
      } else {
        zitem.setBSCHL("40");
        zitem.setNEWKO(obj.getBankNo());
        zitem.setWRBTR(obj.getOffsetAmount().toString());
        zitem.setSGTXT("收" + obj.getDepositDesc() + "等");
        sumMap.put(obj.getBankNo(), zitem);
      }
    }
    // 整理完再塞進去
    for (Map.Entry<String, ZItem> entry : sumMap.entrySet()) {
      zitemList.add(entry.getValue());
    }

    List<PendingRemittanceVo> voList = new ArrayList<>();
    // 50處理
    for (NcccPendingRemittance obj : list) {
      ZItem zitem = new ZItem();
      zitem.setBSCHL("50");
      zitem.setNEWKO("28970397");
      zitem.setWRBTR(obj.getOffsetAmount().toString());
      zitem.setSGTXT("暫收" + obj.getDepositDate() + obj.getDepositDesc());
      zitem.setZUONR(obj.getBankNo());
      zitem.setXREF1(obj.getId().toString());
      zitemList.add(zitem);
      PendingRemittanceVo vo = new PendingRemittanceVo();
      vo.setNcccPendingRemittance(obj);
      voList.add(vo);
    }
    ZReturn zreturn;
    try {
      zreturn = sapUtil.callZcreateAccDocument(header, zitemList);

      List<SapPendingRemittanceStatus> sapPendingRemittanceStatusList = new ArrayList<>();
      for (NcccPendingRemittance obj : list) {
        SapPendingRemittanceStatus sapPendingRemittanceStatus = new SapPendingRemittanceStatus();
        sapPendingRemittanceStatus.setId(obj.getId());
        sapPendingRemittanceStatus.setType(zreturn.getTYPE());
        sapPendingRemittanceStatus.setBukrs(zreturn.getBUKRS());
        sapPendingRemittanceStatus.setBelnr(zreturn.getBELNR());
        sapPendingRemittanceStatus.setGjahr(zreturn.getGJAHR());
        sapPendingRemittanceStatus.setMessage(zreturn.getMESSAGE());
        sapPendingRemittanceStatus.setCreateUser(user.getHrid());
        sapPendingRemittanceStatusList.add(sapPendingRemittanceStatus);
      }
      List<SapPendingRemittanceStatus> saved =
          sapPendingRemittanceStatusRepository.saveAll(sapPendingRemittanceStatusList);
      // 建立 id -> status 的對照表
      Map<Long, SapPendingRemittanceStatus> statusById = new HashMap<>();
      for (SapPendingRemittanceStatus s : saved) {
        statusById.put(s.getId(), s);
      }

      // 將狀態塞回對應的 VO
      for (PendingRemittanceVo vo : voList) {
        Long id = vo.getNcccPendingRemittance().getId();
        vo.setSapPendingRemittanceStatus(statusById.get(id));
      }
    } catch (Exception e) {
      List<SapPendingRemittanceStatus> errorStatuses = new ArrayList<>();

      for (NcccPendingRemittance obj : list) {
        SapPendingRemittanceStatus sapPendingRemittanceStatus = new SapPendingRemittanceStatus();
        sapPendingRemittanceStatus.setId(obj.getId());
        sapPendingRemittanceStatus.setType("E");
        sapPendingRemittanceStatus.setCreateUser(user.getHrid());
        sapPendingRemittanceStatus.setMessage("SAP發生錯誤");
        errorStatuses.add(sapPendingRemittanceStatus);
      }

      List<SapPendingRemittanceStatus> savedErrors =
          sapPendingRemittanceStatusRepository.saveAll(errorStatuses);

      Map<Long, SapPendingRemittanceStatus> statusById = new HashMap<>();
      for (SapPendingRemittanceStatus s : savedErrors) {
        statusById.put(s.getId(), s);
      }

      for (PendingRemittanceVo vo : voList) {
        Long id = vo.getNcccPendingRemittance().getId();
        vo.setSapPendingRemittanceStatus(statusById.get(id));
      }
      LOG.error("SAP 連線發生錯誤", e);
    }
    return voList;
  }
}
