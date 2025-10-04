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
 * SAP折舊預算作業項目暫存檔
 */
@Entity
@Table(name = "NCCC_SAP_DEP_EXPENSE_TMP_D")
public class NcccSapDepExpenseTmpD implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;

    /**
     * 主檔ID
     */
    @Column(name = "mID", nullable = false)
    private long mId;

    /**
     * 作業項目代號
     */
    @Column(name = "OPERATE_ITEM_CODE", length = 10)
    private String operateItemCode;

    /**
     * 作業項目
     */
    @Column(name = "OPERATE_ITEM", length = 100)
    private String operateItem;

    /**
     * 預算金額
     */
    @Column(name = "OPERATE_AMT")
    private BigDecimal operateAmt;

    /**
     * 比例
     */
    @Column(name = "OPERATE_RATIO")
    private BigDecimal operateRatio;

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
