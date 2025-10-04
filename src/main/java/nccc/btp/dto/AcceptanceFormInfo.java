package nccc.btp.dto;

import java.util.List;

public class AcceptanceFormInfo {

    /*
     * 驗收單號
     */
    public String AcceptanceFormNo;

    /*
     * 請購單號
     */
    public String PurchaseRequisitionNo;

    /*
     * 採購單號
     */
    public String PurchaseOrderNo;

    /*
     * 員工編號
     */
    public String EmployeeNo;

    /*
     * 申請人
     */
    public String Applicant;

    /*
     * 交貨地點
     */
    public String DeliveryLocation;

    /*
     * 部門
     */
    public String Department;

    /*
     * 請購目的
     */
    public String PurchasePurpose;

    /*
     * 議價核准 - 公文文號
     */
    public String PriceNegotiationDocumentNo;

    /*
     * 分期驗收
     */
    public List<AcceptanceFormStagedDetailInfo> StagedAcceptanceDetails;

    /*
     * 權責分攤
     */
    public List<AcceptanceFormAllocationOfResponsibilityInfo> AllocationOfResponsibilities;

    /*
     * 固定資產
     */
    public List<AcceptanceFormFixedAssetInfo> FixedAssets;

    /*
     * 現有附件
     */
    public List<UploadFileInfo> ExistingAppendices;

    /*
     * 現有憑證
     */
    public List<UploadFileInfo> ExistingCertificates;

    /*
     * 代扣繳項目
     */
    public List<WithholdingItemInfo> WithholdingItems;

    /*
     * 處理驗收
     */
    public Boolean HandleAcceptance;

    /*
     * 審核歷程記錄
     */
    public List<ApprovalProcessRecordInfo> ApprovalProcessRecords;
}
