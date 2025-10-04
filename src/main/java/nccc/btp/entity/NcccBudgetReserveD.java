package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * NCCC 預算保留明細檔 (NCCC_BUDGET_RESERVE_D)
 */
@Setter
@Getter
@Entity
@IdClass(NcccBudgetReserveD.ConfigId.class)
@Table(name = "NCCC_BUDGET_RESERVE_D")
public class NcccBudgetReserveD implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 單號 */
	@Id
	@Column(name = "RSV_NO", length = 20, nullable = false)
	private String rsvNo;

	/** 序號(001,002,003) */
	@Id
	@Column(name = "SEQ_NO", length = 3, nullable = false)
	private String seqNo;

	/** 請購單號 */
	@Column(name = "PR_NO", length = 12)
	private String prNo;

	/** 採購單號 */
	@Column(name = "PO_NO", length = 12)
	private String poNo;

	/** 預算項目代號 */
	@Column(name = "ACCOUNTING", length = 8)
	private String accounting;

	/** 請購金額 */
	@Column(name = "PR_AMT", precision = 11, scale = 0)
	private BigDecimal prAmt;

	/** 採購金額 */
	@Column(name = "PO_AMT", precision = 11, scale = 0)
	private BigDecimal poAmt;

	/** 保留預算金額 */
	@Column(name = "RESERVER_AMT", precision = 11, scale = 0)
	private BigDecimal reserverAmt;

	/** 採購目的 */
	@Column(name = "PO_REMARK", length = 100)
	private String poRemark;

	/** 保留預算原因 */
	@Column(name = "RES_REASON", length = 100)
	private String resReason;

	/** 請購公文文號 */
	@Column(name = "PR_DOC_NO", length = 100)
	private String prDocNo;

	/** 採購公文文號 */
	@Column(name = "PO_DOC_NO", length = 100)
	private String poDocNo;

	/** 建檔者 */
	@Column(name = "CREATE_USER", length = 50)
	private String createUser;

	/** 建檔日期 */
	@Column(name = "CREATE_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDate;

	/** 更新者 */
	@Column(name = "UPDATE_USER", length = 50)
	private String updateUser;

	/** 更新日期 */
	@Column(name = "UPDATE_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateDate;

	/** 複合主鍵 */
	public static class ConfigId implements Serializable {
		private static final long serialVersionUID = 1L;

		private String rsvNo;
		private String seqNo;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof ConfigId)) return false;
			ConfigId that = (ConfigId) o;
			return Objects.equals(rsvNo, that.rsvNo) &&
				   Objects.equals(seqNo, that.seqNo);
		}

		@Override
		public int hashCode() {
			return Objects.hash(rsvNo, seqNo);
		}
	}
}
