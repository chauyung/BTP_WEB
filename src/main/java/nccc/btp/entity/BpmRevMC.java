package nccc.btp.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

/*
 * 驗收單憑證
 */
@Entity
@Table(name = "BPM_REV_M_C")
public class BpmRevMC {

    /*
     * 主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_rev_m_c")
    @SequenceGenerator(name = "seq_bpm_rev_m_c", sequenceName = "SEQ_BPM_REV_M_C", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    /*
     * 驗收單單號
     */
    @Column(name = "REV_NO", length = 12)
    private String revNo;

    /* 處理識別鍵 */
    @Column(name = "HANDLE_IDENTIFICATION_KEY", length = 36)
    private String handleIdentificationKey;

    /*
     * 檔名
     */
    @Column(name = "NAME", length = 50)
    private String name;

    /*
     * 附件
     */
    @Lob
    @Column(name = "APPENDIX")
    private byte[] appendix;

    /*
     * 上傳者
     */
    @Column(name = "UPLOADER", length = 10)
    private String uploader;

    /*
     * 戳記時間
     */
    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

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

    public byte[] getAppendix() {
        return appendix;
    }

    public void setAppendix(byte[] appendix) {
        this.appendix = appendix;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandleIdentificationKey() {
        return handleIdentificationKey;
    }

    public void setHandleIdentificationKey(String handleIdentificationKey) {
        this.handleIdentificationKey = handleIdentificationKey;
    }
}
