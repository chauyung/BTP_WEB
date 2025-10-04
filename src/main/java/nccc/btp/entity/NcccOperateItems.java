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
@Table(name = "NCCC_OPERATE_ITEMS")
@NamedQuery(name = "NcccOperateItems.findAll", query = "SELECT n FROM NcccOperateItems n")
public class NcccOperateItems implements Serializable {
	private static final long serialVersionUID = 1L;

	// 作業項目代號
	@Id
	@Column(name = "OPERATE_ITEM_CODE", nullable = false)
	private String operateItemCode;

	// 作業項目
	@Column(name = "OPERATE_ITEM")
	private String operateItem;

	// 說明
	@Column(name = "DESCRIPTION")
	private String description;

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

	public NcccOperateItems() {
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

}
