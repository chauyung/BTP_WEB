package nccc.btp.service.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import nccc.btp.entity.NcccEmsMgm;
import nccc.btp.entity.SapEmsRecStatus;
import nccc.btp.repository.NcccEmsMgmRepository;
import nccc.btp.repository.SapEmsRecStatusRepository;
import nccc.btp.rfc.SapUtil;
import nccc.btp.rfc.ZInfo;
import nccc.btp.rfc.ZReturn;
import nccc.btp.service.TerminalManagementService;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.BulkEmsUpdateVo;
import nccc.btp.vo.TerminalManagementVo;

@Service
@Transactional
public class TerminalManagementServiceImpl implements TerminalManagementService {

  protected static Logger LOG = LoggerFactory.getLogger(TerminalManagementServiceImpl.class);

  private static final String TEMPLATE_PATH = "template/terminalManagement_sample.csv";

  @Autowired
  private NcccEmsMgmRepository ncccEmsMgmRepository;

  @Autowired
  private SapEmsRecStatusRepository sapEmsRecStatusRepository;

  @Autowired
  private SapUtil sapUtil;

  @Override
  public List<TerminalManagementVo> search(String ncccWealthNo, String wealthNo) {
    Specification<NcccEmsMgm> spec = (root, query, cb) -> {
      query.distinct(true); // 避免重複
      root.join("sapEmsRecStatus", JoinType.LEFT);

      List<Predicate> ps = new ArrayList<>();

      if (StringUtils.hasText(ncccWealthNo)) {
        ps.add(cb.like(root.get("ncccWealthNo"), "%" + ncccWealthNo.trim() + "%"));
      }
      if (StringUtils.hasText(wealthNo)) {
        ps.add(cb.like(root.get("wealthNo"), "%" + wealthNo.trim() + "%"));
      }
      query.orderBy(cb.asc(cb.function("TO_NUMBER", Integer.class, root.get("wealthNo"))));
      return ps.isEmpty() ? cb.conjunction() : cb.and(ps.toArray(new Predicate[0]));
    };
    List<NcccEmsMgm> entities = ncccEmsMgmRepository.findAll(spec);
    return entities.stream().map(n -> new TerminalManagementVo(n, n.getSapEmsRecStatus()))
        .collect(Collectors.toList());
  }

  @Override
  public String update(List<NcccEmsMgm> ncccEmsMgmList) {
    int updated = 0;
    for (NcccEmsMgm ncccEmsMgm : ncccEmsMgmList) {
      if (!ncccEmsMgmRepository.existsById(ncccEmsMgm.getWealthNo())) {
        throw new RuntimeException(ncccEmsMgm.getPurSeqNo() + " not found");
      }
      ncccEmsMgm.setUpdateUser(SecurityUtil.getCurrentUser().getHrid());
      updated += ncccEmsMgmRepository.updateStatusByWealthNo(ncccEmsMgm.getWealthNo(),
          ncccEmsMgm.getStatus(), ncccEmsMgm.getPostingDate(), ncccEmsMgm.getDocNo(),
          ncccEmsMgm.getDescription(), ncccEmsMgm.getListed(), ncccEmsMgm.getImpairmentDate(),
          ncccEmsMgm.getUpdateUser());
    }
    return MessageFormat.format("成功修改筆數:{0}", updated);
  }

