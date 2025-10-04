package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 權責分攤調整主檔
 */
@Setter
@Getter
@Entity
@Table(name = "NCCC_ALLOCATION_ADJ_M")
public class NcccAllocationAdjM implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 權責調整單號
     */
    @Id
    @Column(name = "ALL_ADJ_NO",length = 20)
    private String allAdjNo;

    /**
     * 任務編號
     */
    @Column(name = "TASKID",length = 10)
    private String taskID;

    /**
     * 日期
     */
    @Column(name = "APPLY_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate applyDate;

    /**
     * 員工代號
     */
    @Column(name = "APPLY_USER", length = 20)
    private String applyUser;

    /**
     * 部門
     */
    @Column(name = "APPLY_OUCODE", length = 20)
    private String applyOuCode;

    /**
     * 結案碼Y:結案
     */
    @Column(name = "CLOSE_STATUS", length = 1)
    private String closeStatus; 

    /**
     * 結案日期
     */
    @Column(name = "CLOSE_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime closeDate;
 
    /**
     * 作廢碼Y:作廢
     */
    @Column(name = "VOID_STATUS", length = 1)
    private String voidStatus;

    /**
     * 作廢日期
     */
    @Column(name = "VOID_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime voidDate;


    /**
     * 建檔者=上傳者
     */
    @Column(name = "CREATE_USER", length = 50)
    private String createUser;

    /**
     * 建檔日期
     */
    @Column(name = "CREATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;

    /** 流程狀態 */
    @Column(name = "FLOW_STATUS", length = 2)
    private String flowStatus;
}