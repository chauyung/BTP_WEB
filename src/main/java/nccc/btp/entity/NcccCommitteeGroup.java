package nccc.btp.entity;

import javax.persistence.*;

@Entity
@Table(name = "NCCC_COMMITTEE_GROUP")
public class NcccCommitteeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nccc_committee_group")
    @SequenceGenerator(name = "seq_nccc_committee_group", sequenceName = "SEQ_NCCC_COMMITTEE_GROUP", allocationSize = 1)
    @Column(name = "SERIAL_NO", nullable = false, precision = 10, scale = 0)
    private Long serialNo;

    // 組別名稱
    @Column(name = "GROUP_NAME", length = 20)
    private String groupName;

    // 研究項目
    @Column(name = "RESEARCH_PROJECT", length = 255)
    private String researchProject;

    // 小組召集人
    @Column(name = "CONVENER", length = 10)
    private String convener;

    public Long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Long serialNo) {
        this.serialNo = serialNo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getResearchProject() {
        return researchProject;
    }

    public void setResearchProject(String researchProject) {
        this.researchProject = researchProject;
    }

    public String getConvener() {
        return convener;
    }

    public void setConvener(String convener) {
        this.convener = convener;
    }
}
