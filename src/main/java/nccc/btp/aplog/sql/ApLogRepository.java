package nccc.btp.aplog.sql;

import nccc.btp.aplog.model.ApLogDetail;
import nccc.btp.aplog.model.ApLogMaster;
import nccc.btp.aplog.model.LogFunctionCount;

public interface ApLogRepository {
	public void save(ApLogMaster logMaster, Iterable<ApLogDetail> logDetails, LogFunctionCount lfc);
}
