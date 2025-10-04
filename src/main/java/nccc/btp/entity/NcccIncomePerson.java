package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The persistent class for the NCCC_INCOME_PERSON database table.
 * 
 */
@Entity
@Table(name = "NCCC_INCOME_PERSON")
@NamedQuery(name = "NcccIncomePerson.findAll", query = "SELECT n FROM NcccIncomePerson n")
public class NcccIncomePerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nccc_income_person")
	@SequenceGenerator(name = "seq_nccc_income_person", sequenceName = "SEQ_NCCC_INCOME_PERSON", allocationSize = 1)
	@Column(name = "PK_ID", nullable = false, precision = 38, scale = 0)
	private Long pkId;

	// 證號
	@Column(name = "ID", length = 10)
	private String id;

	// 姓名
	@Column(name = "NAME", length = 200)
	private String name;

	// 所得人類別
	@Column(name = "INCOME_CATEGORY", length = 10)
	private String incomeCategory;

	// 證號別
	@Column(name = "CERTIFICATE_CATEGORY", length = 1)
	private String certificateCategory;

	// 國內有無住所
	@Column(name = "RESIDENCE", length = 1)
	private String residence;

	// 所得人錯誤註記
	@Column(name = "ERROR_NOTE", length = 1)
	private String errorNote;

	// 聯絡電話ㄧ
	@Column(name = "PHONE1", length = 50)
	private String phone1;

	// 聯絡電話二
	@Column(name = "PHONE2", length = 50)
	private String phone2;

	// 手機號碼
	@Column(name = "MOBILE", length = 50)
	private String mobile;

	// EMAIL
	@Column(name = "EMAIL", length = 50)
	private String email;

	// 聯絡人姓名
	@Column(name = "CONTACT_NAME", length = 200)
	private String contactName ;

	// 聯絡人電話
	@Column(name = "CONTACT_PHONE", length = 50)
	private String contactPhone;

	// 聯絡人EMAIL
	@Column(name = "CONTACT_EMAIL", length = 50)
	private String contactEmail;

	// 戶籍地址
	@Column(name = "ADDRESS", length = 150)
	private String address ;

	// 通訊地址
	@Column(name = "CONTACT_ADDRESS", length = 150)
	private String contactAddress;

	// 備註
	@Column(name = "REMARK", length = 200)
	private String remark ;

	// 資料來源檔
	@Column(name = "SOURCE_FILE", length = 1)
	private String sourceFile ;

	// 修改人員
	@Column(name = "UPDATE_USER", nullable = false)
	private String updateUser;

	// 修改時間
	@Column(name = "UPDATE_DATE", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate updateDate;

	@PrePersist
	@PreUpdate
	protected void onUpdate() {
		updateDate = LocalDate.now();
	}

	public NcccIncomePerson() {
	}

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

	public LocalDate getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDate updateDate) {
		this.updateDate = updateDate;
	}

}