  @Override
  public String toSAP(List<NcccEmsMgm> ncccEmsMgmList) {
    int successCnt = 0;
    int errorCnt = 0;
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
    for (NcccEmsMgm ncccEmsMgm : ncccEmsMgmList) {
      ZInfo zinfo = new ZInfo();
      // 來源系統
      zinfo.setZSYSTEM("BTP");
      // 公司代碼
      zinfo.setBUKRS("1010");
      // 設備代號
      zinfo.setZEDCNO(ncccEmsMgm.getEqType());
      // 資產
      zinfo.setANLN1(ncccEmsMgm.getNcccWealthNo());
      // 子資產編號
      zinfo.setANLN2(ncccEmsMgm.getSubassets());
      String postingDate = ncccEmsMgm.getPostingDate().format(fmt);
      // 文件日期
      zinfo.setBLDAT(postingDate);
      // 過帳日期
      zinfo.setBUDAT(postingDate);
      // 資產起息日
      zinfo.setBZDAT(postingDate);
      // 項目內文
      zinfo.setSGTXT("公文文號:" + ncccEmsMgm.getDocNo());
      // 數量
      zinfo.setMENGE("1");
      if (LocalDate.now().getYear() == ncccEmsMgm.getCreateDate().getYear()) {
        // 自今年度購置
        zinfo.setXANEU("X");
      } else {
        // 前一年購置
        zinfo.setXAALT("X");
      }
      ZReturn zreturn;
      try {
        zreturn = sapUtil.callZretireAsset(zinfo);
        SapEmsRecStatus sapEmsRecStatus = new SapEmsRecStatus();
        sapEmsRecStatus.setWealthNo(ncccEmsMgm.getWealthNo());
        sapEmsRecStatus.setType(zreturn.getTYPE());
        sapEmsRecStatus.setBukrs(zreturn.getBUKRS());
        sapEmsRecStatus.setBelnr(zreturn.getBELNR());
        sapEmsRecStatus.setGjahr(zreturn.getGJAHR());
        sapEmsRecStatus.setMessage(zreturn.getMESSAGE());
        sapEmsRecStatusRepository.save(sapEmsRecStatus);
        if (zreturn.getTYPE().equals("S")) {
          successCnt++;
        } else {
          errorCnt++;
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return MessageFormat.format("成功筆數:{0}, 失敗筆數:{1}", successCnt, errorCnt);
  }

  @Override
  public Resource getTerminalManagementCsvSample() throws IOException {
    ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
    if (!resource.exists()) {
      throw new IOException("template is not found : " + TEMPLATE_PATH);
    }
    return resource;
  }

  @Override
  public String ledgerToScrap(List<BulkEmsUpdateVo> voList) {
    if (voList == null || voList.isEmpty())
      return "無資料可更新";

    // 預檢：全部必須存在
    StringBuilder notFound = new StringBuilder();
    for (BulkEmsUpdateVo vo : voList) {
      String wealthNo = vo.getWealthNo();
      String ncccWealthNo = vo.getNcccWealthNo();

      if (!ncccEmsMgmRepository.existsByWealthNoAndNcccWealthNo(wealthNo, ncccWealthNo)) {
        if (notFound.length() > 0)
          notFound.append('\n');
        notFound.append("不存在：採購流水編號=").append(wealthNo).append("，財產編號=").append(ncccWealthNo);
      }
    }
    if (notFound.length() > 0) {
      return "查無下列資料：\n" + notFound;
    }

    // 通過預檢才更新
    int updated = 0;
    for (BulkEmsUpdateVo vo : voList) {
      updated += ncccEmsMgmRepository.updateStatusToScrap(vo.getWealthNo(), vo.getNcccWealthNo(),
          vo.getPostingDate(), vo.getDocNo(), vo.getDescription());
    }
    return "已更新 " + updated + " 筆";
  }

  @Override
  public String ledgerToListed(List<BulkEmsUpdateVo> voList) {
    if (voList == null || voList.isEmpty())
      return "無資料可更新";

    // 預檢：全部必須「存在且 status=3(報廢)」
    StringBuilder notFound = new StringBuilder();
    for (BulkEmsUpdateVo vo : voList) {
      String wealthNo = vo.getWealthNo();
      String ncccWealthNo = vo.getNcccWealthNo();

      if (!ncccEmsMgmRepository.existsByWealthNoAndNcccWealthNoAndStatus(wealthNo, ncccWealthNo,
          "3")) {
        if (notFound.length() > 0)
          notFound.append('\n');
        notFound.append("不存在或狀態不為報廢：採購流水編號=").append(wealthNo).append("，財產編號=")
            .append(ncccWealthNo);
      }
    }
    if (notFound.length() > 0) {
      return "下列資料不存在或狀態不符合：\n" + notFound;
    }

    int updated = 0;
    for (BulkEmsUpdateVo vo : voList) {
      updated += ncccEmsMgmRepository.updateListed(vo.getWealthNo(), vo.getNcccWealthNo());
    }
    return "已更新 " + updated + " 筆";
  }

  @Override
  public String listedImpair(List<BulkEmsUpdateVo> voList) {
    if (voList == null || voList.isEmpty())
      return "無資料可更新";

    // 預檢：全部必須存在
    StringBuilder notFound = new StringBuilder();
    for (BulkEmsUpdateVo vo : voList) {
      String wealthNo = vo.getWealthNo();
      String ncccWealthNo = vo.getNcccWealthNo();

      if (!ncccEmsMgmRepository.existsByWealthNoAndNcccWealthNo(wealthNo, ncccWealthNo)) {
        if (notFound.length() > 0)
          notFound.append('\n');
        notFound.append("不存在：採購流水編號=").append(wealthNo).append("，財產編號=").append(ncccWealthNo);
      }
    }
    if (notFound.length() > 0) {
      return "查無下列資料，未執行任何更新：\n" + notFound;
    }

    int updated = 0;
    for (BulkEmsUpdateVo vo : voList) {
      updated += ncccEmsMgmRepository.updateImpairmentDate(vo.getImpairmentDate(), vo.getWealthNo(),
          vo.getNcccWealthNo());
    }
    return "已更新 " + updated + " 筆";
  }
}
