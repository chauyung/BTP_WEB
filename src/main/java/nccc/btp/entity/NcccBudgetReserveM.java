package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/** NCCC 預算保留主檔 */
@Setter
@Getter
@Entity
@Table(name = "NCCC_BUDGET_RESERVE_M")
public class NcccBudgetReserveM implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 單號 */
    @Id
    @Column(name = "RSV_NO", length = 20, nullable = false)
    private String rsvNo;

    /** 任務編號 */
    @Column(name = "TASKID", length = 40)
    private String taskId;

    /** 日期 */
    @Column(name = "APPLY_DATE")
    private LocalDate applyDate;

    /** 員工代號 */
    @Column(name = "APPLY_USER", length = 50)
    private String applyUser;

    /** 部門 */
    @Column(name = "APPLY_OUCODE", length = 20)
    private String applyOuCode;

    /** 預算年度（NVARCHAR2(4)） */
    @Column(name = "YEAR", length = 4)
    private String year;

    /** 版次 */
    @Column(name = "VERSION", length = 1)
    private String version;

    /** 備註 */
    @Column(name = "REMARK", length = 500)
    private String remark;

    /** 建檔者 */
    @Column(name = "CREATE_USER", length = 50)
    private String createUser;

    /** 建檔日期 */
    @Column(name = "CREATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;

    /** 更新者 */
    @Column(name = "UPDATE_USER", length = 50)
    private String updateUser;

    /** 更新日期 */
    @Column(name = "UPDATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDate;

    /** 流程狀態 */
    @Column(name = "FLOW_STATUS", length = 2)
    private String flowStatus;
    
}
