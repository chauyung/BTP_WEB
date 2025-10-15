package nccc.btp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * 預算管理模組 : 預算編列-預算分攤展算批次作業(DTO)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@Data
public class BudgetaryApportionmentCheckDto {
	
	/**
	 * 預算年度
	 */
	@NotBlank
    @Pattern(regexp = "^[0-9]{4}$", message = "YYYY")
	private String year;
	
	/**
	 * 版次
	 */
	@NotBlank
	@Pattern(regexp = "^(1|2){1}$")
	private String version;
}
