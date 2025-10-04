package nccc.btp.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 費用申請單人員明晰
 */
public class BpmExMPplVo {

    //編號
    private String exItemNo;

    //身分證
    private String pplId;

    //姓名
    private String name;

    //是否為公務員
    private String isCivil;

    //職稱
    private String jobTitle;

    //服務單位
    private String unit;

    // (董)車馬費 / 研發委員研究費
    private BigDecimal amount1;

    // (董)出席費 / 幹事津貼
    private BigDecimal amount2;

    // (單位)車馬費 / 出席費
    private BigDecimal amount3;

    // (單位)出席費
    private BigDecimal amount4;

    //合計
    private BigDecimal totalAmount;

    // 預算年度
    private String year;

    // 預算部門
    private String ouCode;

    // 預算會計科目1
    private String accounting1;

    // 預算會計科目2
    private String accounting2;

    // 預算會計科目3
    private String accounting3;

    // 預算會計科目4
    private String accounting4;

    // 所得人
    private List<BpmExMDetailWHVo> bpmExMDetailWHVoList;

    public BpmExMPplVo()
    {
        this.setPplId("");
        this.setName("");
        this.setIsCivil("");
        this.setJobTitle("");
        this.setUnit("");
        this.setAmount1(new BigDecimal("0.00"));
        this.setAmount2(new BigDecimal("0.00"));
        this.setAmount3(new BigDecimal("0.00"));
        this.setAmount4(new BigDecimal("0.00"));
        this.setTotalAmount(new BigDecimal("0.00"));
        this.setBpmExMDetailWHVoList(new ArrayList<>());
    }

    public String getExItemNo() {
        return exItemNo;
    }

    public void setExItemNo(String exItemNo) {
        this.exItemNo = exItemNo;
    }

    public String getPplId() {
        return pplId;
    }

    public void setPplId(String pplId) {
        this.pplId = pplId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsCivil() {
        return isCivil;
    }

    public void setIsCivil(String isCivil) {
        this.isCivil = isCivil;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public BigDecimal getAmount3() {
        return amount3;
    }

    public void setAmount3(BigDecimal amount3) {
        this.amount3 = amount3;
    }

    public BigDecimal getAmount4() {
        return amount4;
    }

    public void setAmount4(BigDecimal amount4) {
        this.amount4 = amount4;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOuCode() {
        return ouCode;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getAccounting1() {
        return accounting1;
    }

    public void setAccounting1(String accounting1) {
        this.accounting1 = accounting1;
    }

    public String getAccounting2() {
        return accounting2;
    }

    public void setAccounting2(String accounting2) {
        this.accounting2 = accounting2;
    }

    public String getAccounting3() {
        return accounting3;
    }

    public void setAccounting3(String accounting3) {
        this.accounting3 = accounting3;
    }

    public String getAccounting4() {
        return accounting4;
    }

    public void setAccounting4(String accounting4) {
        this.accounting4 = accounting4;
    }

    public List<BpmExMDetailWHVo> getBpmExMDetailWHVoList() {
        return bpmExMDetailWHVoList;
    }

    public void setBpmExMDetailWHVoList(List<BpmExMDetailWHVo> bpmExMDetailWHVoList) {
        this.bpmExMDetailWHVoList = bpmExMDetailWHVoList;
    }
}
