package nccc.btp.entity;

import java.io.Serializable;
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
@Table(name = "NCCC_ACCOUNTING_LIST")
@NamedQuery(name = "NcccAccountingList.findAll", query = "SELECT n FROM NcccAccountingList n")
public class NcccAccountingList implements Serializable {
	private static final long serialVersionUID = 1L;

	// 科目群組
	@Column(name = "ACCOUNTING_GROUP")
	private String accountingGroup;

	// 會計科目
	@Id
	@Column(name = "SUBJECT", nullable = false)
	private String subject;

	// 短文(繁體中文)
	@Column(name = "ESSAY")
	private String essay;

	// 總帳科目長文(繁體中文)
	@Column(name = "ACCOUNT_LONG")
	private String accountLong;

	// 幣別
	@Column(name = "CURRENCY")
	private String currency;

	// 預算科目
	@Column(name = "BUDGET")
	private String budget;

	// 所得稅科目
	@Column(name = "INCOME_TAX")
	private String incomeTax;

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

	public NcccAccountingList() {
	}

	public String getAccountingGroup() {
		return accountingGroup;
	}

	public void setAccountingGroup(String accountingGroup) {
		this.accountingGroup = accountingGroup;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEssay() {
		return essay;
	}

	public void setEssay(String essay) {
		this.essay = essay;
	}

	public String getAccountLong() {
		return accountLong;
	}

	public void setAccountLong(String accountLong) {
		this.accountLong = accountLong;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(String incomeTax) {
		this.incomeTax = incomeTax;
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
