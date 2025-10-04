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
 * NCCC 預算項目作業項目明細檔
 */
@Getter
@Setter
@Entity
@IdClass(NcccOucodeAccountingOperateD.ConfigId.class)
@Table(name = "NCCC_OUCODE_ACCOUNTING_OPERATE_D")
public class NcccOucodeAccountingOperateD implements Serializable {

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
	 * 作業項目代號
	 */
	@Id
	@Column(name = "OPERATE_ITEM_CODE", length = 10)
	private String operateItemCode;
	
	/**
	 * 作業項目
	 */
	@Column(name = "OPERATE_ITEM", length = 100)
	private String operateItem;
	
	/**
	 * 人數
	 */
	@Column(name = "OPERATE_QTY")
	private BigDecimal operateQty;
	
	/**
	 * 人數比例
	 */
	@Column(name = "OPERATE_QTY_RATIO")
	private BigDecimal operateQtyRatio;
	
	/**
	 * 金額
	 */
	@Column(name = "OPERATE_AMT")
	private BigDecimal operateAmt;
	
	/**
	 * 金額比例
	 */
	@Column(name = "OPERATE_AMT_RATIO")
	private BigDecimal operateAmtRatio;
	
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
	
	
	public static class ConfigId implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String year;
		private String version;
		private String oucode;
		private String accounting;
		private String operateItemCode;
		
		@Override
		public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConfigId)) return false;
            ConfigId configId = (ConfigId) o;
            return Objects.equals(year, configId.year) &&
                Objects.equals(version, configId.version) &&
                Objects.equals(oucode, configId.oucode) &&
                Objects.equals(accounting, configId.accounting) &&
                Objects.equals(operateItemCode, configId.operateItemCode);
        }
		
		@Override
        public int hashCode() {
            return Objects.hash(year, version, oucode, accounting, operateItemCode);
        }
		
	}

}
