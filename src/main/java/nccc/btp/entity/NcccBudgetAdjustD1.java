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

/**
 * NCCC 預算調撥作業項目明細檔（D1：對應 D 的子項）
 */
@Setter
@Getter
@Entity
@IdClass(NcccBudgetAdjustD1.ConfigId.class)
@Table(
    name = "NCCC_BUDGET_ADJUST_D1",
    indexes = {
        @Index(name = "IDX_BUDGET_ADJ_D1_ADJ_NO", columnList = "ADJ_NO"),
        @Index(name = "IDX_BUDGET_ADJ_D1_ADJ_NO_SEQ_NO", columnList = "ADJ_NO,SEQ_NO")
    }
)
public class NcccBudgetAdjustD1 implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 調撥編號（對應主檔/明細檔的 ADJ_NO） */
    @Id
    @Column(name = "ADJ_NO", length = 20, nullable = false)
    private String adjNo;

    /** D 明細序號(001,002,003) */
    @Id
    @Column(name = "SEQ_NO", length = 3, nullable = false)
    private String seqNo;

    /** D1 子序號(001,002,003) */
    @Id
    @Column(name = "SEQ_NO1", length = 3, nullable = false)
    private String seqNo1;

    /** 作業項目代號 */
    @Column(name = "OPERATE_ITEM_CODE", length = 10)
    private String operateItemCode;

    /** 作業項目 */
    @Column(name = "OPERATE_ITEM", length = 100)
    private String operateItem;

    /** 預算金額 */
    @Column(name = "OPERATE_AMT")
    private BigDecimal operateAmt;

    /** 比例 */
    @Column(name = "OPERATE_RATIO")
    private BigDecimal operateRatio;

    /** 建檔者=上傳者 */
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

    /**
     * ★ 回到 D 明細（父層）：用 ADJ_NO + SEQ_NO 關聯
     *   - D1 是 D 的子列，通常不需要把 SEQ_NO1 也放進關聯條件
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "ADJ_NO", referencedColumnName = "ADJ_NO", insertable = false, updatable = false),
        @JoinColumn(name = "SEQ_NO", referencedColumnName = "SEQ_NO", insertable = false, updatable = false)
    })
    private NcccBudgetAdjustD parentDetail;

    /** 複合鍵（駝峰命名，與欄位屬性一致） */
    @NoArgsConstructor
    public static class ConfigId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String adjNo;
        private String seqNo;
        private String seqNo1;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId that = (ConfigId) o;
            return Objects.equals(adjNo, that.adjNo)
                && Objects.equals(seqNo, that.seqNo)
                && Objects.equals(seqNo1, that.seqNo1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(adjNo, seqNo, seqNo1);
        }
    }
}
