package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 會計科目階層檔
 * TABLE: NCCC_ACCOUNTING_LEVEL
 * COLUMNS:
 *  - ACCOUNTING_LEVEL_CODE (PK)
 *  - ACCOUNTING_LEVEL_NAME
 *  - CREATE_USER, CREATE_DATE
 *  - UPDATE_USER, UPDATE_DATE
 */
@Entity
@Table(name = "NCCC_ACCOUNTING_LEVEL")
public class NcccAccountingLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ACCOUNTING_LEVEL_CODE", nullable = false, length = 8)
    private String accountingLevelCode;

    @Column(name = "ACCOUNTING_LEVEL_NAME", length = 30)
    private String accountingLevelName;

    @Column(name = "CREATE_USER", length = 50)
    private String createUser;

    @Column(name = "CREATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @Column(name = "UPDATE_USER", length = 50)
    private String updateUser;

    @Column(name = "UPDATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;

    public NcccAccountingLevel() {}

    @PrePersist
    protected void onCreate() {
        LocalDate today = LocalDate.now();
        if (this.createDate == null) this.createDate = today;
        this.updateDate = today;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDate.now();
    }

    public String getAccountingLevelCode() { return accountingLevelCode; }
    public void setAccountingLevelCode(String accountingLevelCode) { this.accountingLevelCode = accountingLevelCode; }

    public String getAccountingLevelName() { return accountingLevelName; }
    public void setAccountingLevelName(String name) { this.accountingLevelName = name; }

    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }

    public LocalDate getCreateDate() { return createDate; }
    public void setCreateDate(LocalDate createDate) { this.createDate = createDate; }

    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }

    public LocalDate getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDate updateDate) { this.updateDate = updateDate; }
}
