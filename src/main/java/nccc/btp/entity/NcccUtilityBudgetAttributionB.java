package nccc.btp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "NCCC_UTILITY_BUDGET_ATTRIBUTION_B")
@NamedQuery(name = "NcccUtilityBudgetAttributionB.findAll", query = "SELECT n FROM NcccUtilityBudgetAttributionB n")
public class NcccUtilityBudgetAttributionB implements Serializable {
    private static final long serialVersionUID = 1L;

    // 部門編號
    @Column(name = "OU_CODE")
    private String ouCode;

    // 會計科目
    @Column(name = "ACCOUNTING")
    private String accounting;

    // 用戶號碼
    @Id
    @Column(name = "PHONE")
    private String phone;

    // 中心金額上限
    @Column(name = "LIMIT_AMOUNT", precision = 13, scale = 2)
    private BigDecimal limitAmount;

    // 修改人員
    @Column(name = "UPDATE_USER")
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

    public NcccUtilityBudgetAttributionB() {
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
