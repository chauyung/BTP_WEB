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
 * 預算檔
 */
@Setter
@Getter
@Entity
@IdClass(NcccBudgetD.ConfigId.class)
@Table(name = "NCCC_BUDGET_D")
public class NcccBudgetD implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Year",length = 4)
    private String year;

    @Id
    @Column(name = "VERSION",length = 1)
    private String version;

    /**
     * 組織編號
     */
    @Id
    @Column(name = "OUCODE", length = 50)
    private String ouCode;

    /**
     * 會計科目	
     */
    @Id
    @Column(name = "ACCOUNTING", length = 8)
    private String accounting;

    /**
     * 作業項目代號	
     */
    @Id
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

        private String year;
        private String version;
        private String ouCode;
        private String accounting;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(year, configId.year) &&
                Objects.equals(version, configId.version) &&
                Objects.equals(ouCode, configId.ouCode) &&
                Objects.equals(accounting, configId.accounting);
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, version, ouCode, accounting);
        }
    }
}