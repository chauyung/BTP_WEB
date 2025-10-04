package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table(name = "BPM_PREPAID_ORDER")
public class BpmPrepaidOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_prepaid_order")
  @SequenceGenerator(name = "seq_bpm_prepaid_order", sequenceName = "SEQ_BPM_PREPAID_ORDER", allocationSize = 1)
  @Column(name = "ID", nullable = false, precision = 38, scale = 0)
  private Long id;

  @Column(name = "EX_NO", length = 20)
  private String exNo;

  @Column(name = "M_NO", length = 20)
  private String mNo;

  @Column(name = "TOTAL_AMOUNT", precision = 13, scale = 2)
  private BigDecimal totalAmount;

  @Column(name = "PAY_AMOUNT", precision = 13, scale = 2)
  private BigDecimal payAmount;

  @Column(name = "REFUND_AMOUNT", precision = 13, scale = 2)
  private BigDecimal refundAmount;

  @Column(name = "REFUND_DATE")
  private LocalDate refundDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getExNo() {
    return exNo;
  }

  public void setExNo(String exNo) {
    this.exNo = exNo;
  }

  public String getMNo() {
    return mNo;
  }

  public void setMNo(String mNo) {
    this.mNo = mNo;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public BigDecimal getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(BigDecimal payAmount) {
    this.payAmount = payAmount;
  }

  public BigDecimal getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(BigDecimal refundAmount) {
    this.refundAmount = refundAmount;
  }

  public LocalDate getRefundDate() {
    return refundDate;
  }

  public void setRefundDate(LocalDate refundDate) {
    this.refundDate = refundDate;
  }

}
