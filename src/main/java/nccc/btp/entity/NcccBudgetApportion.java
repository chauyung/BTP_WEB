package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 預算編列-預算分攤展算表(Entity)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
@Setter
@Getter
@Entity
@Table(name = "NCCC_BUDGET_APPORTION")
public class NcccBudgetApportion implements Serializable {
	private static final long serialVersionUID = -441047628380013970L;

    /**
     * 預算年度
     */
    @Id
    @Column(name = "YYYY",length = 4)
    private String yyyy;

    /**
     * 版次
     */
    @Id
    @Column(name = "VERSION",length = 1)
    private String version;

    /** 
     * 分攤前:BEFORE/分攤後:AFTER 
     */
    @Column(name = "APPROATION", length = 10)
    private String APPROATION;
    
    /**
     * 預算部門代號
     */
    @Id
    @Column(name = "OUCODE", length = 20)
    private String ouCode;
 
    /** 
     * 預算部門名稱
     */
    @Column(name = "OUNAME", length = 30)
    private String ouName;
    
    /**
     * 預算科目代號
     */
    @Id
    @Column(name = "ACCOUNTING", length = 8)
    private String accounting;

    /**
     * 預算科目名稱
     */
	@Column(name = "SUBJECT", length = 30, nullable = false)
	private String subject;
	
    /** 
     * 作業項目代號
     */
    @Column(name = "OPERATE_ITEM_CODE", length = 10)
    private String operateItemCode;
    
    /** 
     * 作業項目
     */
    @Column(name = "OPERATE_ITEM", length = 100)
    private String operateItem;
    
    /** 
     * 預算金額 
     */
    @Column(name = "AMOUNT")
    private BigDecimal operateAmt;
    
    /**
     * 調撥說明
     */
    @Column(name = "REMARK", length = 100)
    private String remark;
    
    /**
     * 建檔者
     */
    @Column(name = "CREATE_USER", length = 10)
    private String createUser;

    /**
     * 建檔日期
     */
    @Column(name = "CREATE_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;

    /**
     * 更新者
     */
    @Column(name = "UPDATE_USER", length = 10)
    private String updateUser;

    /**
     * 更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDate;
    
    
    /**
     * 資料表(PK)
     */
    public static class ConfigId implements Serializable{
        private static final long serialVersionUID = 1L;

        private String yyyy;
        private String version;
        private String ouCode;
        private String accounting;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
				return true;
			}
            if (!(o instanceof ConfigId)) {
				return false;
			}
            ConfigId configId = (ConfigId) o;
            return Objects.equals(yyyy, configId.yyyy) &&
                Objects.equals(version, configId.version) &&
                Objects.equals(ouCode, configId.ouCode) &&
                Objects.equals(accounting, configId.accounting);
        }

        @Override
        public int hashCode() {
            return Objects.hash(yyyy, version, ouCode, accounting);
        }
    }
}
