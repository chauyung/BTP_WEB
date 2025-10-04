package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * NCCC 分攤規則明細
 */
@Setter
@Getter
@Entity
@IdClass(NcccApportionmentRuleD.ConfigId.class)
@Table(name = "NCCC_Apportionment_RULE_D")
public class NcccApportionmentRuleD implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ACCOUNTING",length = 8)
    private String accounting;
    @Id
    @Column(name = "YEAR",length = 4)
    private String year;
    @Id
    @Column(name = "MONTH",length = 2)
    private String month;
    @Id
    @Column(name = "OU_CODE",length = 20)
    private String ouCode;

    @Column(name = "OU_NAME",length = 40)
    private String ouName;

    @Column(name = "UNIT_QTY")
    private BigDecimal unitQty;

    @Column(name = "REMARK",length = 30)
    private String remark;

    @Column(name = "CREATE_USER",length = 10)
    private String createUser;

    @Column(name = "CREATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @Column(name = "UPDATE_USER",length = 10)
    private String updateUser;

    @Column(name = "UPDATE_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;

    public static class ConfigId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String year;
        private String month;
        private String ouCode;
        private String accounting;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NcccApportionmentRuleD.ConfigId)) return false;
            NcccApportionmentRuleD.ConfigId configId = (NcccApportionmentRuleD.ConfigId) o;
            return Objects.equals(year, configId.year) &&
                    Objects.equals(month, configId.month) &&
                    Objects.equals(ouCode, configId.ouCode)&&
                    Objects.equals(accounting, configId.accounting);
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, month, ouCode,accounting);
        }
    }
}
