package nccc.btp.vo;

import javax.persistence.Column;

public class CommitteeVo {

    // 身分證編號
    private String id;

    // 職稱
    private String jobTitle;

    // 付款方式
    private String payMethod;

    // 姓名
    private String name;

    // 起聘年月
    private String sDate;

    // 解聘年月
    private String eDate;

    // 聯絡電話
    private String phone1;

    // 金融機構代碼
    private String financialInstitutionCode;

    // 單位與部門
    private String unit;

    // Email
    private String email;

    // 職位
    private String position;

    // 傳真
    private String fax;

    // 列印順序
    private String sort;

    // 戶籍地址
    private String address;

    // 通訊地址
    private String contactAddress;

    // 組別
    private String groupName;

    // 研究項目
    private String researchProject;

    // 小組召集人
    private String convener;

    // 備註
    private String remark;

    public CommitteeVo()
    {
        this.setId("");
        this.setJobTitle("");
        this.setPayMethod("");
        this.setName("");
        this.setSDate("");
        this.setEDate("");
        this.setPhone1("");
        this.setFinancialInstitutionCode("");
        this.setUnit("");
        this.setEmail("");
        this.setPosition("");
        this.setFax("");
        this.setSort("");
        this.setAddress("");
        this.setContactAddress("");
        this.setGroupName("");
        this.setResearchProject("");
        this.setConvener("");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSDate() {
        return sDate;
    }

    public void setSDate(String sDate) {
        this.sDate = sDate;
    }

    public String getEDate() {
        return eDate;
    }

    public void setEDate(String eDate) {
        this.eDate = eDate;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getFinancialInstitutionCode() {
        return financialInstitutionCode;
    }

    public void setFinancialInstitutionCode(String financialInstitutionCode) {
        this.financialInstitutionCode = financialInstitutionCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getResearchProject() {
        return researchProject;
    }

    public void setResearchProject(String researchProject) {
        this.researchProject = researchProject;
    }

    public String getConvener() {
        return convener;
    }

    public void setConvener(String convener) {
        this.convener = convener;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
