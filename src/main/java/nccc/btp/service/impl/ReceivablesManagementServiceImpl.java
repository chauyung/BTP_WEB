package nccc.btp.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import liquibase.repackaged.org.apache.commons.text.StringEscapeUtils;
import nccc.btp.dto.NcccNssfvoueRecDto;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccAccountingList;
import nccc.btp.entity.NcccNssfvoueHeaderBatch;
import nccc.btp.entity.NcccNssfvoueInvoice;
import nccc.btp.entity.NcccNssfvoueRecData;
import nccc.btp.entity.NcccNssfvoueRecDataId;
import nccc.btp.entity.NcccNssfvoueRecHeader;
import nccc.btp.entity.SapNssfvoueRecStatus;
import nccc.btp.ftp.FtpsUtil;
import nccc.btp.repository.NcccAccountingListRepository;
import nccc.btp.repository.NcccNssfvoueHeaderBatchRepository;
import nccc.btp.repository.NcccNssfvoueInvoiceRepository;
import nccc.btp.repository.NcccNssfvoueRecDataRepository;
import nccc.btp.repository.NcccNssfvoueRecHeaderRepository;
import nccc.btp.repository.SapNssfvoueRecStatusRepository;
import nccc.btp.rfc.SapUtil;
import nccc.btp.rfc.T202;
import nccc.btp.rfc.TInput;
import nccc.btp.rfc.ZHeader;
import nccc.btp.rfc.ZItem;
import nccc.btp.rfc.ZReturn;
import nccc.btp.service.ReceivablesManagementService;
import nccc.btp.util.DateUtil;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.ReceivablesManagementQueryVo;
import nccc.btp.vo.ReceivablesManagementVo;

@Service
@Transactional
public class ReceivablesManagementServiceImpl implements ReceivablesManagementService {

  protected static Logger LOG = LoggerFactory.getLogger(ReceivablesManagementServiceImpl.class);

  @Autowired
  private NcccNssfvoueHeaderBatchRepository ncccNssfvoueHeaderBatchRepository;

  @Autowired
  private NcccAccountingListRepository ncccAccountingListRepository;

  @Autowired
  private NcccNssfvoueRecHeaderRepository ncccNssfvoueRecHeaderRepository;

  @Autowired
  private NcccNssfvoueRecDataRepository ncccNssfvoueRecDataRepository;

  @Autowired
  private SapNssfvoueRecStatusRepository sapNssfvoueRecStatusRepository;

  @Autowired
  private NcccNssfvoueInvoiceRepository ncccNssfvoueInvoiceRepository;

  @Autowired
  private SapUtil sapUtil;

  @Autowired
  @Qualifier("nccFtpsUtil")
  private FtpsUtil ftpsUtilNCC;

  @Autowired
  @Qualifier("isdFtpsUtil")
  private FtpsUtil ftpsUtilISD;

  @Value("${ftp.NCCFTP.path}")
  private String nccFtpPath;

  @Value("${ftp.ISDFTP.path}")
  private String isdFtpPath;

  // 檔案每行最小長度
  private static final int RECORD_MIN_LENGTH = 200;

  // 檔案每行最大長度
  private static final int RECORD_MAX_LENGTH = 203;

  // 編碼
  private static final Charset CHARSET = Charset.forName("MS950");

  // 美金會計科目
  private static final List<String> FOREIGN_AMT_IDS =
      Arrays.asList("11010245", "11010213", "11010208");

  // 進項稅會計科目
  private static final List<String> INPUT_TAX_IDS = Arrays.asList("11110601", "11110602");

  // 銷項稅會計科目
  private static final List<String> OUTPUT_TAX_IDS =
      Arrays.asList("21080601", "21080602", "21080603");

  @Override
  public List<NcccNssfvoueHeaderBatch> getNssfvoueHeaderBatch() {
    return ncccNssfvoueHeaderBatchRepository.findAll((Sort.by("nssfvoueHeaderBatch")));
  }

  @Override
  public List<NcccAccountingList> getNcccAccountingList() {
    return ncccAccountingListRepository.findAll((Sort.by("subject")));
  }

