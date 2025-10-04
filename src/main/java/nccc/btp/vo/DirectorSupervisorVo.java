package nccc.btp.vo;

public class DirectorSupervisorVo {

    // 身分證字號
    private String id;

    // 職稱
    private String jobTitle;

    // 姓名
    private String name;

    // 出生日期
    private String birthday;

    // 服務單位
    private String unit;

    // 單位職稱
    private String unitJobTitle;

    // 起聘年月
    private String sDate;

    // 解聘年月
    private String eDate;

    // 聯絡電話一
    private String phone1;

    // 聯絡電話二
    private String phone2;

    // 手機號碼
    private String mobile;

    // 聯絡人姓名
    private String contactName;

    // 聯絡人電話
    private String contactPhone;

    // 付款方式
    private String payMethod;

    // 戶籍地址
    private String address;

    // 通訊地址
    private String contactAddress;

    // 通訊地址
    private String remark1;

    // 通訊地址
    private String remark2;

    // 列印順序
    private String sort;

    // 是否為公務員
    private String isCivil;

    public DirectorSupervisorVo()
    {
        this.setId("");
        this.setJobTitle("");
        this.setName("");
        this.setBirthday("");
        this.setUnit("");
        this.setUnitJobTitle("");
        this.setSDate("");
        this.setEDate("");
        this.setPhone1("");
        this.setPhone2("");
        this.setMobile("");
        this.setContactName("");
        this.setContactPhone("");
        this.setPayMethod("");
        this.setAddress("");
        this.setContactAddress("");
        this.setRemark1("");
        this.setRemark2("");
        this.setSort("");
        this.setIsCivil("");
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitJobTitle() {
        return unitJobTitle;
    }

    public void setUnitJobTitle(String unitJobTitle) {
        this.unitJobTitle = unitJobTitle;
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
}
