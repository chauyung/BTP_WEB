package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;
/**
 * 所得人Vo
 */
public class IncomePersonVo {

	// 主健
	private Long pkId;

	// 身分證字號
	private String id;
	
	// 姓名
	private String name;
	
	// 所得人類別
	private String incomeCategory;

	// 證號別
	private String certificateCategory;

	// 國內有無住所
	private String residence;

	// 所得人錯誤註記
	private String errorNote;

	// 聯絡電話ㄧ
	private String phone1;

	// 聯絡電話二
	private String phone2;

	// 手機號碼
	private String mobile;

	// EMAIL
	private String email;

	// 聯絡人姓名
	private String contactName ;

	// 聯絡人電話
	private String contactPhone;

	// 聯絡人EMAIL
	private String contactEmail;

	// 戶籍地址
	private String address ;

	// 通訊地址
	private String contactAddress;

	// 備註
	private String remark ;

	// 資料來源檔
	private String sourceFile ;

	// 修改人員
	private String updateUser;

	// 修改時間
	private String updateDate;

	public Long getPkId() {
		return pkId;
	}

	public void setPkId(Long pkId) {
		this.pkId = pkId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIncomeCategory() {
		return incomeCategory;
	}

	public void setIncomeCategory(String incomeCategory) {
		this.incomeCategory = incomeCategory;
	}

	public String getCertificateCategory() {
		return certificateCategory;
	}

	public void setCertificateCategory(String certificateCategory) {
		this.certificateCategory = certificateCategory;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getErrorNote() {
		return errorNote;
	}

	public void setErrorNote(String errorNote) {
		this.errorNote = errorNote;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
}
