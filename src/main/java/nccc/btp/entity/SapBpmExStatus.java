package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "SAP_BPM_EX_STATUS")
@NamedQuery(name = "SapBpmExStatus.findAll", query = "SELECT s FROM SapBpmExStatus s")
public class SapBpmExStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EX_NO")
    private String exNo;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "BUKRS")
    private String bukrs;

    @Column(name = "BELNR")
    private String belnr;

    @Column(name = "GJAHR")
    private String gjahr;

    @Column(name = "MESSAGE")
    private String message;

    public SapBpmExStatus() {}

    public String getExNo() {
        return exNo;
    }

    public void setExNo(String exNo) {
        this.exNo = exNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getBelnr() {
        return belnr;
    }

    public void setBelnr(String belnr) {
        this.belnr = belnr;
    }

    public String getGjahr() {
        return gjahr;
    }

    public void setGjahr(String gjahr) {
        this.gjahr = gjahr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
