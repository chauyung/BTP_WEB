package nccc.btp.enums;

/*
 * 驗收單-流程應用類型
 */
public enum AcceptanceFormApplyType {

    /*
     * 一般驗收
     */
    General(1),

    /*
     * 固定資產驗收
     */
    FixedAsset(2),

    /*
     * 預付設備款
     */
    PrepaymentForEquipment(3),

    /*
     * 指定結案(餘量不收)
     */
    DesignatedClosure(4),

    /*
     * 分期付款變更
     */
    InstallmentChange(5);

    private Integer Value;

    private AcceptanceFormApplyType(Integer Value) {

        this.Value = Value;

    }

    public Integer GetValue() {

        return this.Value;

    }

}
