package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** NCCC 預算調撥明細檔 */
@Setter
@Getter
@Entity
@IdClass(NcccBudgetAdjustD.ConfigId.class)
@Table(name = "NCCC_BUDGET_ADJUST_D",
       indexes = {
           @Index(name = "IDX_BUDGET_ADJ_D_ADJ_NO", columnList = "ADJ_NO")
       })
public class NcccBudgetAdjustD implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 調撥編號（與主檔關聯） */
    @Id
    @Column(name = "ADJ_NO", length = 20, nullable = false)
    private String adjNo;

    /** 序號(001,002,003) */
    @Id
    @Column(name = "SEQ_NO", length = 3, nullable = false)
    private String seqNo;

    /** 勻出部門 */
    @Column(name = "OUT_OUCODE", length = 50)
    private String outOuCode;

    /** 勻出部門經辦 */
    @Column(name = "OUT_OUCODE_USER", length = 50)
    private String outOuCodeUser;

    /** 勻出預算科目 */
    @Column(name = "OUT_ACCOUNTING", length = 8)
    private String outAccounting;

    /** 勻出預算餘額 */
    @Column(name = "OUT_BALANCE")
    private BigDecimal outBalance;

    /** 勻入部門 */
    @Column(name = "IN_OUCODE", length = 50)
    private String inOuCode;

    /** 勻入部門經辦 */
    @Column(name = "IN_OUCODE_USER", length = 50)
    private String inOuCodeUser;

    /** 勻入預算科目 */
    @Column(name = "IN_ACCOUNTING", length = 8)
    private String inAccounting;

    /** 勻入預算餘額 */
    @Column(name = "IN_BALANCE")
    private BigDecimal inBalance;

    /** 金額 */
    @Column(name = "ADJUST_AMT")
    private BigDecimal adjustAmt;

    /** 建檔者 */
    @Column(name = "CREATE_USER", length = 10)
    private String createUser;

    /** 建檔日期 */
    @Column(name = "CREATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    /** 更新者 */
    @Column(name = "UPDATE_USER", length = 10)
    private String updateUser;

    /** 更新日期 */
    @Column(name = "UPDATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;

    /** 複合鍵類別（駝峰命名） */
    @NoArgsConstructor
    public static class ConfigId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String adjNo;
        private String seqNo;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId that = (ConfigId) o;
            return Objects.equals(adjNo, that.adjNo) &&
                   Objects.equals(seqNo, that.seqNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(adjNo, seqNo);
        }
    }
    
    /** 回到主檔，用 ADJ_NO 關聯；mappedBy=master 對應到 M.details */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADJ_NO", referencedColumnName = "ADJ_NO", insertable = false, updatable = false)
    private NcccBudgetAdjustM master;

    @Getter @Setter @NoArgsConstructor
    public static class PK implements Serializable {
        private String adjNo;
        private String seqNo;
    }
}
