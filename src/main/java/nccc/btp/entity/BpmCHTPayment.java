package nccc.btp.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "BPM_CHT_PAYMENT")
public class BpmCHTPayment  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_cht_payment")
    @SequenceGenerator(name = "seq_bpm_cht_payment", sequenceName = "SEQ_BPM_CHT_PAYMENT", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    @Column(name = "EX_NO", length = 12)
    private String exNo;

    @Column(name = "PAY_TYPE", length = 1)
    private String payType;

    @Column(name = "ZERO_TAX_AMOUNT", precision = 13, scale = 2)
    private BigDecimal zeroTaxAmount;

    @Column(name = "DUTY_FREE_AMOUNT", precision = 13, scale = 2)
    private BigDecimal dutyFreeAmount;

    @Column(name = "UNTAX_AMOUNT", precision = 13, scale = 2)
    private BigDecimal untaxAmount;

    @Column(name = "TAX_AMOUNT", precision = 13, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "APPLY_AMOUNT", precision = 13, scale = 2)
    private BigDecimal applyAmount;

    //載具號碼
    @Column(name = "CARRIER_NUMBER", length = 30)
    private String carrierNumber;

    //用戶號碼
    @Column(name = "PHONE", length = 50)
    private String phone;

    @Column(name = "BAN", length = 8)
    private String ban;

    @Column(name = "NOTIFICATION_NUMBER", length = 15)
    private String notificationNumber;

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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getZeroTaxAmount() {
        return zeroTaxAmount;
    }

    public void setZeroTaxAmount(BigDecimal zeroTaxAmount) {
        this.zeroTaxAmount = zeroTaxAmount;
    }

    public BigDecimal getDutyFreeAmount() {
        return dutyFreeAmount;
    }

    public void setDutyFreeAmount(BigDecimal dutyFreeAmount) {
        this.dutyFreeAmount = dutyFreeAmount;
    }

    public BigDecimal getUntaxAmount() {
        return untaxAmount;
    }

    public void setUntaxAmount(BigDecimal untaxAmount) {
        this.untaxAmount = untaxAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getCarrierNumber() {
        return carrierNumber;
    }

    public void setCarrierNumber(String carrierNumber) {
        this.carrierNumber = carrierNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public String getNotificationNumber() {
        return notificationNumber;
    }

    public void setNotificationNumber(String notificationNumber) {
        this.notificationNumber = notificationNumber;
    }
}
