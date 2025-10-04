package nccc.btp.aplog;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.ThreadContext;
import nccc.btp.aplog.mask.IApLogMask;

public class ApLogContext {
	private static Map<String, Map<String, IApLogMask>> tableMasks = new HashMap<>();

	public static Map<String, Map<String, IApLogMask>> getTableMasks() {
		return tableMasks;
	}
	
	private static Map<String, String[]> tableKeys = new HashMap<>();
	
	public static Map<String, String[]> getTableKeys() {
		return tableKeys;
	}
	
	private static Map<String, Map<String, Integer>> charMap = new HashMap<>();
	
	public static Map<String, Map<String, Integer>> getCharMap() {
		return charMap;
	}
		
	public static final String detail_count_key = "detailCount";
	public static final String date_format = "yyyyMMdd";
	public static final String time_format = "HHmmss";
	
	private static String jndiName = "";

	public static String getJndiName() {
		return jndiName;
	}

	public static void setJndiName(String jndiName) {
		ApLogContext.jndiName = jndiName;
	}
	
	public static void setFunctionCnt(int count) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		ThreadContext.put("functionCnt", String.valueOf(count));
	}
	
	public static void setSuccessFlag(String successFlag) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		ThreadContext.put("successFlag", successFlag);
	}

    // 新增一個 ThreadLocal 變數，用來記錄 SQL 錯誤代碼
    private static final ThreadLocal<String> SQL_CODE = new ThreadLocal<>();

    public static void setCurrentSqlCode(String code) {
      SQL_CODE.set(code);
    }

    public static String getCurrentSqlCode() {
      return SQL_CODE.get();
    }

    public static void clearCurrentSqlCode() {
      SQL_CODE.remove();
    }

    private static final ThreadLocal<Integer> SQL_INDEX = ThreadLocal.withInitial(() -> 0);


    public static void setCurrentSqlIndex(int index) {
      SQL_INDEX.set(index);
    }

    public static int getCurrentSqlIndex() {
      return SQL_INDEX.get();
    }

    public static int getAndIncrementSqlIndex() {
      int current = SQL_INDEX.get();
      SQL_INDEX.set(current + 1);
      return current;
    }

    public static void clearCurrentSqlIndex() {
      SQL_INDEX.remove();
    }
}
