package nccc.btp.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import nccc.btp.entity.NcccHrGradeD;

/**
 * 各職等金額表 DTO
 */
public class NcccHrGradeDto {
	
	private String year;
	private String version;
	private String accounting;
	private String subject;
	private String createUser;
	private String createDate;
	private String updateUser;
	private String updateDate;
	private List<NcccHrGradeD> ncccHrGradeDetailList;
	
	// Getters and Setters
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getAccounting() {
		return accounting;
	}
	
	public void setAccounting(String accounting) {
		this.accounting = accounting;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getUpdateUser() {
		return updateUser;
	}
	
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public String getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	public List<NcccHrGradeD> getNcccHrGradeDetailList() {
		return ncccHrGradeDetailList;
	}
	
	public void setNcccHrGradeDetails(List<NcccHrGradeD> ncccHrGradeDetailList) {
		this.ncccHrGradeDetailList = ncccHrGradeDetailList;
	}
	
	
	/*
	 * public static class NcccHrGradeDetail {
	 * 
	 * private String gradeId; private String gradeName; private BigDecimal
	 * gradeSalary;
	 * 
	 * @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
	 * private LocalDate createDate; private String updateUser;
	 * 
	 * @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
	 * private LocalDate updateDate;
	 * 
	 * // Getters and Setters public String getGradeId() { return gradeId; }
	 * 
	 * public void setGradeId(String gradeId) { this.gradeId = gradeId; }
	 * 
	 * public String getGradeName() { return gradeName; }
	 * 
	 * public void setGradeName(String gradeName) { this.gradeName = gradeName; }
	 * 
	 * public BigDecimal getGradeSalary() { return gradeSalary; }
	 * 
	 * public void setGradeSalary(BigDecimal gradeSalary) { this.gradeSalary =
	 * gradeSalary; }
	 * 
	 * public LocalDate getCreateDate() { return createDate; }
	 * 
	 * public void setCreateDate(LocalDate createDate) { this.createDate =
	 * createDate; } public String getUpdateUser() { return updateUser; }
	 * 
	 * public void setUpdateUser(String updateUser) { this.updateUser = updateUser;
	 * }
	 * 
	 * public LocalDate getUpdateDate() { return updateDate; }
	 * 
	 * public void setUpdateDate(LocalDate updateDate) { this.updateDate =
	 * updateDate; }
	 * 
	 * }
	 */

}
