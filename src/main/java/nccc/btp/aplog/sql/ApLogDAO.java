package nccc.btp.aplog.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import nccc.btp.aplog.ApLogContext;
import nccc.btp.aplog.model.ApLogDetail;
import nccc.btp.aplog.model.ApLogMaster;
import nccc.btp.aplog.model.LogFunctionCount;
import nccc.btp.util.DateUtil;

public class ApLogDAO implements ApLogRepository {

  // 修改 LOG_MASTER 的 SQL 插入語句，新增 TIMESTAMP 欄位
  private static final String sql_insert_master =
      "insert into LOG_MASTER(GUID, SYSTEM_ID, FUNCTION_ID, FUNCTION_NAME, REQUEST_URL, DEPT_ID, TEAM_ID, USER_ID, USER_NAME, ACCESS_DATE, ACCESS_TIME, ACCESS_TYPE, SUCCESS_FLAG, SOURCE_IP, TARGET_IP, QUERY_INPUT, FUNCTION_COUNT, WRITE_DATE, WRITE_TIME, TIMESTAMP) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  // 修改 LOG_DETAIL 的 SQL 插入語句，新增 TIMESTAMP 欄位
  private static final String sql_insert_detail =
      "insert into LOG_Detail(GUID, WRITE_DATE, WRITE_TIME, SQL_STATEMENT_1, SQL_STATEMENT_2, BEFORE_IMAGE, AFTER_IMAGE, SQL_CODE, QUERY_COUNT, TIMESTAMP) values (?,?,?,?,?,?,?,?,?,?)";
//	private static final String sql_insert_functionCnt = "insert into LOG_FUNCTION_COUNT(ACCESS_DATE, ACCESS_TIME, SYSTEM_ID, FUNCTION_ID, FUNCTION_NAME) values (?,?,?,?,?)";
	private static final String sql_insert_functionStat = "Insert into LOG_FUNCTION_STATS (SYS_MONTH, SYSTEM_ID, FUNCTION_ID, FUNCTION_NAME, FUNCTION_USE_COUNT) Values (?, ?, ?, ?, ?)";
	private static final String sql_update_functionStat = "update LOG_FUNCTION_STATS set FUNCTION_USE_COUNT = FUNCTION_USE_COUNT + 1 where SYS_MONTH = ? AND SYSTEM_ID = ? AND FUNCTION_ID = ? ";

	private static ApLogDataSource ds = null;

	@Override
	public void save(ApLogMaster logMaster, Iterable<ApLogDetail> logDetails, LogFunctionCount lfc) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Context _context;
			if (ds == null) {
				try {
					_context = new InitialContext();
                    ds = new ApLogDataSource((DataSource) _context.lookup("jdbc/btp_ds"));
				} catch (NamingException e) {
					System.out.println(e.getMessage());

					try {
					_context = new InitialContext();
                    ds = new ApLogDataSource(
                        (DataSource) _context.lookup("java:comp/env/jdbc/btp_ds"));
					} catch (NamingException x) {
						System.out.println(x.getMessage());
						x.printStackTrace();
					}
					
				}
			}
			
			conn = ds.getApLogConnection();
			conn.setAutoCommit(false);

            // Master Log Insert
            ApLogContext.setCurrentSqlIndex(ApLogContext.getAndIncrementSqlIndex());
			ps = conn.prepareStatement(sql_insert_master);
			int index = 1;
			ps.setString(index++, logMaster.getGuid());
			ps.setString(index++, logMaster.getSystemId());
            ps.setString(index++, logMaster.getFunctionId());
			ps.setString(index++, logMaster.getFunctionName());
			ps.setString(index++, logMaster.getRequestUrl());
			ps.setString(index++, logMaster.getDeptId());
			ps.setString(index++, logMaster.getTeamId());
			ps.setString(index++, logMaster.getUserId());
			ps.setString(index++, logMaster.getUserName());
			ps.setString(index++, logMaster.getAccessDate());
			ps.setString(index++, logMaster.getAccessTime());
			ps.setString(index++, logMaster.getAccessType());
			ps.setString(index++, logMaster.getSuccessFlag());
			ps.setString(index++, logMaster.getSourceIp());
			ps.setString(index++, logMaster.getTargetIp());
			ps.setString(index++, StringUtils.substring(logMaster.getQureyInput(), 0, 900));
			ps.setString(index++, logMaster.getFunctionCount());
			ps.setString(index++, logMaster.getWriteDate());
			ps.setString(index++, logMaster.getWriteTime());
            ps.setString(index++, logMaster.getTimestamp()); // 設定 LogMaster 的 TIMESTAMP 欄位
			ps.executeUpdate();
			ps.close();

            // Detail Log Insert
			ps = conn.prepareStatement(sql_insert_detail);
			int batchNum = 0;
			for (ApLogDetail detail : logDetails) {
              ApLogContext.setCurrentSqlIndex(ApLogContext.getAndIncrementSqlIndex());
				batchNum++;
				index = 1;
				ps.setString(index++, detail.getGuid());
				ps.setString(index++, detail.getWriteDate());
				ps.setString(index++, detail.getWriteTIme());
				ps.setString(index++, detail.getSqlStatement1());
				ps.setString(index++, detail.getSqlStatement2());
				ps.setString(index++, detail.getBeforeImage());
				ps.setString(index++, detail.getAfterImage());
				ps.setString(index++, detail.getSqlCode());
				ps.setString(index++, detail.getQueryCount());
                ps.setString(index++, detail.getTimestamp()); // 設定 ApLogDetail 的 TIMESTAMP 欄位
				ps.addBatch();
				if(batchNum % 2000 == 0) {
					batchNum = 0;
					ps.executeBatch();
				}
			}
			if(batchNum>0) {
				ps.executeBatch();
			}
			ps.close();

            // Function Stats Update/Insert
			if(lfc != null) {
              String sysMonth = DateUtil.getYyyyMm(new Date());
				
				// -- update function cnt, if fale then insert
				index = 1;
                ApLogContext.setCurrentSqlIndex(ApLogContext.getAndIncrementSqlIndex());
				ps = conn.prepareStatement(sql_update_functionStat);
				ps.clearParameters();
				ps.setString(index++, sysMonth);
				ps.setString(index++, lfc.getSystemId());
				ps.setString(index++, lfc.getFunctionId());
				int r = ps.executeUpdate();
				if (r <= 0){
					// insert
                    ApLogContext.setCurrentSqlIndex(ApLogContext.getAndIncrementSqlIndex());
					ps = conn.prepareStatement(sql_insert_functionStat);
					ps.clearParameters();
					index = 1;
					ps.setString(index++, sysMonth);
					ps.setString(index++, lfc.getSystemId());
                    ps.setString(index++, lfc.getFunctionId());
					ps.setString(index++, lfc.getFunctionName());
					ps.setInt(index++, 1);
					ps.execute();
				}
				ps.close();
			}
			
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps !=null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}