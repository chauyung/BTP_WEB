package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 提列費用主檔
 */
@Getter
@Setter
@Entity
@Table(name = "NCCC_PROVISION_EXPENSE_M")
public class NcccProvisionExpenseM implements Serializable {
	private static final long serialVersionUID = 1L;
    
    /**
     * 提列單號
     */
    @Id
    @Column(name = "PROVISION_NO", length = 20)
    private String provisionNo;

    /**
     * 任務編號
     */
    @Column(name = "TaskID", length = 40)
    private String taskID;

    /**
     * 日期
     */
    @Column(name = "APPLY_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate applyDate;

    /**
     * 員工代號
     */
    @Column(name = "APPLY_USER", length = 50)
    private String applyUser;

    /**
     * 部門
     */
    @Column(name = "APPLY_OUCODE", length = 50)
    private String applyOuCode;

    /**
     * 預算年度
     */
    @Column(name = "YEAR", length = 4)
    private String year;

    /**
     * 版次
     */
    @Column(name = "VERSION", length = 1)
    private String version;

    /**
     * 動用項目代號
     */
    @Column(name = "ACCOUNTING", length = 8)
    private String accounting;

    /**
     * 採購單號
     */
    @Column(name = "PURCHASE_NO", length = 20)
    private String purchaseNo;

    /**
     * 指定過帳日
     */
    @Column(name = "POSTING_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate postingDate;

    /**
     * SAP文件編號
     */
    @Column(name = "DOC_NO", length = 20)
    private String docNo;

    /**
     * 結案碼Y:結案
     */
    @Column(name = "CLOSE_STATUS", length = 1)
    private String closeStatus;

    /**
     * 結案日期
     */
    @Column(name = "CLOSE_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime closeDate;

    
    /**
     * 作廢碼Y:作廢
     */
    @Column(name = "VOID_STATUS", length = 1)
    private String voidStatus;

    /**
     * 作廢日期
     */
    @Column(name = "VOID_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime voidDate;

    /**
     * 建檔者
     */
    @Column(name = "CREATE_USER", length = 10)
    private String createUser;

    /**
     * 建檔日期
     */
    @Column(name = "CREATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;

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
    private LocalDateTime updateDate;

    /**
     * 月份
     */
    @Column(name = "MONTH", length = 2)
    private String month;

    /**
     * 動用金額
     */
    @Column(name = "TOTAL_AMOUNT",  precision = 11)
    private BigDecimal totalAmount;

    /** 流程狀態 */
    @Column(name = "FLOW_STATUS", length = 2)
    private String flowStatus;
}
