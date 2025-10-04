package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "NCCC_EMS_MGM")
@NamedQuery(name = "NcccEmsMgm.findAll", query = "SELECT n FROM NcccEmsMgm n")
public class NcccEmsMgm implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "WEALTH_NO", length = 15)
  private String wealthNo;
  
  @Column(name = "PUR_SEQ_NO", length = 10)
  private String purSeqNo;

  @Column(name = "NCCC_WEALTH_NO", length = 20)
  private String ncccWealthNo;

  @Column(name = "EQ_TYPE", length = 10)
  private String eqType;

  @Column(name = "EQ_NAME", length = 20)
  private String eqName;

  @Column(name = "MODEL_NO", length = 10)
  private String modelNo;

  @Column(name = "MODEL_NAME", length = 20)
  private String modelName;

  @Column(name = "VENDOR_ID", length = 10)
  private String vendorId;

  @Column(name = "VENDOR_NAME", length = 20)
  private String vendorName;

  @Column(name = "STATUS", length = 10)
  private String status;

  @Column(name = "DOC_NO", length = 10)
  private String docNo;

  @Column(name = "DESCRIPTION", length = 100)
  private String description;

  @Column(name = "POSTING_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate postingDate;

  @Column(name = "SUBASSETS", length = 100)
  private String subassets;
  
  @Column(name = "LISTED", length = 1)
  private String listed;
  
  @Column(name = "IMPAIRMENT_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate impairmentDate;

  @Column(name = "CREATE_USER", length = 50)
  private String createUser;

  @Column(name = "CREATE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate createDate;

  @Column(name = "UPDATE_USER", length = 50)
  private String updateUser;

  @Column(name = "UPDATE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate updateDate;

  public NcccEmsMgm() {}
  
  @PrePersist
  @PreUpdate
  protected void onUpdate() {
    updateDate = LocalDate.now();
  }
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "WEALTH_NO", referencedColumnName = "WEALTH_NO", insertable = false, updatable = false)
  private SapEmsRecStatus sapEmsRecStatus;

  public SapEmsRecStatus getSapEmsRecStatus() {
    return sapEmsRecStatus;
  }

  public void setSapEmsRecStatus(SapEmsRecStatus sapEmsRecStatus) {
    this.sapEmsRecStatus = sapEmsRecStatus;
  }


  public String getPurSeqNo() {
    return purSeqNo;
  }

  public void setPurSeqNo(String purSeqNo) {
    this.purSeqNo = purSeqNo;
  }

  public String getNcccWealthNo() {
    return ncccWealthNo;
  }

  public void setNcccWealthNo(String ncccWealthNo) {
    this.ncccWealthNo = ncccWealthNo;
  }

  public String getWealthNo() {
    return wealthNo;
  }

  public void setWealthNo(String wealthNo) {
    this.wealthNo = wealthNo;
  }

  public String getEqType() {
    return eqType;
  }

  public void setEqType(String eqType) {
    this.eqType = eqType;
  }

  public String getEqName() {
    return eqName;
  }

  public void setEqName(String eqName) {
    this.eqName = eqName;
  }

  public String getModelNo() {
    return modelNo;
  }

  public void setModelNo(String modelNo) {
    this.modelNo = modelNo;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public String getVendorName() {
    return vendorName;
  }

  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDocNo() {
    return docNo;
  }

  public void setDocNo(String docNo) {
    this.docNo = docNo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getPostingDate() {
    return postingDate;
  }

  public void setPostingDate(LocalDate postingDate) {
    this.postingDate = postingDate;
  }

  public String getSubassets() {
    return subassets;
  }

  public void setSubassets(String subassets) {
    this.subassets = subassets;
  }

  public String getListed() {
    return listed;
  }

  public void setListed(String listed) {
    this.listed = listed;
  }

  public LocalDate getImpairmentDate() {
    return impairmentDate;
  }

  public void setImpairmentDate(LocalDate impairmentDate) {
    this.impairmentDate = impairmentDate;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
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
