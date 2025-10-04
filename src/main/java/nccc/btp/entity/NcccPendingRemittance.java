package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "NCCC_PENDING_REMITTANCE")
@NamedQuery(name = "NcccPendingRemittance.findAll", query = "SELECT n FROM NcccPendingRemittance n")
public class NcccPendingRemittance implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "BANK_NO", length = 8)
  private String bankNo;

  @Column(name = "DEPOSIT_DATE", length = 8)
  private String depositDate;

  @Column(name = "DEPOSIT_DESC", length = 500)
  private String depositDesc;

  @Column(name = "DEPOSIT_AMOUNT", precision = 13, scale = 2)
  private BigDecimal depositAmount;

  @Column(name = "WRITE_OFF_DATE", length = 8)
  private String writeOffDate;

  @Column(name = "CHECKOUT_DATE", length = 8)
  private String checkoutDate;

  @Column(name = "WRITE_OFF_AMOUNT", precision = 13, scale = 3)
  private BigDecimal writeOffAmount;

  @Column(name = "OFFSET_AMOUNT", precision = 13, scale = 3)
  private BigDecimal offsetAmount;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

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
  
  @Column(name = "DESCRIPTION_USER", length = 50)
  private String descriptionUser;

  @Column(name = "DESCRIPTION_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate descriptionDate;

  @Column(name = "DESCRIPTION_OU_CODE", length = 50)
  private String descriptionOuCode;

  public NcccPendingRemittance() {}


  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
  private SapPendingRemittanceStatus sapPendingRemittanceStatus;

  public SapPendingRemittanceStatus getSapPendingRemittanceStatus() {
    return sapPendingRemittanceStatus;
  }

  public void setSapPendingRemittanceStatus(SapPendingRemittanceStatus sapPendingRemittanceStatus) {
    this.sapPendingRemittanceStatus = sapPendingRemittanceStatus;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBankNo() {
    return bankNo;
  }

  public void setBankNo(String bankNo) {
    this.bankNo = bankNo;
  }

  public String getDepositDate() {
    return depositDate;
  }

  public void setDepositDate(String depositDate) {
    this.depositDate = depositDate;
  }

  public String getDepositDesc() {
    return depositDesc;
  }

  public void setDepositDesc(String depositDesc) {
    this.depositDesc = depositDesc;
  }

  public BigDecimal getDepositAmount() {
    return depositAmount;
  }

  public void setDepositAmount(BigDecimal depositAmount) {
    this.depositAmount = depositAmount;
  }

  public String getWriteOffDate() {
    return writeOffDate;
  }

  public void setWriteOffDate(String writeOffDate) {
    this.writeOffDate = writeOffDate;
  }

  public String getCheckoutDate() {
    return checkoutDate;
  }

  public void setCheckoutDate(String checkoutDate) {
    this.checkoutDate = checkoutDate;
  }

  public BigDecimal getWriteOffAmount() {
    return writeOffAmount;
  }

  public void setWriteOffAmount(BigDecimal writeOffAmount) {
    this.writeOffAmount = writeOffAmount;
  }

  public BigDecimal getOffsetAmount() {
    return offsetAmount;
  }

  public void setOffsetAmount(BigDecimal offsetAmount) {
    this.offsetAmount = offsetAmount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
  
  public String getDescriptionUser() {
    return descriptionUser;
  }

  public void setDescriptionUser(String descriptionUser) {
    this.descriptionUser = descriptionUser;
  }
  
  public LocalDate getDescriptionDate() {
    return descriptionDate;
  }

  public void setDescriptionDate(LocalDate descriptionDate) {
    this.descriptionDate = descriptionDate;
  }

  public String getDescriptionOuCode() {
    return descriptionOuCode;
  }

  public void setDescriptionOuCode(String descriptionOuCode) {
    this.descriptionOuCode = descriptionOuCode;
  }
}
