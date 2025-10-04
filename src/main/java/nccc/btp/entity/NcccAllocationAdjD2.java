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
 * 權責分攤期數明細檔
 */
@Setter
@Getter
@Entity
@IdClass(NcccAllocationAdjD2.ConfigId.class)
@Table(name = "NCCC_ALLOCATION_ADJ_D2")
public class NcccAllocationAdjD2 implements Serializable {
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
     * 期數
     */
    @Id
    @Column(name = "PLAN", length = 3)
    private String plan;


    /**
     * 預算年度
     */
    @Column(name = "YEAR", length = 4)
    private String year;

    /**
     * 月份
     */
    @Column(name = "MONTH", length = 2)
    private String month;

    /**
     * 權責金額
     */
    @Column(name = "ALLOCATION_TOTAL")
    private BigDecimal allocationTotal;

    /**
     * 調整金額
     */
    @Column(name = "ADJUST_TOTAL")
    private BigDecimal adjustTotal;

    /**
     * 調整後權責金額
     */
    @Column(name = "FINAL_TOTAL")
    private BigDecimal finalTotal; 

    /**
     * SAP會計文件號碼 
     */
    @Column(name = "SAP_DOC_NO",length = 30)
    private String sapDocNo;

    /**
     * 指定過帳日
     */
    @Column(name = "POST_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate postDate;

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
    private LocalDateTime updateDate;


    public static class ConfigId implements Serializable{
        private static final long serialVersionUID = 1L;

        private String allAdjNo;
        private String seqNo;
        private String plan;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(allAdjNo, configId.allAdjNo) &&
                Objects.equals(seqNo, configId.seqNo)&&
                Objects.equals(plan, configId.plan);
        }

        @Override
        public int hashCode() {
            return Objects.hash(allAdjNo, seqNo, plan);
        }
    }
}