package nccc.btp.vo;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 費用申請單Vo
 */
public class BpmExMVo {

    private String exNo;

    private String taskId;

    private String applyDate;

    private String applyType;

    private String applicant;

    private String empNo;

    private String department;

    private String payEmpNo;

    private String payName;

    private String payWay;

    private String specialPayDate;

    private String applyReason;

    private String tradeCurrency;

    private BigDecimal currencyRate;

    private BigDecimal total;

    private BigDecimal predictTwd;

    private  List<String> prepayNo;

    private BigDecimal payAmount;

    private BigDecimal refundAmount;

    private String refundDate;

    private String bankAccount;

    private String approvalMethod;

    private String approvalNo;

    private String payWayMethod;

    private String withholdTotal;

    private String postingDate;

    @Lob
    private byte[] attachment;

    private List<BpmExMDetailVo> bpmExMDetailVoList;

    //扣繳項目
    private List<BpmExMWHVo> bpmExMWHVoList;

    //董監事/研究委員明細
    private List<BpmExMPplVo> bpmExMPplVoList;

    public BpmExMVo(){

        this.setBpmExMDetailVoList(new ArrayList<>());

        this.setPrepayNo(new ArrayList<>());

        this.setBpmExMWHVoList(new ArrayList<>());

        this.setBpmExMPplVoList(new ArrayList<>());
    }

    public String getExNo() {
        return exNo;
    }

    public void setExNo(String exNo) {
        this.exNo = exNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPayEmpNo() {
        return payEmpNo;
    }

    public void setPayEmpNo(String payEmpNo) {
        this.payEmpNo = payEmpNo;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getSpecialPayDate() {
        return specialPayDate;
    }

    public void setSpecialPayDate(String specialPayDate) {
        this.specialPayDate = specialPayDate;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }

    public BigDecimal getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(BigDecimal currencyRate) {
        this.currencyRate = currencyRate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPredictTwd() {
        return predictTwd;
    }

    public void setPredictTwd(BigDecimal predictTwd) {
        this.predictTwd = predictTwd;
    }

    public List<String> getPrepayNo() {
        return prepayNo;
    }

    public void setPrepayNo(List<String> prepayNo) {
        this.prepayNo = prepayNo;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getApprovalMethod() {
        return approvalMethod;
    }

    public void setApprovalMethod(String approvalMethod) {
        this.approvalMethod = approvalMethod;
    }

    public String getApprovalNo() {
        return approvalNo;
    }

    public void setApprovalNo(String approvalNo) {
        this.approvalNo = approvalNo;
    }

    public String getPayWayMethod() {
        return payWayMethod;
    }

    public void setPayWayMethod(String payWayMethod) {
        this.payWayMethod = payWayMethod;
    }

    public String getWithholdTotal() {
        return withholdTotal;
    }

    public void setWithholdTotal(String withholdTotal) {
        this.withholdTotal = withholdTotal;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public List<BpmExMDetailVo> getBpmExMDetailVoList() {
        return bpmExMDetailVoList;
    }

    public void setBpmExMDetailVoList(List<BpmExMDetailVo> bpmExMDetailVoList) {
        this.bpmExMDetailVoList = bpmExMDetailVoList;
    }

    public List<BpmExMWHVo> getBpmExMWHVoList() {
        return bpmExMWHVoList;
    }

    public void setBpmExMWHVoList(List<BpmExMWHVo> bpmExMWHVoList) {
        this.bpmExMWHVoList = bpmExMWHVoList;
    }

    public List<BpmExMPplVo> getBpmExMPplVoList() {
        return bpmExMPplVoList;
    }

    public void setBpmExMPplVoList(List<BpmExMPplVo> bpmExMPplVoList) {
        this.bpmExMPplVoList = bpmExMPplVoList;
    }
}
