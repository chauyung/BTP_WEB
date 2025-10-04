package nccc.btp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板key
 */
public enum ProcessDefinitionKey {

  DOMESTIC_BUSINESS_TRIP_GENERAL_COLLEAGUES("DomesticBusinessTrip-GeneralColleagues",
      "差旅申請單-國內出差(一般同仁)"), DOMESTIC_BUSINESS_TRIP_SUPERVISOR_AND_ABOVE(
          "DomesticBusinessTrip-SupervisorAndAbove",
          "差旅申請單-國內出差(主管以上)"), OVERSEAS_BUSINESS_TRIP_GENERAL_COLLEAGUES(
              "OverseasBusinessTrip-GeneralColleagues",
              "差旅申請單-國外出差(一般同仁)"), OVERSEAS_BUSINESS_TRIP_SUPERVISOR_AND_ABOVE(
                  "OverseasBusinessTrip-SupervisorAndAbove", "差旅申請單-國外出差(主管以上)"),
  BUDGET_BUDGETMANAGEMENT("Budget-budgetManagement", "提列費用"),
  BUDGET_BUDGETALLOCATION("Budget-budgetAllocation", "預算調撥"),
  BUDGET_ALLOCATIONADJ("Budget-allocationAdj", "權責分攤"),
  BUDGET_BUDGETRETENTION("Budget-budgetRetention", "預算保留"),
  APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_EXCLUSIVE("ApplicationExpenses-GeneralExpensesRequestPayment-Exclusive","費用申請單-限定科目"),
  APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_GENERAL("ApplicationExpenses-GeneralExpensesRequestPayment-General","費用申請單-一般費用"),
  APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_SOCIAL("ApplicationExpenses-GeneralExpensesRequestPayment-Social","費用申請單-交際費"),
  APPLICATIONEXPENSES_PETTYCASH("ApplicationExpenses-PettyCash","費用申請單-零用金"),
  APPLICATIONEXPENSES_PREPAYMENTWITHAPPROVAL("ApplicationExpenses-PrepaymentWithApproval","費用申請單-預付單（無簽呈）"),
  APPLICATIONEXPENSES_PREPAYMENTWITHOUTAPPROVAL("ApplicationExpenses-PrepaymentWithoutApproval","費用申請單-預付單（簽呈）"),
  APPLICATIONEXPENSES_UTILITYEXPENSES("ApplicationExpenses-UtilityExpenses",
      "費用申請單-公用事業費/零用金撥補/董監事及研發委員"), PURCHASEREQUISTION_GENERAL("PurchaseRequistion-General",
          "全體(一般未滿30萬)"), PURCHASEREQUISTION_COMPUTERDEMANDORDERREQUISITION(
              "PurchaseRequistion-ComputerDemandOrderRequisition",
              "電腦需求單請購(線下作業紙本)"), PURCHASEREQUISTION_IOTEQUIPMENT("PurchaseRequistion-IotEquipment",
                  "購置物聯網設備(一般未滿30萬)"), PURCHASEREQUISTION_ITPERIPHERAL(
                      "PurchaseRequistion-ItPeripheral",
                      "購置電腦周邊軟硬體(一般未滿30萬)"), PURCHASEREQUISTION_SUBMITPURCHASEREQUEST(
                          "PurchaseRequistion-SubmitPurchaseRequest", "簽呈請購(電子化公文系統)");

  private final String key;
  private final String displayName; // 中文名稱

  // 快取表
  private static final Map<String, ProcessDefinitionKey> KEY_MAP = new HashMap<>();

  static {
    for (ProcessDefinitionKey pd : values()) {
      KEY_MAP.put(pd.key, pd);
    }
  }

  ProcessDefinitionKey(String key, String displayName) {
    this.key = key;
    this.displayName = displayName;
  }

  public String getKey() {
    return key;
  }

  public String getDisplayName() {
    return displayName;
  }

  /**
   * 字串反查 enum
   */
  public static ProcessDefinitionKey fromKey(String key) {
    ProcessDefinitionKey pd = KEY_MAP.get(key);
    if (pd == null) {
      throw new IllegalArgumentException("Unknown process definition key: " + key);
    }
        return pd;
    }

    /**
     * 直接從 key 取得中文名稱
     */
    public static String getDisplayNameByKey(String key) {
      ProcessDefinitionKey pd = KEY_MAP.get(key);
      return (pd != null) ? pd.getDisplayName() : null;
    }
}