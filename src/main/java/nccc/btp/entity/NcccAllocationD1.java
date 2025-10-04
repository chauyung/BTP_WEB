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
 * 權責分攤作業項目明細檔
 */
@Setter
@Getter
@Entity
@IdClass(NcccAllocationD1.ConfigId.class)
@Table(name = "NCCC_ALLOCATION_D1")
public class NcccAllocationD1 implements Serializable {
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
     * 序號(001,002,003)
     */
    @Id
    @Column(name = "SEQ_NO1", length = 3)
    private String seqNo1;

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
     * 金額
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
    @Column(name = "CREATE_USER", length = 50)
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
    @Column(name = "UPDATE_USER", length = 50)
    private String updateUser;

    /**
     * 更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;

    public static class ConfigId implements Serializable{
        private static final long serialVersionUID = 1L;

        private String poNo;
        private String poItemNo;
        private String seqNo1;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(poNo, configId.poNo) &&
                Objects.equals(poItemNo, configId.poItemNo)&&
                Objects.equals(seqNo1, configId.seqNo1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(poNo, poItemNo, seqNo1);
        }
    }
}