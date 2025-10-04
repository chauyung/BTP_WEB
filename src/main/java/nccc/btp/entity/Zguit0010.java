package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The persistent class for the ZGUIT0010 database table.
 * 
 */
@Entity
@Table(name = "ZGUIT0010")
@NamedQuery(name = "Zguit0010.findAll", query = "SELECT n FROM Zguit0010 n")
public class Zguit0010 implements Serializable {
	private static final long serialVersionUID = 1L;

	// 版次
	@Column(name = "VERS")
	private String vers;

	// 版次序號
	@Id
	@Column(name = "VERS_SEQ")
	private String versSeq;

	// 發票類別代號
	@Column(name = "SUBOBJECT")
	private String subobject;

	// 公司統編
	@Column(name = "VAT")
	private String vat;

	// 發票類別代號
	@Column(name = "GUI_CLASS")
	private String guiClass;

	// 發票類別說明
	@Column(name = "GUI_DESC")
	private String guiCesc;

	// 發票期別
	@Column(name = "GUI_PERIOD")
	private String guiPeriod;

	// 字軌
	@Column(name = "PREFIXCODE")
	private String prefixcode;

	// 起始號碼
	@Column(name = "FROMNUMBER")
	private String fromnumber;

	// 終止號碼
	@Column(name = "TONUMBER")
	private String tonumber;

	// SAP建立者名稱
	@Column(name = "ERNAM")
	private String ernam;

	// SAP建立日期
	@Column(name = "ERDAT")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String erdat;

	// SAP輸入時間
	@Column(name = "ERZET")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String erzet;

	// 配號註記
	@Column(name = "AS_FG")
	private String asFg;

	// 配號日期
	@Column(name = "AS_DAT")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String asDat;

	// 配號時間
	@Column(name = "AS_TIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String asTime;

	// 配號傳輸檔案名稱
	@Column(name = "AS_FNAME")
	private String asFname;

	// 修改人員
	@Column(name = "UPDATE_USER")
	private String updateUser;

	// 修改時間
	@Column(name = "UPDATE_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate updateDate;

	@PrePersist
	@PreUpdate
	protected void onUpdate() {
		updateDate = LocalDate.now();
	}

	public Zguit0010() {
	}

	public String getVers() {
		return vers;
	}

	public void setVers(String vers) {
		this.vers = vers;
	}

	public String getVersSeq() {
		return versSeq;
	}

	public void setVersSeq(String versSeq) {
		this.versSeq = versSeq;
	}

	public String getSubobject() {
		return subobject;
	}

	public void setSubobject(String subobject) {
		this.subobject = subobject;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getGuiClass() {
		return guiClass;
	}

	public void setGuiClass(String guiClass) {
		this.guiClass = guiClass;
	}

	public String getGuiCesc() {
		return guiCesc;
	}

	public void setGuiCesc(String guiCesc) {
		this.guiCesc = guiCesc;
	}

	public String getGuiPeriod() {
		return guiPeriod;
	}

	public void setGuiPeriod(String guiPeriod) {
		this.guiPeriod = guiPeriod;
	}

	public String getPrefixcode() {
		return prefixcode;
	}

	public void setPrefixcode(String prefixcode) {
		this.prefixcode = prefixcode;
	}

	public String getFromnumber() {
		return fromnumber;
	}

	public void setFromnumber(String fromnumber) {
		this.fromnumber = fromnumber;
	}

	public String getTonumber() {
		return tonumber;
	}

	public void setTonumber(String tonumber) {
		this.tonumber = tonumber;
	}

	public String getErnam() {
		return ernam;
	}

	public void setErnam(String ernam) {
		this.ernam = ernam;
	}

	public String getErdat() {
		return erdat;
	}

	public void setErdat(String erdat) {
		this.erdat = erdat;
	}

	public String getErzet() {
		return erzet;
	}

	public void setErzet(String erzet) {
		this.erzet = erzet;
	}

	public String getAsFg() {
		return asFg;
	}

	public void setAsFg(String asFg) {
		this.asFg = asFg;
	}

	public String getAsDat() {
		return asDat;
	}

	public void setAsDat(String asDat) {
		this.asDat = asDat;
	}

	public String getAsTime() {
		return asTime;
	}

	public void setAsTime(String asTime) {
		this.asTime = asTime;
	}

	public String getAsFname() {
		return asFname;
	}

	public void setAsFname(String asFname) {
		this.asFname = asFname;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public LocalDate getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDate updateDate) {
		this.updateDate = updateDate;
	}

}
