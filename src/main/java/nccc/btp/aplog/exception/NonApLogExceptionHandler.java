package nccc.btp.aplog.exception;

import java.sql.Date;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.ThreadContext;
import nccc.btp.aplog.ApLogContext;

public class NonApLogExceptionHandler {
	public void handleException(Exception e) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		int detailCount = getDetailCount();
		ThreadContext.put(detailCount + ":isApLogError", Boolean.FALSE.toString());
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
