/**
 * 
 */
package nccc.btp.enums;


/**
 *  流程使用 key=設定流程人員變數, code=部門代號 (經辦跟科長會同個部門)
 */
public enum AssigneeRole {

  INITIATOR("initiator",""), // 申請人
  APPROVERS("approvers",""), // 申請單位(動態流程人員)
  CURRENT_INDEX("currentIndex",""), // 動態申請單位 (計算用)
  CURRENT_APPROVER("currentApprover",""), // 動態申請單位 (當前簽核人員)
  APPLICANT_SUPERVISOR("applicantSupervisor",""), // 申請單位主管
  CASHIER("cashier","C402"), // 出納(零用金管理人)
  COST_CLERK("costClerk", "F403"), // 成本經辦
  COST_SECTION_CHIEF("costSectionChief","F403"), // 成本科長
  ACCOUNTING_CLERK("accountingClerk", "F404"), // 會計經辦
  ACCOUNTING_SECTION_CHIEF("accountingSectionChief","F404"), // 會計科長
  ACCOUNTING_MANAGER("accountingManager","F101"), // 會計經理
  ACCOUNTING_ASSOCIATE("accountingAssociate","F200"), // 會計協理
  ACCOUNTING_SENIOR_ASSOCIATE("accountingSeniorAssociate","F100"), // 會計資協
  IT_CLERK("itClerk", ""), // 資訊服務部經辦
  IT_SECTION_CHIEF("itSectionChief", ""), // 資訊服務部科長
  IT_MANAGER("itManager", ""), // 資訊服務部經理
  IT_ASSOCIATE("itAssociate", ""), // 資訊服務部協理
  IT_SENIOR_ASSOCIATE("itSeniorAssociate", ""), // 資訊服務部資協
  PURCHASE_CLERK("purchaseClerk", ""), // 採購經辦
  PURCHASE_SECTION_CHIEF("purchaseSectionChief", ""), // 採購科長
  PURCHASE_MANAGER("purchaseManager", ""), // 採購經理
  PURCHASE_ASSOCIATE("purchaseAssociate", ""), // 採購協理
  PURCHASE_SENIOR_ASSOCIATE("purchaseSeniorAssociate", ""), // 採購資協
  AUDIT_OFFICE("auditOffice","1200"), // 稽核室
  ADMINISTRATIVE_MANAGER("administrativeManager","C100"), // 行管部主管
  VICE_PRESIDENT("vicePresident","1010"), // 副總經理
  PRESIDENT("president","1000"), // 總經理
  CHAIRMAN("chairman","0001"), // 董事長
  SAP_USER("sapUser",""), // sap使用者
  GET_ASSET_TAG("getAssetTag",""), // 取得資產編號
  ASSET_MANAGER("assetManager","C403"), // 資產管理人
  ASSET_MANAGER_SUPERVISOR("assetManagerSupervisor","C403"), // 資產管理人主管
  ASSIGN_ACCOUNTING_CLERK("assignAccountingClerk",""),//分派任務(會計經辦)
  ASSIGN_COST_CLERK("assignCostClerk",""),//分派任務(成本經辦)
  ASSIGN_PURCHASE_CLERK("assignPurchaseClerk", ""), // 分派任務(採購經辦)
  ASSIGN_IT_CLERK("assignItClerk", ""), // 分派任務(資訊經辦)
  INCOME_TAX_PROCESSOR("incomeTaxProcessor",""),//所得稅處理人
  RETENTION_COST_CLERK("retentionCostClerk",""),//保留簽的成本經辦
  ;

  private final String key;
  private final String code;

  AssigneeRole(String key, String code) {
    this.key = key;
    this.code = code;
  }

  public String getKey() {
    return key;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return key + (code.isEmpty() ? "" : "(" + code + ")");
  }
  
  /**
   * 根據 key 找到對應的 enum，如果不存在拋 IllegalArgumentException
   */
  public static AssigneeRole fromKey(String key) {
      for (AssigneeRole assigneeRole : values()) {
          if (assigneeRole.key.equals(key)) {
              return assigneeRole;
          }
      }
      throw new IllegalArgumentException("Unknown key: " + key);
  }
}
