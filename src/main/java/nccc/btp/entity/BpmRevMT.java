package nccc.btp.entity;

import javax.persistence.*;

/**
 * 驗收單任務
 */
@Entity
@Table(name = "BPM_REV_M_T")
public class BpmRevMT {

    /*
     * 主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_rev_m_d")
    @SequenceGenerator(name = "seq_bpm_rev_m_d", sequenceName = "SEQ_BPM_REV_M_D", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    /**
     * 驗收單單號
     */
    @Column(name = "REV_NO", length = 12)
    private String revNo;

    /** 任務編碼 */
    @Column(name = "TASK_ID", length = 10)
    private String taskId;

    /** 處理識別鍵 */
    @Column(name = "HANDLE_IDENTIFICATION_KEY", length = 36)
    private String handleIdentificationKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRevNo() {
        return revNo;
    }

    public void setRevNo(String revNo) {
        this.revNo = revNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getHandleIdentificationKey() {
        return handleIdentificationKey;
    }

    public void setHandleIdentificationKey(String handleIdentificationKey) {
        this.handleIdentificationKey = handleIdentificationKey;
    }
}
