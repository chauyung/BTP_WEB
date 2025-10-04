package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * NCCC 預算明細檔
 */
@Getter
@Setter
@Entity
@IdClass(NcccAccountingGradeD.ConfigId.class)
@Table(name = "NCCC_ACCOUNTING_GRADE_D")
public class NcccAccountingGradeD implements Serializable {

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
	 * 職等代號
	 */
	@Id
	@Column(name = "GRADE_ID", length = 4)
	private String gradeId;
	
	/**
	 * 職等名稱
	 */
	@Column(name = "GRADE_NAME", length = 20)
	private String gradeName;
	
	/**
	 * 人數
	 */
	@Column(name = "QTY")
	private BigDecimal qty;
	
	/**
	 * 職等
	 */
	@Column(name = "GRADE_SALARY")
	private BigDecimal gradeSalary;
	
	/**
	 * 預算數
	 */
	@Column(name = "QTY_GRADE_SALARY")
	private BigDecimal qtyGradeSalary;
	
	/**
	 * 調整數
	 */
	@Column(name = "ADJUST_GRADE_SALARY")
	private BigDecimal adjustGradeSalary;
	
	/**
	 * 調整後金額
	 */
	@Column(name = "FINAL_GRADE_SALARY")
	private BigDecimal finalGradeSalary;
	
	/**
	 * 備註
	 */
	@Column(name = "REMARK", length = 30)
	private String remark;
	
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
		private String gradeId;
		
		@Override
		public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(year, configId.year) &&
                Objects.equals(version, configId.version) &&
                Objects.equals(oucode, configId.oucode) &&
                Objects.equals(accounting, configId.accounting) &&
                Objects.equals(gradeId, configId.gradeId);
        }
		
		@Override
        public int hashCode() {
            return Objects.hash(year, version, oucode, gradeId, accounting);
        }
		
	}

}
