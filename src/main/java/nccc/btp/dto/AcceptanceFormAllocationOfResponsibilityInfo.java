package nccc.btp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.*;

/**
 * 權責分攤資料模型
 */
public class AcceptanceFormAllocationOfResponsibilityInfo {

  /**
   * 驗收單單號
   */
  public String AcceptanceFormNo;

  /**
   * 主鍵
   */
  public Long Id;

  /*
   * 品號
   */
  public String ItemCode;

  /*
   * 品名
   */
  public String ItemName;

  /*
   * 起始月份
   */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  public LocalDate BeginMonth;

  /*
   * 期數
   */
  public Integer Period;

  /**
   * 分攤明細
   */
  public List<AcceptanceFormAllocationOfResponsibilityDetailInfo> Allocations;

}
