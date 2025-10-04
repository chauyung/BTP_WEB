package nccc.btp.entity;

import javax.persistence.*;

@Entity
@Table(name = "BPM_CLOSED_YEAR")
public class BpmClosedYear {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_closed_year")
    @SequenceGenerator(name = "seq_bpm_closed_year", sequenceName = "SEQ_BPM_CLOSED_YEAR", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    // 年度
    @Column(name = "YEAR", length = 4)
    private String year;

    // 關帳
    @Column(name = "CLOSED", length = 1)
    private String closed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }
}
