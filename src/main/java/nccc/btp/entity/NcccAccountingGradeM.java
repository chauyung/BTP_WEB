package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
 * NCCC 預算主檔
 */
@Getter
@Setter
@Entity
@IdClass(NcccAccountingGradeM.ConfigId.class)
@Table(name = "NCCC_ACCOUNTING_GRADE_M")
public class NcccAccountingGradeM implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 預算年度
	 */
	@Id
	@Column(name = "YEAR", length = 4)
	private String year;
	
	/**
	 * 版次
	 */
	@Id
	@Column(name = "VERSION", length = 1)
	private String version;
	
	/**
	 * 組織編號
	 */
	@Id
	@Column(name = "OUCODE", length = 50)
	private String oucode;
	
	/**
	 * 會計科目
	 */
	@Id
	@Column(name = "ACCOUNTING", length = 8)
	private String accounting;
	
	/**
	 * 摘要說明
	 */
	@Column(name = "REMARK", length = 200)
	private String remark;
	
	/**
	 * 確認人員
	 */
	@Column(name = "CONFIRM_USER", length = 50)
	private String confirmUser;
	
	/**
	 * 確認日期
	 */
	@Column(name = "CONFIRM_DATE", nullable = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate confirmDate;
	
	/**
	 * 覆核人員
	 */
	@Column(name = "APPROVE_USER", length = 50)
	private String approveUser;
	
	/**
	 * 覆核日期
	 */
	@Column(name = "APPROVE_DATE", nullable = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate approveDate;
	
	/**
	 * 建檔者
	 */
	@Column(name = "CREATE_USER", length = 50)
	private String createUser;
	
	/**
	 * 建檔日期
	 */
	@Column(name = "CREATE_DATE", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate createDate;
	
	/**
	 * 更新人員
	 */
	@Column(name = "UPDATE_USER", length = 50)
	private String updateUser;
	
	/**
	 * 更新時間
	 */
	@Column(name = "UPDATE_DATE", nullable = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate updateDate;
	
	
	public static class ConfigId implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String year;
		private String version;
		private String oucode;
		private String accounting;
		
		@Override
		public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(year, configId.year) &&
                Objects.equals(version, configId.version) &&
                Objects.equals(oucode, configId.oucode) &&
                Objects.equals(accounting, configId.accounting);
        }
		
		@Override
        public int hashCode() {
            return Objects.hash(year, version, oucode, accounting);
        }
		
	}

}
