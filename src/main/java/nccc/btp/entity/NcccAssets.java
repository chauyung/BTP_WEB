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
 * The persistent class for the ZGUIT0010 database table.
 * 
 */
@Entity
@Table(name = "NCCC_ASSETS")
@NamedQuery(name = "NcccAssets.findAll", query = "SELECT n FROM NcccAssets n")
public class NcccAssets implements Serializable {
	private static final long serialVersionUID = 1L;

	// 資產類別代號
	@Id
	@Column(name = "OPERATE_ITEM_CODE")
	private String operateItemCode;

	// 資產類別名稱
	@Column(name = "OPERATE_ITEM")
	private String operateItem;

	// 資產類別說明
	@Column(name = "DESCRIPTION")
	private String description;

	// SAP建立者名稱
	@Column(name = "SAP_CREATER")
	private String sapCreater;

	// SAP建立日期
	@Column(name = "SAP_CREATE_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate sapCreateDate;

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

	public NcccAssets() {
	}

	public String getOperateItemCode() {
		return operateItemCode;
	}

	public void setOperateItemCode(String operateItemCode) {
		this.operateItemCode = operateItemCode;
	}

	public String getOperateItem() {
		return operateItem;
	}

	public void setOperateItem(String operateItem) {
		this.operateItem = operateItem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getSapCreater() {
		return sapCreater;
	}

	public void setSapCreater(String sapCreater) {
		this.sapCreater = sapCreater;
	}

	public LocalDate getSapCreateDate() {
		return sapCreateDate;
	}

	public void setSapCreateDate(LocalDate sapCreateDate) {
		this.sapCreateDate = sapCreateDate;
	}

}
