package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 預算交易明細檔
 */
@Setter
@Getter
@Entity
@Table(name = "NCCC_BUDGET_TRANSCATION")
public class NcccBudgetTranscation implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private BigInteger id;

    /**
     * 預算年度
     */
    @Column(name = "YEAR",length = 4)
    private String year;

    /**
     * 版次
     */
    @Column(name = "VERSION",length = 1)
    private String version;

    /**
     * 組織編號
     */
    @Column(name = "OUCODE", length = 50)
    private String ouCode;

    /**
     * 會計科目	
     */
    @Column(name = "ACCOUNTING", length = 8)
    private String accounting;

    /**
     * 金額
     */
    @Column(name = "AMOUNT", precision = 13, scale = 2)
    private BigDecimal amount;

    /**
     * 來源名稱	
     */
    @Column(name = "TRANSCATION_SOURCE", length = 20)
    private String transcationSource;
    
    /**
     * 來源編號
     */
    @Column(name = "TRANSCATION_NO", length = 20)
    private String transcationNo;

    /**
     * 交易日期
     */
    @Column(name = "TRANSCATION_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transcationDate;
    
    /**
     * 交易類別
     */
    @Column(name = "TRANSCATION_TYPE", length = 20)
    private String transcationType;

    /**
     * 供應商代號
     */
    @Column(name = "BP_NO", length = 100)
    private String bpNo;

    /**
     * 供應商名稱
     */
    @Column(name = "BP_NAME", length = 100)
    private String bpName;

    /**
     * 簽呈號碼
     */
    @Column(name = "DOC_NO", length = 100)
    private String docNo;

    /**
     * 建檔者=上傳者
     */
    @Column(name = "CREATE_USER", length = 50)
    private String createUser;

    /**
     * 建檔日期
     */
    @Column(name = "CREATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;

}