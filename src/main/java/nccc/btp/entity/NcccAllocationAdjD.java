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
 * 權責分攤調整明細檔
 */
@Setter
@Getter
@Entity
@IdClass(NcccAllocationAdjD.ConfigId.class)
@Table(name = "NCCC_ALLOCATION_ADJ_D")
public class NcccAllocationAdjD implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 權責調整單號
     */
    @Id
    @Column(name = "ALL_ADJ_NO",length = 20)
    private String allAdjNo;

    /**
     * 序號(001,002,003)
     */
    @Id
    @Column(name = "SEQ_NO", length = 3)
    private String seqNo;

    /**
     * 採購單編號
     */
    @Column(name = "PO_NO",length = 12)
    private String poNo;

    /**
     * 採購單項次
     */
    @Column(name = "PO_ITEM_NO",length = 5)
    private String poItemNo;

    /**
     * 預算項目代號
     */
    @Column(name = "ACCOUNTING",length = 8)
    private String accounting;

    /**
     * 會計科目短文
     */
    @Column(name = "SUBJECT",length = 40)
    private String subject;  

    /**
     * 預算部門代號
     */
    @Column(name = "OUCODE",length = 20)
    private String ouCode;    

    /**
     * 預算部門名稱
     */
    @Column(name = "OUNAME",length = 30)
    private String ouName;

    /**
     * 供應商代號
     */
    @Column(name = "BP_NO",length = 256)
    private String bpNo;    

    /**
     * 供應商
     */
    @Column(name = "BP_NAME",length = 256)
    private String bpName;


    /**
     * 未稅金額
     */
    @Column(name = "TOTAL")
    private BigDecimal total;

    /**
     * 餘量不收金額
     */
    @Column(name = "CALCEL_TOTAL")
    private BigDecimal calcelTotal;

    /**
     * 可權責金額
     */
    @Column(name = "TOTAL_REMAIN")
    private BigDecimal totalRemain; 

    /**
     * 項目內文 
     */
    @Column(name = "ITEM_TEXT",length = 100)
    private String itemText;

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

    public static class ConfigId implements Serializable{
        private static final long serialVersionUID = 1L;

        private String allAdjNo;
        private String seqNo;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(allAdjNo, configId.allAdjNo) &&
                Objects.equals(seqNo, configId.seqNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(allAdjNo, seqNo);
        }
    }
}