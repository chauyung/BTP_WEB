package nccc.btp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "NCCC_RESERVER_CONTROLLER")
@NamedQuery(name = "NcccReserverController.findAll", query = "SELECT n FROM NcccReserverController n")
public class NcccReserverController  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RESERVER_CONTROLLER_NO")
    private String reserverControllerNo;

    @Column(name = "RESERVER_CONTROLLER_DES")
    private String reserverControllerDes;

    @Column(name = "UPDATE_USER", length = 50)
    private String updateUser;

    @Column(name = "UPDATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDate.now();
    }

    public NcccReserverController() {}

    public String getReserverControllerNo() {
        return reserverControllerNo;
    }

    public void setReserverControllerNo(String reserverControllerNo) {
        this.reserverControllerNo = reserverControllerNo;
    }

    public String getReserverControllerDes() {
        return reserverControllerDes;
    }

    public void setReserverControllerDes(String reserverControllerDes) {
        this.reserverControllerDes = reserverControllerDes;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
