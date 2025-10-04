package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "NCCC_DIRECTOR_SUPERVISOR_LIST")
public class NcccDirectorSupervisorList {

    // 身分證字號
    @Id
    @Column(name = "ID", length = 10)
    private String id;

    // 職稱
    @Column(name = "JOB_TITLE", length = 20)
    private String jobTitle;

    // 姓名
    @Column(name = "NAME", length = 200)
    private String name;

    // 出生日期
    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    // 服務單位
    @Column(name = "UNIT", length = 50)
    private String unit;

    // 單位職稱
    @Column(name = "UNIT_JOBTITLE", length = 50)
    private String unitJobtitle;

    // 起聘年月
    @Column(name = "SDATE")
    private LocalDate sDate;

    // 解聘年月
    @Column(name = "EDATE")
    private LocalDate eDate;

    // 聯絡電話一
    @Column(name = "PHONE1", length = 50)
    private String phone1;

    // 聯絡電話二
    @Column(name = "PHONE2", length = 50)
    private String phone2;

    // 手機號碼
    @Column(name = "MOBILE", length = 50)
    private String mobile;

    // 聯絡人姓名
    @Column(name = "CONTACT_NAME", length = 50)
    private String contactName;

    // 聯絡人電話
    @Column(name = "CONTACT_PHONE", length = 50)
    private String contactPhone;

    // 付款方式
    @Column(name = "PAY_METHOD", length = 50)
    private String payMethod;

    // 戶籍地址
    @Column(name = "ADDRESS", length = 150)
    private String address;

    // 通訊地址
    @Column(name = "CONTACT_ADDRESS", length = 150)
    private String contactAddress;

    // 備註一
    @Column(name = "REMARK1", length = 200)
    private String remark1;

    // 備註二
    @Column(name = "REMARK2", length = 200)
    private String remark2;

    // 列印順序
    @Column(name = "SORT", length = 2)
    private String sort;

    // 是否為公務員
    @Column(name = "IS_CIVIL", length = 1)
    private String isCivil;

    // 業務夥伴號碼
    @Column(name = "PARTNER", length = 10)
    private String partner;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitJobtitle() {
        return unitJobtitle;
    }

    public void setUnitJobtitle(String unitJobtitle) {
        this.unitJobtitle = unitJobtitle;
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

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
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

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIsCivil() {
        return isCivil;
    }

    public void setIsCivil(String isCivil) {
        this.isCivil = isCivil;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
}
