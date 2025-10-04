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
 * The persistent class for the NCCC_HTC_MOBILE database table.
 * 
 */
@Entity
@Table(name = "NCCC_UTILITY_BUDGET_ATTRIBUTION_P")
@NamedQuery(name = "NcccUtilityBudgetAttribution.findAll", query = "SELECT n FROM NcccUtilityBudgetAttribution n")
public class NcccUtilityBudgetAttribution implements Serializable {
	private static final long serialVersionUID = 1L;

	// 部門編號
	@Column(name = "OU_CODE", length = 10)
	private String ouCode;

	// 會計科目
	@Column(name = "ACCOUNTING", length = 10)
	private String accounting;

	// 用戶號碼
	@Id
	@Column(name = "PHONE", length = 50)
	private String phone;

	// 中心金額上限
	@Column(name = "LIMIT_AMOUNT", precision = 13, scale = 2)
	private BigDecimal limitAmount;

	// 修改人員
	@Column(name = "UPDATE_USER", length = 50)
	private String updateUser;

	// 修改時間
	@Column(name = "UPDATE_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate updateDate;

	@PrePersist
	@PreUpdate
	protected void onUpdate() {
		updateDate = LocalDate.now();
	}

	public NcccUtilityBudgetAttribution() {
	}

	public String getOuCode() {
		return ouCode;
	}

	public void setOuCode(String ouCode) {
		this.ouCode = ouCode;
	}

	public String getAccounting() {
		return accounting;
	}

	public void setAccounting(String accounting) {
		this.accounting = accounting;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
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
