package nccc.btp.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "NCCC_OUCODE_OPERATE_D")
@IdClass(NcccOuOperateDId.class)
public class NcccOuOperateD {

    @Id @Column(name = "YEAR", length = 4)
    private String year;

    @Id @Column(name = "VERSION", length = 1)
    private String version;

    @Id @Column(name = "OUCODE", length = 50)
    private String oucode;

    @Id @Column(name = "GRADE_ID", length = 4)
    private String gradeId;

    @Id @Column(name = "OPERATE_ITEM_CODE", length = 10)
    private String operateItemCode;

    @Column(name = "GRADE_NAME", length = 20)
    private String gradeName;

    @Column(name = "OPERATE_ITEM", length = 100)
    private String operateItem;

    @Column(name = "OPERATE_QTY", precision = 6, scale = 2)
    private BigDecimal operateQty;

    @Column(name = "CREATE_USER", length = 50)
    private String createUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "UPDATE_USER", length = 50)
    private String updateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    // getters/setters 省略可由 IDE 產生
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getOucode() { return oucode; }
    public void setOucode(String oucode) { this.oucode = oucode; }
    public String getGradeId() { return gradeId; }
    public void setGradeId(String gradeId) { this.gradeId = gradeId; }
    public String getOperateItemCode() { return operateItemCode; }
    public void setOperateItemCode(String operateItemCode) { this.operateItemCode = operateItemCode; }
    public String getGradeName() { return gradeName; }
    public void setGradeName(String gradeName) { this.gradeName = gradeName; }
    public String getOperateItem() { return operateItem; }
    public void setOperateItem(String operateItem) { this.operateItem = operateItem; }
    public BigDecimal getOperateQty() { return operateQty; }
    public void setOperateQty(BigDecimal operateQty) { this.operateQty = operateQty; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
