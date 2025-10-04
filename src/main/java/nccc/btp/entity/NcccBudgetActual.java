package nccc.btp.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "NCCC_BUDGET_ACTUAL")
public class NcccBudgetActual {

    @EmbeddedId
    private NcccBudgetActualId id;

    @Column(name = "OUNAME", length = 30)
    private String ouName;

    @Column(name = "SUBJECT", length = 350)
    private String subject;

    @Column(name = "OPERATE_ITEM", length = 100)
    private String operateItem;

    @Column(name = "AMOUNT", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "REMARK", length = 100)
    private String remark;

    @Column(name = "CREATE_USER", length = 50)
    private String createUser;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "UPDATE_USER", length = 50)
    private String updateUser;

    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public void setKey(String yymm, String version, String approation,
            String ouCode, String accounting, String itemCode) {
		NcccBudgetActualId id = new NcccBudgetActualId();
		id.setYymm(yymm);
		id.setVersion(version);
		id.setApproation(approation);
		id.setOuCode(ouCode);
		id.setAccounting(accounting);
		id.setOperateItemCode(itemCode);
		this.id = id;
	}
    
    // ===== Getter / Setter =====
    public NcccBudgetActualId getId() { return id; }
    public void setId(NcccBudgetActualId id) { this.id = id; }

    public String getOuName() { return ouName; }
    public void setOuName(String ouName) { this.ouName = ouName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getOperateItem() { return operateItem; }
    public void setOperateItem(String operateItem) { this.operateItem = operateItem; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }

    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    

}
