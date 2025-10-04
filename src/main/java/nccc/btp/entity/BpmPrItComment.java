package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "BPM_PR_IT_COMMENT")
@NamedQuery(name = "BpmPrItComment.findAll", query = "SELECT b FROM BpmPrItComment b")
public class BpmPrItComment implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 請購單編號 PK */
	@Id
	@Column(name = "pr_no", length = 12)
	private String prNo;

	/** 建議金額 */
	@Column(name = "RECOMMENDED_AMOUNT", precision = 13, scale = 2)
	private BigDecimal recommendedAmount;

	/** 說明 */
	@Column(name = "remark", length = 100)
	private String remark;

	/** 附件 */
	@Lob
	@Column(name = "attachment")
	private byte[] attachment;

	public BpmPrItComment() {
	}

	public String getPrNo() {
		return prNo;
	}

	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}

	public BigDecimal getRecommendedAmount() {
		return recommendedAmount;
	}

	public void setRecommendedAmount(BigDecimal recommendedAmount) {
		this.recommendedAmount = recommendedAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

}
