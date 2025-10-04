package nccc.btp.entity;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(NcccBudgetVersion.ConfigId.class)
@Table(name = "NCCC_BUDGET_VERSION")
public class NcccBudgetVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Year",length = 4)
    private String year;

    @Id
    @Column(name = "VERSION",length = 1)
    private String version;

    @Column(name = "CREATE_USER",length = 10)
    private String createUser;

    @Column(name = "CREATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @Column(name = "OPEN_USER",length = 10)
    private String openUser;

    @Column(name = "OPEN_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openDate;

    @Column(name = "CLOSE_USER",length = 10)
    private String closeUser;

    @Column(name = "CLOSE_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    public static class ConfigId implements Serializable{
        private static final long serialVersionUID = 1L;

        private String year;
        private String version;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(year, configId.year) &&
                Objects.equals(version, configId.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, version);
        }
    }
}
