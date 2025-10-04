package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The persistent class for the NCCC_PURCHASE_CATEGORY_NUMBER database table.
 * 
 */
@Entity
@Table(name = "NCCC_PURCHASE_CATEGORY_NUMBER")
@NamedQuery(name = "NcccPurchaseCategoryNumber.findAll", query = "SELECT n FROM NcccPurchaseCategoryNumber n")
public class NcccPurchaseCategoryNumber implements Serializable {
	private static final long serialVersionUID = 1L;

	// 群組
	@Column(name = "CATEGORYGROUP")
	private String categoryGroup;

	// 品號
	@Id
	@Column(name = "CATEGORYNUMBER", nullable = false)
	private String categoryNumber;

	// 品名
	@Column(name = "CATEGORYNAME")
	private String categoryName;

	// 總帳科目
	@Column(name = "ACCOUNTING")
	private String accounting;

	// 會計科目短文
	@Column(name = "SUBJECT")
	private String subject;

	// 成本中心必填
	@Column(name = "ASSETSACCOUNTING")
	private String assetsaccounting;

	/**
	 * 是否為固定資產
	 */
	@Column(name = "IS_FIXED_ASSET")
	private boolean isFixedAsset;

	/**
	 * 耐用年限
	 */
	@Column(name = "USEFUL_LIFE_YEARS")
	private Integer usefulLifeYears;

	// 修改人員
	@Column(name = "UPDATE_USER", length = 50)
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

	public NcccPurchaseCategoryNumber() {
	}

	public String getCategoryGroup() {
		return categoryGroup;
	}

	public void setCategoryGroup(String categoryGroup) {
		this.categoryGroup = categoryGroup;
	}

	public String getCategoryNumber() {
		return categoryNumber;
	}

	public void setCategoryNumber(String categoryNumber) {
		this.categoryNumber = categoryNumber;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAccounting() {
		return accounting;
	}

	public void setAccounting(String accounting) {
		this.accounting = accounting;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAssetsaccounting() {
		return assetsaccounting;
	}

	public void setAssetsaccounting(String assetsaccounting) {
		this.assetsaccounting = assetsaccounting;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsFixedAsset() {
		return isFixedAsset;
	}

	public void setIsFixedAsset(Boolean isFixedAsset) {
		this.isFixedAsset = isFixedAsset;
	}

	public Integer getUsefulLifeYears() {
		return usefulLifeYears;
	}

	public void setUsefulLifeYears(Integer usefulLifeYears) {
		this.usefulLifeYears = usefulLifeYears;
	}
}
