package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "BPM_BT_M")
public class BpmBtM {

  @Id
  @Column(name = "BT_NO", length = 12)
  private String btNo;

  @Column(name = "TASKID", length = 40)
  private String taskId;

  @Column(name = "APPLY_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate applyDate;

  @Column(name = "APPLY_TYPE", length = 10)
  private String applyType;

  @Column(name = "APPLICANT", length = 20)
  private String applicant;

  @Column(name = "EMP_NO", length = 20)
  private String empNo;

  @Column(name = "DEPATMENT", length = 20)
  private String department;

  @Column(name = "TRAVEL_NO", length = 20)
  private String travelNo;

  @Column(name = "TRAVEL_START", length = 50)
  private String travelStart;

  @Column(name = "TRAVEL_LOCATION", length = 50)
  private String travelLocation;

  @Column(name = "APPLY_AMOUNT", precision = 13, scale = 2)
  private BigDecimal applyAmount;

  @Column(name = "TAX", precision = 13, scale = 2)
  private BigDecimal tax;

  @Column(name = "UNTAX_AMOUNT", precision = 13, scale = 2)
  private BigDecimal untaxAmount;

  @Column(name = "TRIP_MEMBER", length = 50)
  private String tripMember;

  @Column(name = "LEV_CONFIRM_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate levConfirmDate;

  @Column(name = "LEV_APPLY_DATE", length = 35)
  private String levApplyDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  @Column(name = "ACTUAL_LEV_DATE")
  private LocalDateTime actualLevDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  @Column(name = "ACTUAL_LEV_DATE_END")
  private LocalDateTime actualLevDateEnd;

  @Column(name = "SPECIAL_PAY_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate specialPayDate;

  @Column(name = "PAY_EMP_NO", length = 20)
  private String payEmpNo;

  @Column(name = "PAY_NAME", length = 20)
  private String payName;

  @Column(name = "REASON", length = 100)
  private String reason;

  @Column(name = "REASON2", length = 100)
  private String reason2;

  @Column(name = "POSTING_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate postingDate;

  @Lob
  @Column(name = "ATTACHMENT")
  private byte[] attachment;

  @Column(name = "ITEM_TEXT", length = 50)
  private String itemText;

  @Column(name = "SAP_RESULT", length = 1)
  private String sapResult;

  @Column(name = "CREATED_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate createdDate;

  @Column(name = "MODIFIED_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate modifiedDate;

  @Column(name = "CREATED_USER", length = 10)
  private String createdUser;

  @Column(name = "MODIFIED_USER", length = 10)
  private String modifiedUser;

  @Column(name = "IS_SELF_DRIVE", length = 1)
  private String isSelfDrive;

  @Column(name = "ODOMETER_START", precision = 9, scale = 1)
  private BigDecimal odometerStart;

  @Column(name = "ODOMETER_END", precision = 9, scale = 1)
  private BigDecimal odometerEnd;

  @Column(name = "CLAIMABLE_MILEAGE", precision = 9, scale = 1)
  private BigDecimal claimableMileage;

  @Column(name = "FUEL_UNIT_PRICE", precision = 9, scale = 1)
  private BigDecimal fuelUnitPrice;

  @Column(name = "FUEL_TOTAL_COST", precision = 9, scale = 1)
  private BigDecimal fuelTotalCost;

  @Column(name = "YEAR", length = 4)
  private String year;

  // 1.進行中2.已結案.3.已作廢
  @Column(name = "FLOW_STATUS", length = 1)
  private String flowStatus;

  public String getBtNo() {
    return btNo;
  }

  public void setBtNo(String btNo) {
    this.btNo = btNo;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public LocalDate getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(LocalDate applyDate) {
    this.applyDate = applyDate;
  }

  public String getApplyType() {
    return applyType;
  }

  public void setApplyType(String applyType) {
    this.applyType = applyType;
  }

  public String getApplicant() {
    return applicant;
  }

  public void setApplicant(String applicant) {
    this.applicant = applicant;
  }

  public String getEmpNo() {
    return empNo;
  }

  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getTravelNo() {
    return travelNo;
  }

  public void setTravelNo(String travelNo) {
    this.travelNo = travelNo;
  }

  public String getTravelStart() {
    return travelStart;
  }

  public void setTravelStart(String travelStart) {
    this.travelStart = travelStart;
  }

  public String getTravelLocation() {
    return travelLocation;
  }

  public void setTravelLocation(String travelLocation) {
    this.travelLocation = travelLocation;
  }

  public BigDecimal getApplyAmount() {
    return applyAmount;
  }

  public void setApplyAmount(BigDecimal applyAmount) {
    this.applyAmount = applyAmount;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BigDecimal getUntaxAmount() {
    return untaxAmount;
  }

  public void setUntaxAmount(BigDecimal untaxAmount) {
    this.untaxAmount = untaxAmount;
  }

  public String getTripMember() {
    return tripMember;
  }

  public void setTripMember(String tripMember) {
    this.tripMember = tripMember;
  }

  public LocalDate getLevConfirmDate() {
    return levConfirmDate;
  }

  public void setLevConfirmDate(LocalDate levConfirmDate) {
    this.levConfirmDate = levConfirmDate;
  }

  public String getLevApplyDate() {
    return levApplyDate;
  }

  public void setLevApplyDate(String levApplyDate) {
    this.levApplyDate = levApplyDate;
  }

  public LocalDateTime getActualLevDate() {
    return actualLevDate;
  }

  public void setActualLevDate(LocalDateTime actualLevDate) {
    this.actualLevDate = actualLevDate;
  }

  public LocalDateTime getActualLevDateEnd() {
    return actualLevDateEnd;
  }

  public void setActualLevDateEnd(LocalDateTime actualLevDateEnd) {
    this.actualLevDateEnd = actualLevDateEnd;
  }

  public LocalDate getSpecialPayDate() {
    return specialPayDate;
  }

  public void setSpecialPayDate(LocalDate specialPayDate) {
    this.specialPayDate = specialPayDate;
  }

  public String getPayEmpNo() {
    return payEmpNo;
  }

  public void setPayEmpNo(String payEmpNo) {
    this.payEmpNo = payEmpNo;
  }

  public String getPayName() {
    return payName;
  }

  public void setPayName(String payName) {
    this.payName = payName;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getReason2() {
    return reason2;
  }

  public void setReason2(String reason2) {
    this.reason2 = reason2;
  }

  public LocalDate getPostingDate() {
    return postingDate;
  }

  public void setPostingDate(LocalDate postingDate) {
    this.postingDate = postingDate;
  }

  public byte[] getAttachment() {
    return attachment;
  }

  public void setAttachment(byte[] attachment) {
    this.attachment = attachment;
  }

  public String getItemText() {
    return itemText;
  }

  public void setItemText(String itemText) {
    this.itemText = itemText;
  }

  public String getSapResult() {
    return sapResult;
  }

  public void setSapResult(String sapResult) {
    this.sapResult = sapResult;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDate getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDate modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public String getCreatedUser() {
    return createdUser;
  }

  public void setCreatedUser(String createdUser) {
    this.createdUser = createdUser;
  }

  public String getModifiedUser() {
    return modifiedUser;
  }

  public void setModifiedUser(String modifiedUser) {
    this.modifiedUser = modifiedUser;
  }

  public String getIsSelfDrive() {
    return isSelfDrive;
  }

  public void setIsSelfDrive(String isSelfDrive) {
    this.isSelfDrive = isSelfDrive;
  }

  public BigDecimal getOdometerStart() {
    return odometerStart;
  }

  public void setOdometerStart(BigDecimal odometerStart) {
    this.odometerStart = odometerStart;
  }

  public BigDecimal getOdometerEnd() {
    return odometerEnd;
  }

  public void setOdometerEnd(BigDecimal odometerEnd) {
    this.odometerEnd = odometerEnd;
  }

  public BigDecimal getClaimableMileage() {
    return claimableMileage;
  }

  public void setClaimableMileage(BigDecimal claimableMileage) {
    this.claimableMileage = claimableMileage;
  }

  public BigDecimal getFuelUnitPrice() {
    return fuelUnitPrice;
  }

  public void setFuelUnitPrice(BigDecimal fuelUnitPrice) {
    this.fuelUnitPrice = fuelUnitPrice;
  }

  public BigDecimal getFuelTotalCost() {
    return fuelTotalCost;
  }

  public void setFuelTotalCost(BigDecimal fuelTotalCost) {
    this.fuelTotalCost = fuelTotalCost;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getFlowStatus() {
    return flowStatus;
  }

  public void setFlowStatus(String flowStatus) {
    this.flowStatus = flowStatus;
  }
}
