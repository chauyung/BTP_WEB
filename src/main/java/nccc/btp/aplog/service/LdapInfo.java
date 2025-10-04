package nccc.btp.aplog.service;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nccc.btp.aplog.mask.IApLogMask;

/**
 *  AP Log Ldap info
 */
@Data
@NoArgsConstructor
@ToString
public class LdapInfo {
	private String funId;
	private String funName;
	private String[] accType;
	private String[] url;
	private String[] queryIds;
	private String[] needCount;
	private Map<String, Map<String, String>> queryParas = new HashMap<>();
	private Map<String, Map<String, String>> actionParas = new HashMap<>();
	private Map<String, Map<String, IApLogMask>> queryMasks = new HashMap<>();
	private Map<String, Map<String, Map<String, String>>> queryParaMapping = new HashMap<>();
	
	Map<String, String> ldapInfoQueryParas = null;
	Map<String, Map<String, String>> ldapInfoMappingMap = null;
	Map<String, IApLogMask> maskMap = null;
	
	private String fuctionCount;
	private String accessType;

}
