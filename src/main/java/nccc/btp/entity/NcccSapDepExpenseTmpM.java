package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * SAP折舊預算暫存檔
 */
@Entity
@Table(name = "NCCC_SAP_DEP_EXPENSE_TMP_M")
public class NcccSapDepExpenseTmpM implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;

    /**
     * 執行日期時間(開始執行時取得的日期時間,沿用到執行結束,作為重複執行時辨識用)
     */
    @Column(name = "INPUT_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inputDate;

    /**
     * 預算年度
     */
    @Column(name = "YEAR", length = 4)
    private String year;

    /**
     * 單號
     */
    @Column(name = "OUCODE", length = 50)
    private String ouCode;  

    /**
     * 預算項目代號
     */
    @Column(name = "ACCOUNTING", length = 8)
    private String accounting;

    /**
     * 預算項目名稱
     */
    @Column(name = "SUBJECT", length = 20)
    private String subject;

    
    /**
     * 資產代號
     */
    @Id
    @Column(name = "ASSETS_CODE", length = 20)
    private String assetsCode;

    /**
     * 摘要說明
     */
    @Column(name = "REMARK", length = 200)
    private String remark;

    /**
     * 金額
     */
    @Column(name = "AMOUNT")
    private BigDecimal amount;

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
}
