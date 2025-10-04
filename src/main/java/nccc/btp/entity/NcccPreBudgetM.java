package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 預算編列資料主檔
 */
@Getter
@Setter
@Entity
@Table(name = "NCCC_PRE_BUDGET_M")
public class NcccPreBudgetM implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 預算單號
     */
    @Id
    @Column(name = "BUDGET_NO", length = 20)
    private String budgetNo;

    /**
     * 日期
     */
    @Column(name = "APPLY_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate applyDate;

    /**
     * 員工代號
     */
    @Column(name = "APPLY_USER", length = 50)
    private String applyUser;

    /**
     * 申請人部門
     */
    @Column(name = "APPLY_OUCODE", length = 50)
    private String applyOuCode;

    /**
     * 預算部門
     */
    @Column(name = "OUCODE", length = 50)
    private String ouCode;

    /**
     * 預算年度
     */
    @Column(name = "YEAR", length = 4)
    private String year;

    /**
     * 預算版次
     */
    @Column(name = "VERSION", length = 1)
    private String version;

    /**
     * 建檔者=上傳者
     */
    @Column(name = "CREATE_USER", length = 10)
    private String createUser;

    /**
     * 建檔日期
     */
    @Column(name = "CREATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    /**
     * 更新者
     */
    @Column(name = "UPDATE_USER", length = 10)
    private String updateUser;

    /**
     * 更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;

    /**
     * 開帳者
     */
    @Column(name = "OPEN_USER", length = 10)
    private String openUser;

    /**
     * 開帳日期
     */
    @Column(name = "OPEN_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openDate;

    /**
     * 確認人員
     */
    @Column(name = "ASSIGN_USER", length = 10)
    private String assignUser;

    /**
     * 確認日期
     */
    @Column(name = "ASSIGN_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignDate;

    /**
     * 覆核人員
     */
    @Column(name = "REVIEW_USER", length = 10)
    private String reviewUser;

    /**
     * 覆核日期
     */
    @Column(name = "REVIEW_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate reviewDate;

    /**
     * 覆核人員
     */
    @Column(name = "ASSIGNMENT", length = 255)
    private String assignment;
}
