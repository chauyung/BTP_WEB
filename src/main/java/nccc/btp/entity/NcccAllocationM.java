package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
 * 權責分攤主檔
 */
@Setter
@Getter
@Entity
@IdClass(NcccAllocationM.ConfigId.class)
@Table(name = "NCCC_ALLOCATION_M")
public class NcccAllocationM implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 採購單編號
     */
    @Id
    @Column(name = "PO_NO",length = 12)
    private String poNo;

    /**
     * 項次
     */
    @Id
    @Column(name = "PO_ITEM_NO",length = 5)
    private String poItemNo;

    /**
     * 品名（品號）
     */
    @Column(name = "ITEM_CODE",length = 10)
    private String itemCode;

    /**
     * 預算項目代號
     */
    @Column(name = "ACCOUNTING",length = 8)
    private String accounting;

    /**
     * 會計科目短文
     */
    @Column(name = "SUBJECT",length = 30)
    private String subject;  

    /**
     * 預算部門代號
     */
    @Column(name = "OUCODE",length = 20)
    private String ouCode;    

    /**
     * 預算部門名稱
     */
    @Column(name = "OUNAME",length = 40)
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
    @Column(name = "ITEM_TEXT",length = 50)
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
    private LocalDate createDate;


    public static class ConfigId implements Serializable{
        private static final long serialVersionUID = 1L;

        private String poNo;
        private String poItemNo;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(poNo, configId.poNo) &&
                Objects.equals(poItemNo, configId.poItemNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(poNo, poItemNo);
        }
    }
}