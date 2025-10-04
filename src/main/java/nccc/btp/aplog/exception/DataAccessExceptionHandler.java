package nccc.btp.aplog.exception;

import java.sql.Date;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.ThreadContext;
import nccc.btp.aplog.ApLogContext;

public class DataAccessExceptionHandler {
	public void handleException(String sql, DataAccessException e) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		int detailCount = getDetailCount();
		ThreadContext.put(detailCount + ":isApLogError", Boolean.FALSE.toString());
		ThreadContext.put(detailCount + ":errorCode", String.valueOf(e.getErrorCode()));
		ThreadContext.put(detailCount + ":sql", sql);
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat dsdf = new SimpleDateFormat(ApLogContext.date_format);
		ThreadContext.put(detailCount + ":date", dsdf.format(now));
		SimpleDateFormat tsdf = new SimpleDateFormat(ApLogContext.time_format);
		ThreadContext.put(detailCount + ":time", tsdf.format(now));
	}
	
	public void handleApLogException(String sql, DataAccessException e) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		int detailCount = getDetailCount();
		ThreadContext.put(detailCount + ":isApLogError", Boolean.TRUE.toString());
		ThreadContext.put(detailCount + ":errorCode", String.valueOf(e.getErrorCode()));
		ThreadContext.put(detailCount + ":sql", sql);
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat dsdf = new SimpleDateFormat(ApLogContext.date_format);
		ThreadContext.put(detailCount + ":date", dsdf.format(now));
		SimpleDateFormat tsdf = new SimpleDateFormat(ApLogContext.time_format);
		ThreadContext.put(detailCount + ":time", tsdf.format(now));
	}
	
	private int getDetailCount() {
		String detailCountS = ThreadContext.get(ApLogContext.detail_count_key);
		int detailCount = 0;
		if (detailCountS != null) {
			detailCount = Integer.parseInt(detailCountS) + 1;
		}
		ThreadContext.put(ApLogContext.detail_count_key, String.valueOf(detailCount));
		return detailCount;
	}
}
