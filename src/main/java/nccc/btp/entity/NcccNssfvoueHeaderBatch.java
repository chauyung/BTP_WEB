package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "NCCC_NSSFVOUE_HEADER_BATCH")
@NamedQuery(name = "NcccNssfvoueHeaderBatch.findAll",
    query = "SELECT n FROM NcccNssfvoueHeaderBatch n")
public class NcccNssfvoueHeaderBatch implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "NSSFVOUE_HEADER_BATCH")
  private String nssfvoueHeaderBatch;

  @Column(name = "DOC_TYPE")
  private String docType;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "UPDATE_USER", length = 50)
  private String updateUser;

  @Column(name = "UPDATE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate updateDate;

  public NcccNssfvoueHeaderBatch() {}

  @PrePersist
  @PreUpdate
  protected void onUpdate() {
    updateDate = LocalDate.now();
  }

  public String getNssfvoueHeaderBatch() {
    return nssfvoueHeaderBatch;
  }

  public void setNssfvoueHeaderBatch(String nssfvoueHeaderBatch) {
    this.nssfvoueHeaderBatch = nssfvoueHeaderBatch;
  }

  public String getDocType() {
    return docType;
  }

  public void setDocType(String docType) {
    this.docType = docType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }

  public LocalDate getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(LocalDate updateDate) {
    this.updateDate = updateDate;
  }

}