  @Override
  public List<ReceivablesManagementVo> query(ReceivablesManagementQueryVo queryVo) {
    Specification<NcccNssfvoueRecHeader> spec = (root, query, cb) -> {
      // 非 count/exists 查詢時做 fetch join，並 distinct 防重
      if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
        root.fetch("status", JoinType.LEFT);
        query.distinct(true);
      }
      List<Predicate> predicates = new ArrayList<>();
      if (queryVo.getNssfvoueHeaderBatch() != null && !queryVo.getNssfvoueHeaderBatch().isEmpty()) {
        predicates.add(
            cb.like(root.get("nssfvoueHeaderBatch"), "%" + queryVo.getNssfvoueHeaderBatch() + "%"));
      }
      if (queryVo.getNssfvoueDateStart() != null && !queryVo.getNssfvoueDateStart().isEmpty()) {
        predicates
            .add(cb.greaterThanOrEqualTo(root.get("nssfvoueDate"), queryVo.getNssfvoueDateStart()));
      }
      if (queryVo.getNssfvoueDateEnd() != null && !queryVo.getNssfvoueDateEnd().isEmpty()) {
        predicates
            .add(cb.lessThanOrEqualTo(root.get("nssfvoueDate"), queryVo.getNssfvoueDateEnd()));
      }
      if (queryVo.getCreateDateStart() != null) {
        predicates
            .add(cb.greaterThanOrEqualTo(root.get("createDate"), queryVo.getCreateDateStart()));
      }
      if (queryVo.getCreateDateEnd() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("createDate"), queryVo.getCreateDateEnd()));
      }
      if (queryVo.getAssignDateStart() != null) {
        predicates
            .add(cb.greaterThanOrEqualTo(root.get("assignDate"), queryVo.getAssignDateStart()));
      }
      if (queryVo.getAssignDateEnd() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("assignDate"), queryVo.getAssignDateEnd()));
      }
      if (queryVo.getReviewDateStart() != null) {
        predicates
            .add(cb.greaterThanOrEqualTo(root.get("reviewDate"), queryVo.getReviewDateStart()));
      }
      if (queryVo.getReviewDateEnd() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("reviewDate"), queryVo.getReviewDateEnd()));
      }
      if (queryVo.getAssignUser() != null && !queryVo.getAssignUser().isEmpty()) {
        predicates.add(cb.like(root.get("assignUser"), "%" + queryVo.getAssignUser() + "%"));
      }
      if (queryVo.getReviewUser() != null && !queryVo.getReviewUser().isEmpty()) {
        predicates.add(cb.like(root.get("reviewUser"), "%" + queryVo.getReviewUser() + "%"));
      }
      if (queryVo.getAssignment() != null && !queryVo.getAssignment().equalsIgnoreCase("all")
          && !queryVo.getAssignment().isEmpty()) {
        predicates.add(cb.equal(root.get("assignment"), queryVo.getAssignment()));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
    Sort sort = Sort.by(Sort.Direction.ASC, "nssfvoueHeaderBatch");
    List<NcccNssfvoueRecHeader> headers = ncccNssfvoueRecHeaderRepository.findAll(spec, sort);

    List<ReceivablesManagementVo> voList = new ArrayList<>(headers.size());
    for (NcccNssfvoueRecHeader h : headers) {
      ReceivablesManagementVo vo = new ReceivablesManagementVo();
      vo.setNcccNssfvoueRecHeader(h);
      vo.setSapNssfvoueRecStatus(
          h.getStatus() != null ? h.getStatus() : new SapNssfvoueRecStatus());
      voList.add(vo);
    }
    return voList;
  }

  @Override
  public List<NcccNssfvoueRecData> findDetailsByBatch(String nssfvoueDataBatch) {
    return ncccNssfvoueRecDataRepository.findByIdNssfvoueDataBatch(nssfvoueDataBatch);
  }

  @Override
  public List<ReceivablesManagementVo> findAll() {
    List<NcccNssfvoueRecHeader> ncccNssfvoueRecHeaderList =
        ncccNssfvoueRecHeaderRepository.findAll(Sort.by(Sort.Direction.ASC, "nssfvoueHeaderBatch"));
    List<NcccNssfvoueRecData> ncccNssfvoueRecDataList =
        ncccNssfvoueRecDataRepository.findAll(Sort.by(Sort.Direction.ASC, "id.nssfvoueDataBatch"));
    Map<String, NcccNssfvoueRecHeader> headerMap = new HashMap<>();
    // 建立header映射 (Map)，以HeaderBatch作為 key
    for (NcccNssfvoueRecHeader element : ncccNssfvoueRecHeaderList) {
      headerMap.put(element.getNssfvoueHeaderBatch(), element);
    }
    Map<String, ReceivablesManagementVo> voMap = new HashMap<>();
    // 使用 Map 統整資料，key 為HeaderBatch，value 為 nssfvoueHeaderBatch
    for (NcccNssfvoueRecData element : ncccNssfvoueRecDataList) {
      String dataBatch = element.getId().getNssfvoueDataBatch();
      if (!voMap.containsKey(dataBatch)) {
        NcccNssfvoueRecHeader header = headerMap.get(dataBatch);
        if (header != null) {
          voMap.put(dataBatch, new ReceivablesManagementVo(header));
          voMap.get(dataBatch).setSapNssfvoueRecStatus(sapNssfvoueRecStatusRepository
              .findById(dataBatch).orElse(new SapNssfvoueRecStatus()));
        } else {
          continue;
        }
      }
      voMap.get(dataBatch).addNcccNssfvoueRecData(element);
    }
    // 將統整後的結果轉換成 List
    return new ArrayList<>(voMap.values());
  }

  @Override
  public List<ReceivablesManagementVo> save(ReceivablesManagementVo receivablesManagementVo) {
    initHeaderAndData(receivablesManagementVo);
    NcccNssfvoueRecHeader ncccNssfvoueRecHeader =
        receivablesManagementVo.getNcccNssfvoueRecHeader();
    List<NcccNssfvoueRecData> ncccNssfvoueRecDataList =
        receivablesManagementVo.getNcccNssfvoueRecDataList();
    NcccUserDto user = SecurityUtil.getCurrentUser();
    LocalDate now = LocalDate.now();
    ncccNssfvoueRecHeader.setCreateUser(user.getHrAccount());
    ncccNssfvoueRecHeader.setCreateDate(now);
    ncccNssfvoueRecHeaderRepository.save(ncccNssfvoueRecHeader);
    ncccNssfvoueRecDataRepository.saveAll(ncccNssfvoueRecDataList);
    ReceivablesManagementQueryVo queryVo = new ReceivablesManagementQueryVo();
    queryVo.setNssfvoueHeaderBatch(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch());
    return query(queryVo);
  }

  @Override
  public List<ReceivablesManagementVo> add(ReceivablesManagementVo receivablesManagementVo) {
    NcccNssfvoueRecHeader ncccNssfvoueRecHeader =
        receivablesManagementVo.getNcccNssfvoueRecHeader();
    if (ncccNssfvoueRecHeaderRepository
        .existsById(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch())) {
      throw new RuntimeException(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch() + ": 已存在");
    }
    return save(receivablesManagementVo);
  }

  @Override
  public List<ReceivablesManagementVo> edit(ReceivablesManagementVo receivablesManagementVo) {
    if (!ncccNssfvoueRecHeaderRepository
        .existsById(receivablesManagementVo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch())) {
      throw new RuntimeException(
          receivablesManagementVo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch() + ": 不存在");
    }
    initHeaderAndData(receivablesManagementVo);
    NcccNssfvoueRecHeader ncccNssfvoueRecHeader =
        receivablesManagementVo.getNcccNssfvoueRecHeader();
    List<NcccNssfvoueRecData> ncccNssfvoueRecDataList =
        receivablesManagementVo.getNcccNssfvoueRecDataList();
    NcccUserDto user = SecurityUtil.getCurrentUser();
    LocalDate now = LocalDate.now();
    ncccNssfvoueRecHeader.setUpdateUser(user.getHrAccount());
    ncccNssfvoueRecHeader.setUpdateDate(now);
    ncccNssfvoueRecHeaderRepository.save(ncccNssfvoueRecHeader);
    ncccNssfvoueRecDataRepository.deleteByDataBatch(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch());
    ncccNssfvoueRecDataRepository.saveAll(ncccNssfvoueRecDataList);
    ReceivablesManagementQueryVo queryVo = new ReceivablesManagementQueryVo();
    queryVo.setNssfvoueHeaderBatch(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch());
    return query(queryVo);
  }

  @Override
  public String delete(List<String> nssfvoueHeaderBatchList) {
    for (String nssfvoueHeaderBatch : nssfvoueHeaderBatchList) {
      if (!ncccNssfvoueRecHeaderRepository.existsById(nssfvoueHeaderBatch)) {
        throw new RuntimeException(nssfvoueHeaderBatch + " not found");
      }
      ncccNssfvoueRecHeaderRepository.deleteById(nssfvoueHeaderBatch);
      ncccNssfvoueRecDataRepository.deleteByDataBatch(nssfvoueHeaderBatch);
      if (sapNssfvoueRecStatusRepository.existsById(nssfvoueHeaderBatch)) {
        sapNssfvoueRecStatusRepository.deleteById(nssfvoueHeaderBatch);
      }
    }
    return "deleted successfully";
  }

  @Override
  public String getDocTypeByNssfvoueHeaderBatch(String nssfvoueHeaderBatch) {
    Optional<NcccNssfvoueHeaderBatch> ncccNssfvoueHeaderBatch =
        ncccNssfvoueHeaderBatchRepository.findById(nssfvoueHeaderBatch);
    return ncccNssfvoueHeaderBatch.get().getDocType();
  }

  @Override
  public String toSAP(List<ReceivablesManagementVo> receivablesManagementVoList) {
    int successCnt = 0;
    int errorCnt = 0;

    // 確認是否有拋轉成功過
    List<String> batchList = receivablesManagementVoList.stream()
        .map(vo -> vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch())
        .collect(Collectors.toList());
    List<SapNssfvoueRecStatus> sapNssfvoueRecStatusCheck =
        sapNssfvoueRecStatusRepository.findByTypeAndNssfvoueHeaderBatchIn("S", batchList);
    if (!sapNssfvoueRecStatusCheck.isEmpty()) {
      String msg =
          sapNssfvoueRecStatusCheck.stream().map(SapNssfvoueRecStatus::getNssfvoueHeaderBatch)
              .distinct().map(b -> b + "：傳票已拋轉SAP成功，不可再次拋轉").collect(Collectors.joining("\n"));
      throw new RuntimeException(msg);
    }

    Map<String, String> batchToDocTypeMap = new HashMap<String, String>();
    List<NcccNssfvoueHeaderBatch> ncccNssfvoueHeaderBatchList = getNssfvoueHeaderBatch();
    batchToDocTypeMap = ncccNssfvoueHeaderBatchList.stream().collect(Collectors.toMap(
        NcccNssfvoueHeaderBatch::getNssfvoueHeaderBatch, NcccNssfvoueHeaderBatch::getDocType));
    for (ReceivablesManagementVo vo : receivablesManagementVoList) {
      NcccNssfvoueRecHeader ncccNssfvoueRecHeader = vo.getNcccNssfvoueRecHeader();
      vo.setNcccNssfvoueRecDataList(ncccNssfvoueRecDataRepository
          .findByIdNssfvoueDataBatch(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch()));
      ZHeader header = buildZHeader(vo, batchToDocTypeMap);

      // 一般
      List<NcccNssfvoueRecData> normalList = new ArrayList<>();
      // 進項稅
      List<NcccNssfvoueRecData> inputTaxList = new ArrayList<>();
      // 銷項稅
      List<NcccNssfvoueRecData> outputTaxList = new ArrayList<>();

      for (NcccNssfvoueRecData data : vo.getNcccNssfvoueRecDataList()) {
        String id = data.getNssfvoueId();
        if (INPUT_TAX_IDS.contains(id)) {
          inputTaxList.add(data);
        } else if (OUTPUT_TAX_IDS.contains(id)) {
          outputTaxList.add(data);
        } else {
          normalList.add(data);
        }
      }

      final List<ZItem> normalZitems = buildNormalZItems(normalList);
      try {
        if (!inputTaxList.isEmpty()) {
          List<ZItem> zitemsForInput = new ArrayList<>(normalZitems);
          // 累加同 ID 的金額
          Map<String, NcccNssfvoueRecData> inputMap = aggregateRecData(inputTaxList);
          zitemsForInput.addAll(buildTaxZItems(inputMap));
          List<TInput> tInputs =
              buildTInputList(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch());
          Map<String, String> checkRes = sapUtil.callZAccfTwguiCheck01(tInputs);

          if (checkRes.get("E_ERROR") == null || checkRes.get("E_ERROR").isEmpty()) {
            ZReturn zreturn = sapUtil.callZcreateAccDocument(header, zitemsForInput);
            if ("S".equals(zreturn.getTYPE())) {
              sapUtil.callZAccfTwguiInsert01(zreturn.getBUKRS(), zreturn.getBELNR(),
                  zreturn.getGJAHR(), tInputs);
              recordSapStatus(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(), zreturn,
                  true);
              successCnt++;
            } else {
              recordSapStatus(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(), zreturn,
                  false);
              errorCnt++;
            }
          } else {
            recordSapCheckError(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(),
                checkRes.get("E_XBLNR") + checkRes.get("E_MSG"));
            errorCnt++;
          }
        }

        // 處理「銷項稅」路徑（邏輯同上，金額不依照相同ID加總）
        if (!outputTaxList.isEmpty()) {
          List<ZItem> zitemsForOutput = new ArrayList<>(normalZitems);
          zitemsForOutput.addAll(buildTaxZItemsNoAggregate(outputTaxList));
          List<T202> t202s = buildT202List(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch());
          Map<String, String> checkRes = sapUtil.callZAccfTwguiCheck11(t202s);

          if (checkRes.get("E_ERROR") == null || checkRes.get("E_ERROR").isEmpty()) {
            ZReturn zreturn = sapUtil.callZcreateAccDocument(header, zitemsForOutput);
            if ("S".equals(zreturn.getTYPE())) {
              sapUtil.callZAccfTwguiInsert11(zreturn.getBUKRS(), zreturn.getBELNR(),
                  zreturn.getGJAHR(), t202s);
              recordSapStatus(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(), zreturn,
                  true);
              successCnt++;
            } else {
              recordSapStatus(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(), zreturn,
                  false);
              errorCnt++;
            }
          } else {
            recordSapCheckError(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(),
                checkRes.get("E_XBLNR") + checkRes.get("E_MSG"));
            errorCnt++;
          }
        }

        if (inputTaxList.isEmpty() && outputTaxList.isEmpty()) {
          ZReturn zreturn = sapUtil.callZcreateAccDocument(header, normalZitems);
          if ("S".equals(zreturn.getTYPE())) {
            recordSapStatus(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(), zreturn, true);
            successCnt++;
          } else {
            recordSapStatus(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(), zreturn, false);
            errorCnt++;
          }
        }
      } catch (Exception e) {
        recordSapCheckError(vo.getNcccNssfvoueRecHeader().getNssfvoueHeaderBatch(), "SAP發生錯誤");
        errorCnt++;
      }
    }
    return MessageFormat.format("拋轉成功筆數 {0}，失敗筆數 {1}", successCnt, errorCnt);
  }

  @Override
  public String confirmation(List<String> batchList) {
    checkAssignment(batchList, Collections.singleton("0"), false, true, "只有空格可以確認核可");
    return updateAssignment(batchList, "1", "confirmation");
  }

  @Override
  public String confirmReturn(List<String> batchList) {
    checkAssignment(batchList, Collections.singleton("1"), false, false, "只有已確認可以確認退回");
    return updateAssignment(batchList, "0", "confirmReturn");
  }

  @Override
  public String reviewAndApproval(List<String> batchList) {
    checkAssignment(batchList, Collections.singleton("1"), true, false, "只有已確認可以覆核核可");
    return updateAssignment(batchList, "2", "reviewAndApproval");
  }

  @Override
  public String reviewAndReturn(List<String> batchList) {
    checkAssignment(batchList, new HashSet<>(Arrays.asList("2", "4")), false, false,
        "只有已覆核或拋轉失敗可以覆核退回");
    return updateAssignment(batchList, "1", "reviewAndReturn");
  }

  private void checkAssignment(List<String> batchList, Set<String> allowed, boolean checkUser,
      boolean checkAmt, String errorMsg) {

    String hrAccount = SecurityUtil.getCurrentUser().getHrAccount();

    List<NcccNssfvoueRecHeader> NcccNssfvoueRecHeaderList =
        ncccNssfvoueRecHeaderRepository.findByHeaderBatchIn(batchList);

    if (NcccNssfvoueRecHeaderList.isEmpty()) {
      throw new RuntimeException("未找到任何 底稿批號");
    }

    if (checkAmt) {
      // 判斷主檔借貸金額是否一致
      List<String> invalidAmtBtches = new ArrayList<>();
      // 判斷明細檔借貸金額是否一致
      List<String> invalidDataAmtBtches = new ArrayList<>();
      // 判斷主檔借貸金額與明細借貸金額是否一致
      List<String> invalidHeaderDataAmtBtches = new ArrayList<>();
      for (NcccNssfvoueRecHeader h : NcccNssfvoueRecHeaderList) {
        if (h.getNssfvoueCAmt().compareTo(h.getNssfvoueDAmt()) != 0) {
          invalidAmtBtches.add(h.getNssfvoueHeaderBatch());
        }
        BigDecimal totaNssfvoueCAmt = BigDecimal.ZERO;
        BigDecimal totaNssfvoueDAmt = BigDecimal.ZERO;
        for (NcccNssfvoueRecData ncccNssfvoueRecData : ncccNssfvoueRecDataRepository
            .findByIdNssfvoueDataBatch(h.getNssfvoueHeaderBatch())) {
          if (ncccNssfvoueRecData.getNssfvoueType().equals("40")) {
            totaNssfvoueCAmt =
                totaNssfvoueCAmt.add(defaultIfNull(ncccNssfvoueRecData.getNssfvoueAmt()));
          } else if (ncccNssfvoueRecData.getNssfvoueType().equals("50")) {
            totaNssfvoueDAmt =
                totaNssfvoueDAmt.add(defaultIfNull(ncccNssfvoueRecData.getNssfvoueAmt()));
          } else {
            throw new RuntimeException("批號 " + h.getNssfvoueHeaderBatch() + " 借貸別非40或50");
          }
        }
        if (totaNssfvoueCAmt.compareTo(totaNssfvoueDAmt) != 0) {
          invalidDataAmtBtches.add(h.getNssfvoueHeaderBatch());
        }
        if (h.getNssfvoueCAmt().compareTo(totaNssfvoueCAmt) != 0
            || h.getNssfvoueDAmt().compareTo(totaNssfvoueDAmt) != 0) {
          invalidHeaderDataAmtBtches.add(h.getNssfvoueHeaderBatch());
        }
      }
      if (!invalidAmtBtches.isEmpty()) {
        throw new RuntimeException("下列批號借貸金額不平 ：" + String.join("、", invalidAmtBtches));
      }
      if (!invalidDataAmtBtches.isEmpty()) {
        throw new RuntimeException("下列批號明細借貸金額不平 ：" + String.join("、", invalidDataAmtBtches));
      }
      if (!invalidHeaderDataAmtBtches.isEmpty()) {
        throw new RuntimeException(
            "下列批號主檔與明細借貸金額不一致 ：" + String.join("、", invalidHeaderDataAmtBtches));
      }
      List<NcccNssfvoueRecData> ncccNssfvoueRecDataList =
          ncccNssfvoueRecDataRepository.findByIdNssfvoueDataBatchIn(batchList);
      for (NcccNssfvoueRecData ncccNssfvoueRecData : ncccNssfvoueRecDataList) {
        if (FOREIGN_AMT_IDS.contains(ncccNssfvoueRecData.getNssfvoueId())) {
          if (ncccNssfvoueRecData.getNssfvoueUSAmt() == null
              || ncccNssfvoueRecData.getNssfvoueUSAmt().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                "批號 " + ncccNssfvoueRecData.getId().getNssfvoueDataBatch() + " 美金金額不可為空");
          }
        }
      }
    }

    Set<String> actualAssignments = new HashSet<>();
    List<String> invalidUserBatches = new ArrayList<>();

    for (NcccNssfvoueRecHeader h : NcccNssfvoueRecHeaderList) {
      actualAssignments.add(h.getAssignment());
      if (checkUser && hrAccount.equals(h.getAssignUser())) {
        invalidUserBatches.add(h.getNssfvoueHeaderBatch());
      }
    }
    if (!allowed.containsAll(actualAssignments)) {
      String actual = actualAssignments.stream().map(code -> {
        switch (code) {
          case "0":
            return "空白";
          case "1":
            return "已確認";
          case "2":
            return "已覆核";
          case "4":
            return "拋轉失敗";
          default:
            return code;
        }
      }).collect(Collectors.joining("、"));
      throw new RuntimeException(errorMsg + "（實際為：" + actual + "）");
    }
    // user 不符合
    if (!invalidUserBatches.isEmpty()) {
      throw new RuntimeException("下列批號確認人員不能與審核人員相同 ：" + String.join("、", invalidUserBatches));
    }
  }

  private String updateAssignment(List<String> batchList, String toAssign, String type) {
    NcccUserDto user = SecurityUtil.getCurrentUser();
    LocalDate now = LocalDate.now();
    int count;
    switch (type) {
      case "confirmation":
        count = ncccNssfvoueRecHeaderRepository.assignByHeaderBatchIn(batchList, toAssign,
            user.getHrAccount(), now);
        break;
      case "confirmReturn":
        count = ncccNssfvoueRecHeaderRepository.returnByHeaderBatchIn(batchList);
        break;
      case "reviewAndApproval":
        count = ncccNssfvoueRecHeaderRepository.reviewByHeaderBatchIn(batchList, toAssign,
            user.getHrAccount(), now);
        break;
      case "reviewAndReturn":
        count = ncccNssfvoueRecHeaderRepository.reviewAndReturnByHeaderBatchIn(batchList);
        break;
      default:
        throw new RuntimeException("未知更新類型");
    }
    return "簽核了 " + count + " 筆資料,請重新查詢";
  }

  private BigDecimal defaultIfNull(BigDecimal value) {
    return value == null ? BigDecimal.ZERO : value;
  }

  /** 將一般分錄轉 ZItem */
  private List<ZItem> buildNormalZItems(List<NcccNssfvoueRecData> list) {
    List<ZItem> items = new ArrayList<>();
    for (NcccNssfvoueRecData data : list) {
      ZItem zitem = new ZItem();
      // 過帳碼
      zitem.setBSCHL(data.getNssfvoueType());
      // 科目
      zitem.setNEWKO(data.getNssfvoueId());
      // 文件幣別金額
      zitem.setWRBTR(data.getNssfvoueAmt().toString());
      // 稅碼
      zitem.setMWSKZ("");
      // 稅基
      zitem.setTXBFW("");
      // 成本中心 會計科目如果是5開頭 成本中心欄位固定帶清算會計部「1F000」
      if (data.getNssfvoueId().startsWith("5")) {
        zitem.setKOSTL("1F000");
      } else {
        zitem.setKOSTL("");
      }
      if (data.getNssfvoueId().equals("11119999")) {
        zitem.setZUONR(data.getId().getNssfvoueDataBatch());
      }
      // 內文
      zitem.setSGTXT(data.getNssfvoueNote());
      // 參考碼 1
      zitem.setXREF1("");
      // 參考碼 2
      zitem.setXREF2("");
      // 參考碼 3
      if (data.getNssfvoueUSAmt() != null) {
        String usAmt = data.getNssfvoueUSAmt().setScale(2, RoundingMode.HALF_UP) // 四捨五入到小數點後 2 位
            .toPlainString();
        zitem.setXREF3(usAmt);
      }
      items.add(zitem);
    }
    return items;
  }

  private ZHeader buildZHeader(ReceivablesManagementVo vo, Map<String, String> batchToDocTypeMap) {
    NcccNssfvoueRecHeader ncccNssfvoueRecHeader = vo.getNcccNssfvoueRecHeader();
    ZHeader header = new ZHeader();
    // 來源系統
    header.setZSYSTEM("BTP");
    // 公司代碼
    header.setBUKRS("1010");
    // 文件日期
    header.setBLDAT(ncccNssfvoueRecHeader.getNssfvoueProcDate());
    // 過帳日期
    header.setBUDAT(ncccNssfvoueRecHeader.getNssfvoueDate());
    // 文件類型 (取後四碼)
    header.setBLART(batchToDocTypeMap.get(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch().substring(
        ncccNssfvoueRecHeader.getNssfvoueHeaderBatch().length() - 4,
        ncccNssfvoueRecHeader.getNssfvoueHeaderBatch().length())));
    // 幣別
    header.setWAERS("TWD");
    // 文件表頭的參考碼 1 內部
    header.setXREF1_HD(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch());
    return header;
  }

  /** 同 ID 累加金額 */
  private Map<String, NcccNssfvoueRecData> aggregateRecData(List<NcccNssfvoueRecData> list) {
    Map<String, NcccNssfvoueRecData> map = new LinkedHashMap<>();
    for (NcccNssfvoueRecData data : list) {
      String id = data.getNssfvoueId();
      BigDecimal amt = defaultIfNull(data.getNssfvoueAmt());
      BigDecimal usAmt = defaultIfNull(data.getNssfvoueUSAmt());
      if (map.containsKey(id)) {
        NcccNssfvoueRecData e = map.get(id);
        e.setNssfvoueAmt(e.getNssfvoueAmt().add(amt));
        e.setNssfvoueUSAmt(e.getNssfvoueUSAmt().add(usAmt));
      } else {
        NcccNssfvoueRecData copy = new NcccNssfvoueRecData();
        copy.setNssfvoueId(id);
        copy.setNssfvoueAmt(amt);
        copy.setNssfvoueUSAmt(usAmt);
        copy.setNssfvoueNote(data.getNssfvoueNote());
        copy.setNssfvoueType(data.getNssfvoueType());
        map.put(id, copy);
      }
    }
    return map;
  }

  // 把合併後的稅額分錄轉成 ZItem，並帶入指定的稅碼 (進項用)
  private List<ZItem> buildTaxZItems(Map<String, NcccNssfvoueRecData> map) {

    // 稅基=稅額除以0.05 等於乘以20(1 / 0.05)
    BigDecimal multiplier = new BigDecimal("20");
    List<ZItem> items = new ArrayList<>();
    for (NcccNssfvoueRecData data : map.values()) {
      ZItem zitem = new ZItem();
      // 過帳碼
      zitem.setBSCHL(data.getNssfvoueType());
      // 科目
      zitem.setNEWKO(data.getNssfvoueId());
      // 文件幣別金額
      zitem.setWRBTR(data.getNssfvoueAmt().toString());
      // 稅碼
      switch (data.getNssfvoueId()) {
        case "11110601":
          zitem.setMWSKZ("V3");
          break;
        case "11110602":
          zitem.setMWSKZ("W3");
          break;
        default:
          zitem.setMWSKZ("");
          break;
      }
      // 稅基
      if (INPUT_TAX_IDS.contains(data.getNssfvoueId())) {
        zitem.setTXBFW(data.getNssfvoueAmt().multiply(multiplier).toString());
      } else {
        zitem.setTXBFW("");
      }
      // 內文
      zitem.setSGTXT(data.getNssfvoueNote());
      // 參考碼 3
      if (data.getNssfvoueUSAmt() != null) {
        String usAmt = data.getNssfvoueUSAmt().setScale(2, RoundingMode.HALF_UP) // 四捨五入到小數點後 2 位
            .toPlainString();
        zitem.setXREF3(usAmt);
      }
      items.add(zitem);
    }
    return items;
  }

  // 把稅額分錄逐筆轉成 ZItem（不加總），依 ID 帶稅碼、計算稅基 (銷項用)
  private List<ZItem> buildTaxZItemsNoAggregate(List<NcccNssfvoueRecData> list) {

    BigDecimal multiplier = new BigDecimal("20"); // 稅基 = 稅額 / 0.05 = *20

    List<ZItem> items = new ArrayList<>();
    for (NcccNssfvoueRecData data : list) {
      ZItem zitem = new ZItem();
      zitem.setBSCHL(data.getNssfvoueType());
      zitem.setNEWKO(data.getNssfvoueId());
      zitem.setWRBTR(defaultIfNull(data.getNssfvoueAmt()).toString());

      // 稅碼
      switch (data.getNssfvoueId()) {
        case "21080601":
          zitem.setMWSKZ("A3");
          break;
        case "21080602":
          zitem.setMWSKZ("B3");
          break;
        case "21080603":
          zitem.setMWSKZ("C3");
          break;
        default:
          zitem.setMWSKZ("");
          break;
      }

      // 稅基
      if (OUTPUT_TAX_IDS.contains(data.getNssfvoueId())) {
        zitem.setTXBFW(defaultIfNull(data.getNssfvoueAmt()).multiply(multiplier).toString());
      } else {
        zitem.setTXBFW("");
      }

      zitem.setSGTXT(data.getNssfvoueNote());
      if (data.getNssfvoueUSAmt() != null) {
        String usAmt = data.getNssfvoueUSAmt().setScale(2, RoundingMode.HALF_UP) // 四捨五入到小數點後 2 位
            .toPlainString();
        zitem.setXREF3(usAmt);
      }
      items.add(zitem);
    }
    return items;
  }

  /** 準備給 SAP 檢核／Insert 用的 TInput 清單 */
  private List<TInput> buildTInputList(String nssfvoueHeaderBatch) {
    List<TInput> inputs = new ArrayList<>();
    List<NcccNssfvoueInvoice> invs = ncccNssfvoueInvoiceRepository.findByXa001(nssfvoueHeaderBatch);
    for (NcccNssfvoueInvoice inv : invs) {
      TInput tInput = new TInput();
      tInput.setBLDAT(inv.getTa016());
      tInput.setVATDATE(inv.getTa016());
      tInput.setXBLNR(inv.getTa015());
      // 進項稅固定拋25 銷項稅走t202
      tInput.setZFORM_CODE("25");
      tInput.setSTCEG(inv.getTa007());
      tInput.setHWBAS(inv.getXd007());
      tInput.setHWSTE(inv.getXd008());
      tInput.setTAX_TYPE("1");
      tInput.setCUS_TYPE("");
      tInput.setAM_TYPE("1");
      inputs.add(tInput);
    }
    return inputs;
  }

  /** 準備給 SAP 檢核／Insert 用的 T202 清單 */
  private List<T202> buildT202List(String nssfvoueHeaderBatch) {
    List<T202> inputs = new ArrayList<>();
    List<NcccNssfvoueInvoice> invs = ncccNssfvoueInvoiceRepository.findByXa001(nssfvoueHeaderBatch);
    for (NcccNssfvoueInvoice inv : invs) {
      T202 t202 = new T202();
      t202.setXC003(inv.getXc003());
      t202.setXC011(inv.getXc011());
      t202.setXC012(inv.getXc012());
      t202.setXD003(inv.getXd003());
      t202.setXD004(inv.getXd004());
      t202.setXD005(inv.getXd005());
      t202.setXD006(inv.getXd006());
      t202.setXD007(inv.getXd007());
      t202.setXD008(inv.getXd008());
      t202.setXD009(inv.getXd009());
      t202.setXD011(inv.getXd011());
      t202.setXD012(inv.getXd012());
      t202.setTA007(inv.getTa007());
      t202.setTA008(inv.getTa008());
      t202.setTA015(inv.getTa015());
      t202.setTA016(inv.getTa016());
      t202.setTA201(inv.getTa201());
      t202.setTA204(inv.getTa204());
      t202.setTA205(inv.getTa205());
      t202.setXA001(inv.getXa001());
      inputs.add(t202);
    }
    return inputs;
  }

  // sap拋轉狀態 更新table
  private void recordSapStatus(String nssfvoueHeaderBatch, ZReturn zreturn, boolean success) {
    if (success) {
      ncccNssfvoueRecHeaderRepository.updateAssignmentByBatch(nssfvoueHeaderBatch, "3");
    } else {
      ncccNssfvoueRecHeaderRepository.updateAssignmentByBatch(nssfvoueHeaderBatch, "4");
    }
    SapNssfvoueRecStatus sapNssfvoueRecStatus = new SapNssfvoueRecStatus();
    sapNssfvoueRecStatus.setNssfvoueHeaderBatch(nssfvoueHeaderBatch);
    sapNssfvoueRecStatus.setType(zreturn.getTYPE());
    sapNssfvoueRecStatus.setBukrs(zreturn.getBUKRS());
    sapNssfvoueRecStatus.setBelnr(zreturn.getBELNR());
    sapNssfvoueRecStatus.setGjahr(zreturn.getGJAHR());
    sapNssfvoueRecStatus.setMessage(zreturn.getMESSAGE());
    sapNssfvoueRecStatus.setSapTime(LocalDateTime.now());
    sapNssfvoueRecStatusRepository.save(sapNssfvoueRecStatus);
  }

  // sap 失敗 更新table
  private void recordSapCheckError(String nssfvoueHeaderBatch, String messqge) {
    ncccNssfvoueRecHeaderRepository.updateAssignmentByBatch(nssfvoueHeaderBatch, "4");
    SapNssfvoueRecStatus sapNssfvoueRecStatus = new SapNssfvoueRecStatus();
    sapNssfvoueRecStatus.setNssfvoueHeaderBatch(nssfvoueHeaderBatch);
    sapNssfvoueRecStatus.setType("E");
    sapNssfvoueRecStatus.setMessage(messqge);
    sapNssfvoueRecStatus.setSapTime(LocalDateTime.now());
    sapNssfvoueRecStatusRepository.save(sapNssfvoueRecStatus);
  }

  // 初始化傳票表頭與明細
  private ReceivablesManagementVo initHeaderAndData(
      ReceivablesManagementVo receivablesManagementVo) {
    NcccNssfvoueRecHeader ncccNssfvoueRecHeader =
        receivablesManagementVo.getNcccNssfvoueRecHeader();
    if (ncccNssfvoueRecHeader.getNssfvoueCAmt().signum() >= 0) {
      ncccNssfvoueRecHeader.setNssfvoueCSign("+");
    } else {
      ncccNssfvoueRecHeader.setNssfvoueCSign("-");
      ncccNssfvoueRecHeader.setNssfvoueCAmt(ncccNssfvoueRecHeader.getNssfvoueCAmt().abs());
    }
    if (ncccNssfvoueRecHeader.getNssfvoueDAmt().signum() >= 0) {
      ncccNssfvoueRecHeader.setNssfvoueDSign("+");
    } else {
      ncccNssfvoueRecHeader.setNssfvoueDSign("-");
      ncccNssfvoueRecHeader.setNssfvoueDAmt(ncccNssfvoueRecHeader.getNssfvoueDAmt().abs());
    }
    ncccNssfvoueRecHeader.setNssfvoueRecHeader("2");
    ncccNssfvoueRecHeader.setNssfvoueHeaderSeq("0001");
    List<NcccNssfvoueRecData> ncccNssfvoueRecDataList =
        receivablesManagementVo.getNcccNssfvoueRecDataList();
    for (NcccNssfvoueRecData ncccNssfvoueRecData : ncccNssfvoueRecDataList) {
      if (ncccNssfvoueRecData.getNssfvoueAmt() != null) {
        ncccNssfvoueRecData
            .setNssfvoueSign(ncccNssfvoueRecData.getNssfvoueAmt().signum() >= 0 ? "+" : "-");
      }
      if (ncccNssfvoueRecData.getNssfvoueUSAmt() != null) {
        ncccNssfvoueRecData
            .setNssfvoueUSSign(ncccNssfvoueRecData.getNssfvoueUSAmt().signum() >= 0 ? "+" : "-");
      }
      ncccNssfvoueRecData.setNssfvoueRecHeader("1");
      ncccNssfvoueRecData.setNssfvoueDataSeq("0001");
    }
    ncccNssfvoueRecHeader.setAssignment("0");
    return receivablesManagementVo;
  }

  @Override
  public String importFromFtp(String fileName) throws Exception {
    // 決定來源路徑與 FtpsUtil
    FtpsUtil util = fileName.startsWith("A") ? ftpsUtilNCC : ftpsUtilISD;
    String path = (fileName.startsWith("A") ? nccFtpPath : isdFtpPath) + fileName;

    // 以 byte 為單位讀取，每筆固定 RECORD_LENGTH bytes
    Map<String, List<byte[]>> groups = new LinkedHashMap<>();
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(util.readFileStream(path), StandardCharsets.ISO_8859_1))) {
      String line;
      while ((line = br.readLine()) != null) {
        byte[] rec = line.getBytes(StandardCharsets.ISO_8859_1);
        if (rec.length < RECORD_MIN_LENGTH || rec.length > RECORD_MAX_LENGTH) {
          // 若真遇到長度不足／過長
          throw new IOException(String.format("第 %d 行byte長度 %d < %d 或 > %d",
              groups.values().stream().mapToInt(List::size).sum() + 1, rec.length,
              RECORD_MIN_LENGTH, RECORD_MAX_LENGTH));
        }
        // 取第 2~12 byte 作為 batch key
        String batchKey = new String(rec, 1, 11, CHARSET).trim();
        groups.computeIfAbsent(batchKey, k -> new ArrayList<>()).add(rec);
      }
    } catch (IOException e) {
      String msg = String.format("檔案 %s 處理失敗：%s", path, e.getMessage());
      LOG.error(msg, e);
      throw new Exception(msg, e);
    }

    cutFileToSave(groups, fileName);
    return StringEscapeUtils.escapeHtml4(fileName) + ": 已解析完畢";
  }

  private void cutFileToSave(Map<String, List<byte[]>> groups, String fileName) {
    List<NcccNssfvoueRecDto> dtoList = new ArrayList<>();
    for (List<byte[]> records : groups.values()) {
      NcccNssfvoueRecDto dto = new NcccNssfvoueRecDto();
      for (byte[] rec : records) {
        char type = (char) rec[0];
        // NSSFVOUE-REC-DATA
        if (type == '1') {
          // 底稿批號(key-1)
          String nssfvoueDataBatch = new String(rec, 1, 11, CHARSET).trim();
          // 底稿序號(key-3)
          String nssfvoueSeqNum = new String(rec, 16, 4, CHARSET).trim();
          NcccNssfvoueRecData ncccNssfvoueRecData = new NcccNssfvoueRecData();
          ncccNssfvoueRecData.setId(new NcccNssfvoueRecDataId(nssfvoueDataBatch, nssfvoueSeqNum));
          ncccNssfvoueRecData.setSourceFile(fileName);
          // Record Header
          ncccNssfvoueRecData.setNssfvoueRecHeader(new String(rec, 0, 1, CHARSET).trim());
          ncccNssfvoueRecData.setNssfvoueDataSeq(new String(rec, 12, 4, CHARSET).trim());
          String typeCode = new String(rec, 20, 2, CHARSET).trim();
          ncccNssfvoueRecData.setNssfvoueType("1".equals(typeCode) ? "40" : "50");
          ncccNssfvoueRecData.setNssfvoueId(new String(rec, 22, 20, CHARSET).trim());
          ncccNssfvoueRecData.setNssfvoueSFid(new String(rec, 42, 10, CHARSET).trim());
          ncccNssfvoueRecData.setNssfvoueFid(new String(rec, 52, 10, CHARSET).trim());
          ncccNssfvoueRecData.setFiller(new String(rec, 62, 6, CHARSET).trim());
          ncccNssfvoueRecData.setNssfvoueSign(new String(rec, 68, 1, CHARSET).trim());
          String amt = new String(rec, 69, 14, CHARSET).trim();
          ncccNssfvoueRecData.setNssfvoueAmt(amt.isEmpty() ? BigDecimal.ZERO : new BigDecimal(amt));
          ncccNssfvoueRecData.setNssfvoueSubSubjName1(new String(rec, 83, 10, CHARSET).trim());
          ncccNssfvoueRecData.setNssfvoueSubSubjName2(new String(rec, 93, 10, CHARSET).trim());
          String[] notes = splitMS950Field(rec, 103, 40, 30);
          String note = notes[0];
          String note2 = notes[1];
          ncccNssfvoueRecData.setNssfvoueNote(note + note2);
          if (FOREIGN_AMT_IDS.contains(ncccNssfvoueRecData.getNssfvoueId())) {
            ncccNssfvoueRecData.setNssfvoueUSSign(new String(rec, 173, 1, CHARSET).trim());
            String usAmtStr = new String(rec, 174, 10, CHARSET).trim();
            ncccNssfvoueRecData.setNssfvoueUSAmt(usAmtStr.isEmpty() ? null
                : new BigDecimal(new String(rec, 174, 10, CHARSET).trim()).movePointLeft(2)
                    .setScale(2, RoundingMode.HALF_UP));
          }
          dto.addNcccNssfvoueRecData(ncccNssfvoueRecData);

        }
        // NSSFVOUE-REC-HEADER
        else if (type == '2') {
          NcccNssfvoueRecHeader ncccNssfvoueRecHeader = new NcccNssfvoueRecHeader();
          ncccNssfvoueRecHeader.setNssfvoueRecHeader(new String(rec, 0, 1, CHARSET).trim());
          ncccNssfvoueRecHeader.setNssfvoueHeaderBatch(new String(rec, 1, 11, CHARSET).trim());
          ncccNssfvoueRecHeader.setNssfvoueHeaderSeq(new String(rec, 12, 4, CHARSET).trim());
          ncccNssfvoueRecHeader.setNssfvoueDate(
              DateUtil.convertRocToGregorian(new String(rec, 16, 7, CHARSET).trim()));
          ncccNssfvoueRecHeader.setNssfvoueCSign(new String(rec, 23, 1, CHARSET).trim());
          String cAmt = new String(rec, 24, 14, CHARSET).trim();
          ncccNssfvoueRecHeader
              .setNssfvoueCAmt(cAmt.isEmpty() ? BigDecimal.ZERO : new BigDecimal(cAmt));
          ncccNssfvoueRecHeader.setNssfvoueDSign(new String(rec, 38, 1, CHARSET).trim());
          String dAmt = new String(rec, 39, 14, CHARSET).trim();
          ncccNssfvoueRecHeader
              .setNssfvoueDAmt(dAmt.isEmpty() ? BigDecimal.ZERO : new BigDecimal(dAmt));
          ncccNssfvoueRecHeader.setNssfvoueProcDate(
              DateUtil.convertRocToGregorian(new String(rec, 53, 7, CHARSET).trim()));
          ncccNssfvoueRecHeader.setFiller(new String(rec, 60, 140, CHARSET).trim());
          NcccUserDto user = SecurityUtil.getCurrentUser();
          ncccNssfvoueRecHeader.setCreateUser(user.getHrAccount());
          ncccNssfvoueRecHeader.setCreateDate(LocalDate.now());
          ncccNssfvoueRecHeader.setAssignment("0");
          dto.setNcccNssfvoueRecHeader(ncccNssfvoueRecHeader);
        }
      }
      dtoList.add(dto);
    }


    for (NcccNssfvoueRecDto dto : dtoList) {
      NcccNssfvoueRecHeader ncccNssfvoueRecHeader = dto.getNcccNssfvoueRecHeader();
      List<NcccNssfvoueRecData> ncccNssfvoueRecDataList = dto.getNcccNssfvoueRecDataList();

      boolean exists = ncccNssfvoueRecHeaderRepository
          .existsById(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch());
      if (!exists) {
        ncccNssfvoueRecHeaderRepository.save(ncccNssfvoueRecHeader);
        ncccNssfvoueRecDataRepository.saveAll(ncccNssfvoueRecDataList);

      } else {
        String belnr = sapNssfvoueRecStatusRepository
            .findBelnrByNssfvoueHeaderBatch(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch());
        if (belnr != null && !"$".equals(belnr)) {
          // 底稿批號重複但SAP文件號碼存在或簽核狀態已核可時，該底稿批號資料跳過
        } else {
          NcccNssfvoueRecHeader old = ncccNssfvoueRecHeaderRepository
              .findById(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch()).orElse(null);
          // 底稿批號重複但SAP文件號碼不存在與簽核狀態不等於核可，先將該筆底稿批號Delete後Insert
          // 只有空白 可以覆蓋
          if (old != null && "0".equals(old.getAssignment())) {
            ncccNssfvoueRecHeaderRepository
                .deleteById(ncccNssfvoueRecHeader.getNssfvoueHeaderBatch());
            ncccNssfvoueRecHeaderRepository.save(ncccNssfvoueRecHeader);
            // 把同樣檔名的刪除 再新增 防止副檔名不一致 把fileName截到. 然後用like刪除
            String fileNameLike = fileName.substring(0, fileName.lastIndexOf('.') + 1);
            ncccNssfvoueRecDataRepository.deleteByDataBatchAndSourceFile(
                ncccNssfvoueRecHeader.getNssfvoueHeaderBatch(), fileNameLike);
            ncccNssfvoueRecDataRepository.saveAll(ncccNssfvoueRecDataList);

          }
        }
      }
    }
  }

  /**
   * 將 Big5 編碼的欄位安全切成兩段，避免中文字被切半。
   *
   * @param rec 整筆紀錄的 byte 陣列
   * @param offset 欄位起始位移
   * @param firstBytes 第一段最大 byte 數
   * @param secondBytes 第二段最大 byte 數
   * @return [第一段字串, 第二段字串]
   */
  private String[] splitMS950Field(byte[] rec, int offset, int firstBytes, int secondBytes) {
    int length = firstBytes + secondBytes; // 總長度
    byte[] slice = new byte[length];
    System.arraycopy(rec, offset, slice, 0, length);

    // 建立合法切點
    List<Integer> cutPoints = new ArrayList<>();
    int i = 0;
    while (i < slice.length) {
      cutPoints.add(i);
      int b = slice[i] & 0xFF;
      if (b >= 0x81 && b <= 0xFE) {
        i += 2; // 中文雙位元組
      } else {
        i += 1; // 英文、數字、標點單位元組
      }
    }
    cutPoints.add(slice.length);

    // 找最接近 firstBytes 的合法切點
    int cut1 = 0;
    for (int point : cutPoints) {
      if (point <= firstBytes) {
        cut1 = point;
      } else {
        break;
      }
    }

    // 解碼兩段
    String part1 = new String(slice, 0, cut1, CHARSET).trim();
    String part2 = new String(slice, cut1, slice.length - cut1, CHARSET).trim();

    return new String[] {part1, part2};
  }

}
