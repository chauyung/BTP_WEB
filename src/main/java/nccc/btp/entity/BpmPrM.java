package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@Entity
@Table(name = "BPM_PR_M")
@NamedQuery(name = "BpmPrM.findAll", query = "SELECT b FROM BpmPrM b")
public class BpmPrM implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "TASKID", length = 40)
    private String taskId;

    @Id
    @Column(name = "PR_NO", length = 12, nullable = false)
    private String prNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "APPLY_DATE")
    private LocalDate applyDate;

    @Column(name = "APPLICANT", length = 20)
    private String applicant;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "DEMAND_ORDER", length = 10)
    private String demandOrder;

    @Column(name = "DEPARTMENT", length = 20)
    private String department;

    @Column(name = "DOC_NO", length = 10)
    private String docNo;

    @Column(name = "EMP_NO", length = 20)
    private String empNo;

    @Column(name = "INQUIRY_AMOUNT", precision = 13, scale = 2)
    private BigDecimal inquiryAmount;

    @Column(name = "IT_TYPE", length = 1)
    private String itType;

    @Column(name = "REMARK", length = 100)
    private String remark;

    @Column(name = "TOTAL_AMOUNT", precision = 13, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "FLOW_DEMAND_DATE", length = 1)
    private String flowDemandDate;

    @Column(name = "REMARK_ACC", length = 50)
    private String remarkAcc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "APPROVAL_DATE")
    private LocalDate approvalDate;

    @Column(name = "PO_NO", length = 12)
    private String poNo;

    @Column(name = "REV_NO", length = 12)
    private String revNo;

    @Column(name = "EMP_PO_NO", length = 20)
    private String empPoNo;

    @Column(name = "CREATED_USER", length = 50, nullable = false)
    private String createdUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "UPDATE_USER", length = 50, nullable = false)
    private String updateUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDate updateDate;

    @Column(name = "FLOW_STATUS", length = 1)
    private String flowStatus;

    @Column(name = "PR_TYPE", length = 2)
    private String prType;
}