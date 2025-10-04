package nccc.btp.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "NCCC_EXPENSE_CATEGORY_NUMBER")
@NamedQuery(name = "NcccExpenseCategoryNumber.findAll", query = "SELECT n FROM NcccExpenseCategoryNumber n")
public class NcccExpenseCategoryNumber {
	private static final long serialVersionUID = 1L;

	// 群組
	@Column(name = "CATEGORYGROUP", length = 10)
	private String categoryGroup;

	// 品號
	@Id
	@Column(name = "CATEGORYNUMBER", length = 10, nullable = false)
	private String categoryNumber;

	// 品名
	@Column(name = "CATEGORYNAME", length = 50)
	private String categoryName;

	// 總帳科目
	@Column(name = "ACCOUNTING", length = 8)
	private String accounting;

	// 會計科目短文
	@Column(name = "SUBJECT", length = 50)
	private String subject;

	// 成本中心必填
	@Column(name = "ASSETSACCOUNTING", length = 1)
	private String assetsaccounting;

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
	
	public NcccExpenseCategoryNumber() {
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

}
