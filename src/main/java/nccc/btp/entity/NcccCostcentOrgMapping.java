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
 * The persistent class for the NCCC_COSTCENT_ORG_MAPPING database table.
 * 
 */
@Entity
@Table(name = "NCCC_COSTCENT_ORG_MAPPING")
@NamedQuery(name = "NcccCostcentOrgMapping.findAll", query = "SELECT n FROM NcccCostcentOrgMapping n")
public class NcccCostcentOrgMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	// HR代號
	@Id
	@Column(name = "HR_DEP_CODE_ACT")
	private String hrDepCodeAct;

	// 中文名稱
	@Column(name = "DEP_NAME_ACT")
	private String depNameAct;

	// SAP成本中心
	@Column(name = "COSTCENTER")
	private String costcenter;

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
	
	public NcccCostcentOrgMapping() {
	}

	public String getHrDepCodeAct() {
		return hrDepCodeAct;
	}

	public void setHrDepCodeAct(String hrDepCodeAct) {
		this.hrDepCodeAct = hrDepCodeAct;
	}

	public String getDepNameAct() {
		return depNameAct;
	}

	public void setDepNameAct(String depNameAct) {
		this.depNameAct = depNameAct;
	}

	public String getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(String costcenter) {
		this.costcenter = costcenter;
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
