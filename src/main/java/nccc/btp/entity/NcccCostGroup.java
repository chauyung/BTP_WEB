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
 * The persistent class for the NCCC_COST_GROUP database table.
 * 
 */
@Entity
@Table(name="NCCC_COST_GROUP")
@NamedQuery(name="NcccCostGroup.findAll", query="SELECT n FROM NcccCostGroup n")
public class NcccCostGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(name="GROUP_NAME")
	private String groupName;

	@Column(name="GROUP_NO")
	private String groupNo;

	@Column(name="SERIAL_NO")
	private BigDecimal serialNo;

	@Column(name = "UPDATE_USER", length = 50)
	private String updateUser;
	
	@Column(name = "UPDATE_DATE", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate updateDate;

	@PrePersist
	@PreUpdate
	protected void onUpdate() {
	  updateDate = LocalDate.now();
	}

	public NcccCostGroup() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupNo() {
		return this.groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public BigDecimal getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(BigDecimal serialNo) {
		this.serialNo = serialNo;
	}

	public String getUpdateUser() {
		return this.updateUser;
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