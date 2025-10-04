package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "NCCC_COMMITTEE_LIST")
public class NcccCommitteeList {

    // 身分證字號
    @Id
    @Column(name = "ID", length = 10)
    private String id;

    // 職稱
    @Column(name = "JOB_TITLE", length = 20)
    private String jobTitle;

    // 付款方式
    @Column(name = "PAY_METHOD", length = 50)
    private String payMethod;

    // 姓名
    @Column(name = "NAME", length = 200)
    private String name;

    // 起聘年月
    @Column(name = "SDATE")
    private LocalDate sDate;

    // 解聘年月
    @Column(name = "EDATE")
    private LocalDate eDate;

    // 聯絡電話
    @Column(name = "PHONE1", length = 50)
    private String phone1;

    // 金融機構代碼
    @Column(name = "FINANCIAL_INSTITUTION_CODE", length = 3)
    private String financialInstitutionCode;

    // 單位與部門
    @Column(name = "UNIT", length = 50)
    private String unit;

    // Email
    @Column(name = "EMAIL", length = 50)
    private String email;

    // 職位
    @Column(name = "POSITION", length = 20)
    private String position;

    // 傳真
    @Column(name = "FAX", length = 20)
    private String fax;

    // 列印順序
    @Column(name = "SORT", length = 10)
    private String sort;

    // 戶籍地址
    @Column(name = "ADDRESS", length = 150)
    private String address;

    // 通訊地址
    @Column(name = "CONTACT_ADDRESS", length = 150)
    private String contactAddress;

    // 組別
    @Column(name = "GROUP_NAME", length = 20)
    private String groupName;

    // 研究項目
    @Column(name = "RESEARCH_PROJECT", length = 255)
    private String researchProject;

    // 小組召集人
    @Column(name = "CONVENER", length = 10)
    private String convener;

    // 備註
    @Column(name = "REMARK", length = 200)
    private String remark;

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

    public LocalDate getSDate() {
        return sDate;
    }

    public void setSDate(LocalDate sDate) {
        this.sDate = sDate;
    }

    public LocalDate getEDate() {
        return eDate;
    }

    public void setEDate(LocalDate eDate) {
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
