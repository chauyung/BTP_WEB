package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * NCCC 分攤規則主表
 */
@Setter
@Getter
@Entity
@IdClass(NcccApportionmentRuleM.ConfigId.class)
@Table(name = "NCCC_Apportionment_RULE_M")
public class NcccApportionmentRuleM implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "YEAR",length = 4)
    private String year;

    @Column(name = "DESCRIPTION",length = 50)
    private String description;
    @Id
    @Column(name = "MONTH",length = 2)
    private String month;
    @Id
    @Column(name = "ACCOUNTING",length = 8)
    private String accounting;

    @Column(name = "SUBJECT",length = 50)
    private String subject;

    @Column(name = "BELONG_OU_CODE",length = 4)
    private String belongOuCode;

    @Column(name = "BELONG_OU_NAME",length = 50)
    private String belongOuName;

    @Column(name = "ACTUAL_QTY_FLAG",length = 1)
    private String actualQtyFlag;

    @Column(name = "OPERATION_TYPE",length = 1)
    private String operationType;

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
        private String accounting;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NcccApportionmentRuleM.ConfigId)) return false;
            NcccApportionmentRuleM.ConfigId configId = (NcccApportionmentRuleM.ConfigId) o;
            return Objects.equals(year, configId.year) &&
                    Objects.equals(month, configId.month) &&
                    Objects.equals(accounting, configId.accounting);
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, month,accounting);
        }
    }
}
