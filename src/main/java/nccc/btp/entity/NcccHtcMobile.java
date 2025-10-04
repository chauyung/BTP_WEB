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

/**
 * The persistent class for the NCCC_HTC_MOBILE database table.
 * 
 */
@Entity
@Table(name = "NCCC_HTC_MOBILE")
@NamedQuery(name = "NcccHtcMobile.findAll", query = "SELECT n FROM NcccHtcMobile n")
public class NcccHtcMobile implements Serializable {
	private static final long serialVersionUID = 1L;

	// 通知單流水號
	@Id
	@Column(name = "NOTIFICATION_NUMBER", length = 50)
	private String notificationNumber;

	// 版次序號
	@Column(name = "CARRIER_NUMBER", length = 50)
	private String carrierNumber;

	// 發票類別代號
	@Column(name = "USER_NUMBER", length = 50)
	private String userNumber;

	// 公司統編
	@Column(name = "BUYER_TAX_ID", length = 20)
	private String buyerTaxID;

	// 發票類別代號
	@Column(name = "SELLER_TAX_ID", length = 20)
	private String sellerTaxID;

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

	public NcccHtcMobile() {
	}

	public String getNotificationNumber() {
		return notificationNumber;
	}

	public void setNotificationNumber(String notificationNumber) {
		this.notificationNumber = notificationNumber;
	}

	public String getCarrierNumber() {
		return carrierNumber;
	}

	public void setCarrierNumber(String carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getBuyerTaxID() {
		return buyerTaxID;
	}

	public void setBuyerTaxID(String buyerTaxID) {
		this.buyerTaxID = buyerTaxID;
	}

	public String getSellerTaxID() {
		return sellerTaxID;
	}

	public void setSellerTaxID(String sellerTaxID) {
		this.sellerTaxID = sellerTaxID;
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
