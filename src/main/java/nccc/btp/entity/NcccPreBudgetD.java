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
 * 預算作業項目明細檔
 */
@Getter
@Setter
@Entity
@IdClass(NcccPreBudgetD.ConfigId.class)
@Table(name = "NCCC_PRE_BUDGET_D")
public class NcccPreBudgetD implements Serializable {
	private static final long serialVersionUID = 1L;
   
    /**
     * 預算單號
     */
    @Id
    @Column(name = "BUDGET_NO", length = 20)
    private String budgetNo;

    /**
     * 序號(001,002,003)
     */
    @Id
    @Column(name = "SEQ_NO", length = 3)
    private String seqNo;  

    /**
     * 預算項目代號
     */
    @Column(name = "ACCOUNTING", length = 8)
    private String accounting;

    /**
     * 預算金額
     */
    @Column(name = "BUD_AMOUNT")
    private BigDecimal budAmount;

    /**
     * 摘要說明
     */
    @Column(name = "REMARK", length = 500)
    private String remark;

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

        private String budgetNo;

        private String seqNo;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(budgetNo, configId.budgetNo) &&
                Objects.equals(seqNo, configId.seqNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(budgetNo, seqNo);
        }
    }
}
