package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * 提列費用明細（NCCC_PROVISION_EXPENSE_D）
 * 主鍵：PROVISION_NO + SEQ_NO
 */
@Getter
@Setter
@Entity
@IdClass(NcccProvisionExpenseD.ConfigId.class)
@Table(name = "NCCC_PROVISION_EXPENSE_D")
public class NcccProvisionExpenseD implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 提列單號 */
    @Id
    @Column(name = "PROVISION_NO", length = 20, nullable = false)
    private String provisionNo;

    /** 序號(001,002,003) */
    @Id
    @Column(name = "SEQ_NO", length = 3, nullable = false)
    private String seqNo;

    /** 預算部門代號 */
    @Column(name = "OUCODE", length = 20)
    private String ouCode;

    /** 分攤方式（1:指定 2:原比例） */
    @Column(name = "ALLOCATION_METHOD", length = 10)
    private String allocateMethod;

    /** 提列數(+-) */
    @Column(name = "AMOUNT")
    private BigDecimal amount;

    /** 摘要說明 */
    @Column(name = "REMARK", length = 100)
    private String remark;

    /** 建檔者 */
    @Column(name = "CREATE_USER", length = 10)
    private String createUser;

    /** 建檔日期 */
    @Column(name = "CREATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;

    /** 更新者 */
    @Column(name = "UPDATE_USER", length = 10)
    private String updateUser;

    /** 更新日期 */
    @Column(name = "UPDATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDate;

    /** 組合鍵類別 */
    public static class ConfigId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String provisionNo;
        private String seqNo;

        public ConfigId() {}

        public ConfigId(String provisionNo, String seqNo) {
            this.provisionNo = provisionNo;
            this.seqNo = seqNo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId that = (ConfigId) o;
            return Objects.equals(provisionNo, that.provisionNo)
                && Objects.equals(seqNo, that.seqNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(provisionNo, seqNo);
        }
    }
}
