package nccc.btp.aplog.filter;

import java.util.Arrays;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.util.AntPathMatcher;

public class ApLogUtils {

	
	private static final String[] EXCLUDE_PATHS = {
			"/js/*", 
			"/css/*",
			"/css/themes/*",
			"/css/themes/default/*",
			"/css/themes/default/images/*",
			"/img/*",
			"/images/*",
			"/fonts/*",
			"/kaptcha/*",
			"*.html",
			"/loginStatus"
			
			};
	
	/**
	 * 是否是設定Exclude Uri
	 * @param uri
	 * @return
	 */
	public static boolean isExcludeUri(String uri) {
		if (Arrays.stream(EXCLUDE_PATHS).anyMatch(
				e -> new AntPathMatcher().match(e, uri))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 將functioncount 記錄到 ThreadContext
	 * @param count
	 */
	public static void setApLogFunctionCount(int count){
		ThreadContext.put("apLogfunctionCount", "" + count);
	}
}


