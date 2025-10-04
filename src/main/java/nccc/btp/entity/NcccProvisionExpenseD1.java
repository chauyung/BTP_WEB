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
 * 提列費用作業項目明細檔
 */
@Getter
@Setter
@Entity
@IdClass(NcccProvisionExpenseD1.ConfigId.class)
@Table(name = "NCCC_PROVISION_EXPENSE_D1")
public class NcccProvisionExpenseD1 implements Serializable {
	private static final long serialVersionUID = 1L;
    
    /**
     * 提列單號
     */
    @Id
    @Column(name = "PROVISION_NO", length = 20)
    private String provisionNo;

    /**
     * 序號(001,002,003)
     */
    @Id
    @Column(name = "SEQ_NO", length = 3)
    private String seqNo;

    /**
     * 序號(001,002,003)
     */
    @Id
    @Column(name = "SEQ_NO1", length = 3)
    private String seqNo1;

    /**
     * 序號(001,002,003)
     */
    @Column(name = "OPERATE_ITEM_CODE", length = 10)
    private String operateItemCode;

    /**
     * 序號(001,002,003)
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
     * 建檔者
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

    public static class ConfigId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String provisionNo;
        private String seqNo;
        private String seqNo1;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(provisionNo, configId.provisionNo) &&
                Objects.equals(seqNo, configId.seqNo) &&
                Objects.equals(seqNo1, configId.seqNo1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(provisionNo, seqNo, seqNo1);
        }
    }
}
