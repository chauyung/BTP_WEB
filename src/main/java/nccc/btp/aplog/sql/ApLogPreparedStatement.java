package nccc.btp.aplog.sql;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.ThreadContext;
import lombok.extern.log4j.Log4j2;
import nccc.btp.aplog.ApLogContext;
import nccc.btp.aplog.exception.DataAccessException;
import nccc.btp.aplog.exception.DataAccessExceptionHandler;
import nccc.btp.aplog.mask.IApLogMask;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;

@Log4j2
public class ApLogPreparedStatement implements PreparedStatement {

	private PreparedStatement ps;
	private String sql;
	private List<CallMethod> callMethods = new LinkedList<>();
	private List<ParseSqlResult> parseSqlResults = new LinkedList<>();
	private final String STR_EXECUTE = "EXECUTE";

	private class CallMethod {
		private String name;
		private List<Class<?>> type;
		private List<Object> value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Class<?>> getType() {
			return type;
		}

		public void setType(List<Class<?>> type) {
			this.type = type;
		}

		public List<Object> getValue() {
			return value;
		}

		public void setValue(List<Object> value) {
			this.value = value;
		}
	}

	public ApLogPreparedStatement(PreparedStatement ps) {
		this.ps = ps;
		
	}

	public ApLogPreparedStatement(PreparedStatement ps, String sql) {
		this.ps = ps;
		this.sql = sql.toUpperCase();
		log.debug("ps, sql:" + sql);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		this.sql = sql.toUpperCase();
		ResultSet rs;
		try {
			rs = ps.executeQuery(sql);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}
		excuteSelect(rs);
		return rs;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		this.sql = sql.toUpperCase();
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		int result;
		try {
			result = ps.executeUpdate(sql);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}

		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, result, detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, result, startDetailCount);
		}
		return result;
	}

	@Override
	public void close() throws SQLException {
		ps.close();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return ps.getMaxFieldSize();
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		ps.setMaxFieldSize(max);
	}

	@Override
	public int getMaxRows() throws SQLException {
		return ps.getMaxRows();
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		ps.setMaxRows(max);
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		ps.setEscapeProcessing(enable);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return ps.getQueryTimeout();
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		ps.setQueryTimeout(seconds);
	}

	@Override
	public void cancel() throws SQLException {
		ps.cancel();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return ps.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		ps.clearWarnings();
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		ps.setCursorName(name);
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		this.sql = sql.toUpperCase();
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		boolean result;
		try {
			result = ps.execute(sql);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}

		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, 0, detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, 0, startDetailCount);
		}
		return result;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return ps.getResultSet();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return ps.getUpdateCount();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return ps.getMoreResults();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		ps.setFetchDirection(direction);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return ps.getFetchDirection();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		ps.setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return ps.getFetchSize();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return ps.getResultSetConcurrency();
	}

	@Override
	public int getResultSetType() throws SQLException {
		return ps.getResultSetType();
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		List<CallMethod> mLists = new LinkedList<>();
		for(CallMethod m: callMethods) {
			mLists.add(m);
		}
		ParseSqlResult psr = parseSql(sql.toUpperCase(), mLists);
		psr.setInsertParaMethod(mLists);
		parseSqlResults.add(psr);
		callMethods.clear();
		ps.addBatch(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		ps.clearBatch();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if(parseSqlResults!=null && parseSqlResults.size() > 0) {
			for(ParseSqlResult psr : parseSqlResults) {
				if (psr != null && psr.isGetBefore()) {
					processBeforeImg(psr,detailCount);
				}
			}
		}
		
		int[] result;
		try {
			result = ps.executeBatch();
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}
		if(parseSqlResults!=null && parseSqlResults.size() > 0) {
			for(ParseSqlResult psr : parseSqlResults) {
				if (psr != null && psr.isInsert()) {
					processInsertAfterImg(psr, 0,detailCount);
				}
				if (psr != null && psr.isUpdate()) {
					processUpdateAfterImg(psr, 0,startDetailCount);
				}
			}
		}
		parseSqlResults.clear();
		return result;

	}

	@Override
	public Connection getConnection() throws SQLException {
		return ps.getConnection();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		return ps.getMoreResults(current);
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return ps.getGeneratedKeys();
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		this.sql = sql.toUpperCase();
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		
		int result;
		try {
			result = ps.executeUpdate(sql, autoGeneratedKeys);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}
		
		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, result,detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, result,startDetailCount);
		}
		return result;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		this.sql = sql.toUpperCase();
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		int result;
		try {
			result = ps.executeUpdate(sql, columnIndexes);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}
		
		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, result,detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, result,startDetailCount);
		}
		return result;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		this.sql = sql.toUpperCase();
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		int result;
		try {
			result = ps.executeUpdate(sql, columnNames);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}
		
		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, result,detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, result,startDetailCount);
		}
		return result;
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		this.sql = sql.toUpperCase();
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		boolean result;
		try {
			result = ps.execute(sql, autoGeneratedKeys);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}

		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, 0, detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, 0, startDetailCount);
		}
		return result;

	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		this.sql = sql.toUpperCase();
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		boolean result;
		try {
			result = ps.execute(sql, columnIndexes);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}

		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, 0, detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, 0, startDetailCount);
		}
		return result;

	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		this.sql = sql.toUpperCase();
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		boolean result;
		try {
			result = ps.execute(sql, columnNames);
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}

		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, 0, detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, 0, startDetailCount);
		}
		return result;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return ps.getResultSetHoldability();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return ps.isClosed();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		ps.setPoolable(poolable);
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return ps.isPoolable();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		ps.closeOnCompletion();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return ps.isCloseOnCompletion();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ps.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return ps.isWrapperFor(iface);
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}
		excuteSelect(rs);
		return rs;
	}

	private void excuteSelect(ResultSet rs) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		int rows = 0;
		int detailCount = getDetailCount();
		try {
			rs.last();
			rows = rs.getRow();
			rs.beforeFirst();
			ThreadContext.put(detailCount + ":selectCount", String.valueOf(rows));
			ThreadContext.put(detailCount + ":sql", sql);
			Date now = new Date(System.currentTimeMillis());
			SimpleDateFormat dsdf = new SimpleDateFormat(ApLogContext.date_format);
			ThreadContext.put(detailCount + ":date", dsdf.format(now));
			SimpleDateFormat tsdf = new SimpleDateFormat(ApLogContext.time_format);
			ThreadContext.put(detailCount + ":time", tsdf.format(now));
		} catch (SQLException e) {
			new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
		}
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
	
	private class ParseSqlResult{
		private StringBuffer selectSql = new StringBuffer();
		private boolean getBefore = false;
		private boolean isInsert = false;
		private boolean isUpdate = false;
		private List<CallMethod> selectParaMethod = null;
		private List<CallMethod> insertParaMethod = null;
		private boolean getAfter = false;
		private String tableName;
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public boolean isGetAfter() {
			return getAfter;
		}
		public void setGetAfter(boolean getAfter) {
			this.getAfter = getAfter;
		}
		public StringBuffer getSelectSql() {
			return selectSql;
		}
		public boolean isGetBefore() {
			return getBefore;
		}
		public void setGetBefore(boolean getBefore) {
			this.getBefore = getBefore;
		}
		public boolean isInsert() {
			return isInsert;
		}
		public void setInsert(boolean isInsert) {
			this.isInsert = isInsert;
		}
		public boolean isUpdate() {
			return isUpdate;
		}
		public void setUpdate(boolean isUpdate) {
			this.isUpdate = isUpdate;
		}
		public List<CallMethod> getSelectParaMethod() {
			return selectParaMethod;
		}
		public void setSelectParaMethod(List<CallMethod> selectParaMethod) {
			this.selectParaMethod = selectParaMethod;
		} 
		public List<CallMethod> getInsertParaMethod() {
			return insertParaMethod;
		}
		public void setInsertParaMethod(List<CallMethod> insertParaMethod) {
			this.insertParaMethod = insertParaMethod;
		}
	}
	
	private ParseSqlResult parseSql(String sql, List<CallMethod> callMethods) {
		ParseSqlResult result = new ParseSqlResult();
		log.debug("sql:" + sql);
		String parseSql = sql;
		final String replaceStr = "AAAAA";
		try {
			if(parseSql.toUpperCase().indexOf(STR_EXECUTE) != -1) {
				parseSql = parseSql.replace(STR_EXECUTE, replaceStr);
			}
			Statement s = CCJSqlParserUtil.parse(parseSql);
			String whereS = null;
			String table = null;
			List<Column> columns = null;

			if (s instanceof Insert) {
				Insert insert = (Insert) s;
				table = insert.getTable().getName();
				if(ApLogContext.getTableKeys().containsKey(table)) {
					StringBuffer sb = new StringBuffer();
					for(String column : ApLogContext.getTableKeys().get(table)) {
						sb.append(column).append(" = ? and ");
					}
					sb.delete(sb.length() - 4, sb.length());
					whereS = sb.toString();
					result.setGetAfter(true);
				} else {
					whereS = "";
				}
				columns = insert.getColumns();
				for(Column column:columns) {
					if(column.getColumnName().equalsIgnoreCase(replaceStr)) {
						column.setColumnName(STR_EXECUTE);
					}
				}
				
				Map<Integer, String> columnNameMap = new HashMap<>();
				if(result.isGetAfter() && insert.isUseValues()) {
					List<Integer> keyIndex = new LinkedList<>();
					int index = 1;
					for(Column column:columns) {
						for(String key:ApLogContext.getTableKeys().get(table)) {
							if(key.equals(column.getColumnName())) {
								keyIndex.add(index);
								columnNameMap.put(index, column.getColumnName());
							}
						}
						index++;
					}
					ExpressionList el = (ExpressionList)insert.getItemsList();
					Map<Integer, CallMethod> orderMethod = new HashMap<>();
					for (CallMethod m : callMethods) {
						orderMethod.put(((Integer) m.getValue().get(0)), m);
					}
					int paraIndex = 1;
					index = 1;
					result.setSelectParaMethod(new LinkedList<>());
					Map<String, Integer> padMap = ApLogContext.getCharMap().get(table);
					
					for(Expression ex : el.getExpressions()) {
						if(keyIndex.contains(Integer.valueOf(index)) && ex instanceof JdbcParameter) {
							CallMethod tempCm = orderMethod.get(Integer.valueOf(index));
							tempCm.getValue().set(0, paraIndex);
							if(padMap != null && padMap.size() > 0) {
								String colName = columnNameMap.get(index);
								if(padMap.containsKey(colName)) {
									tempCm.getValue().set(1, padRight(tempCm.getValue().get(1).toString(), padMap.get(colName)));
								}
							}
							
							result.getSelectParaMethod().add(tempCm);
							paraIndex++;
						}
						index++;
					}
					
				}
				table = insert.getTable().getName();
				result.setInsert(true);
				columns = insert.getColumns();
			} else if (s instanceof Update) {
              Update update = (Update) s;

              // 1) WHERE 子句處理與 ? 計數
              int i = -1;
              int pCount = 0;
              if (update.getWhere() != null) {
                String whereText = update.getWhere().toString();
                if (whereText.indexOf(replaceStr) != -1) {
                  whereS = whereText.replace(replaceStr, STR_EXECUTE);
                } else {
                  whereS = whereText;
                }
                String tempWhereS = whereS;
                while ((i = tempWhereS.indexOf("?")) >= 0) {
                  tempWhereS = tempWhereS.substring(i + 1);
                  pCount++;
                }
              }

              // 2) 資料表與 SET 欄位（新版寫法）
              table = update.getTable().getName();
              List<UpdateSet> updateSets = update.getUpdateSets();

              int setParamCount = 0;
              if (updateSets != null) {
                for (UpdateSet updateSet : updateSets) {
                  for (Column col : updateSet.getColumns()) {
                    if (col.getColumnName().equalsIgnoreCase(replaceStr)) {
                      col.setColumnName(STR_EXECUTE);
                    }
                  }
                  for (Expression expr : updateSet.getExpressions()) {
                    setParamCount += countJdbcParams(expr); // 計算這個欄位對應值中的 ?
                  }
                }
              }

              // 3) 建立 JDBC 參數索引 → CallMethod
              Map<Integer, CallMethod> orderMethod = new HashMap<>();
              for (CallMethod m : callMethods) {
                orderMethod.put(((Integer) m.getValue().get(0)), m);
              }

              // 4) WHERE 參數範圍（接在 SET 之後）
              int whereStart = setParamCount + 1;
              int whereEnd = setParamCount + pCount;

              result.setSelectParaMethod(new LinkedList<>());
              for (int idx = whereStart; idx <= whereEnd; idx++) {
                CallMethod tempCm = orderMethod.get(idx);
                if (tempCm != null) {
                  tempCm.getValue().set(0, (idx - setParamCount)); // 重編索引
                  result.getSelectParaMethod().add(tempCm);
                } else {
                  log.debug(
                      "SELECT param missing at jdbc index {} (setParamCount={}, pCount={}, totalParams={})",
                      idx, setParamCount, pCount, orderMethod.size());
                }
              }

              result.setGetBefore(true);
              result.setUpdate(true);
			} else if (s instanceof Delete) {
				log.debug("callMethods size:" + callMethods.size());
				Delete delete = (Delete) s;
				if(delete.getWhere() != null) {
					if(delete.getWhere().toString().indexOf(replaceStr) != -1) {
						whereS = delete.getWhere().toString().replace(replaceStr, STR_EXECUTE);
					}else {
						whereS = delete.getWhere().toString();
					}
				}
				table = delete.getTable().getName();
				result.setGetBefore(true);
				result.setSelectParaMethod(callMethods);
			}
			result.setTableName(table);
			result.getSelectSql().append("select ");
			if (columns != null) {
				for (Column c : columns) {
					if(c.getColumnName().equalsIgnoreCase(replaceStr)) {
						c.setColumnName(STR_EXECUTE);
					}
					result.getSelectSql().append(c.getColumnName()).append(", ");
				}
				result.getSelectSql().delete(result.getSelectSql().length() - 2, result.getSelectSql().length());
			} else {
				result.getSelectSql().append("*");
			}
			
			result.getSelectSql().append(" from ").append(table);
			
			if(whereS != null && !"".equals(whereS)) {
				result.getSelectSql().append(" where ").append(whereS);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public int executeUpdate() throws SQLException {
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		int result = 0;
		try {
			result = ps.executeUpdate();
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}
		
		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, result, detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, result, startDetailCount);
		}
		return result;
	}

	private void processUpdateAfterImg(ParseSqlResult psr, int result, int detailCount) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		PreparedStatement afterPs = null;

		try {
			String selectSql = psr.getSelectSql().toString() + " ORDER BY ROWID";
			afterPs = ps.getConnection().prepareStatement(selectSql);
			
			if(psr.getSelectParaMethod() != null && psr.getSelectParaMethod().size() > 0) {
				for (CallMethod cm : psr.getSelectParaMethod()) {
					try {
						StringBuffer sb = new StringBuffer();
						for (Object o : cm.getValue()) {
							if (o != null)
								sb.append(o.toString()).append(", ");
						}
						Method m = PreparedStatement.class.getMethod(cm.getName(),
								cm.getType().toArray(new Class[cm.getType().size()]));
						m.invoke(afterPs, cm.getValue().toArray());
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			
			ResultSet afterRs = afterPs.executeQuery();
			String tableName = psr.getTableName();
			while (afterRs.next()) {
				StringBuffer sb = new StringBuffer();
				for (int i = 1; i <= afterRs.getMetaData().getColumnCount(); i++) {
					
					String data = null;
					
					if (afterRs.getObject(i) != null) {
						if(afterRs.getObject(i) instanceof StringReader) {
							StringReader v = (StringReader) afterRs.getObject(i);
							int intValueOfChar;
							String readV = "";
							while( (intValueOfChar = v.read()) != -1 ) {
								readV += (char) intValueOfChar;
							}
							v.close();
							data = readV;
						} else {
							data = afterRs.getObject(i).toString();
						}
					}
					
					if (data == null) {
						sb.append("NULL").append("\t");
					} else {
						if (ApLogContext.getTableMasks().containsKey(tableName)) {
							Map<String, IApLogMask> maskTypes = ApLogContext.getTableMasks().get(tableName);
							String columnName = afterRs.getMetaData().getColumnName(i);
							if (maskTypes.containsKey(columnName)) {
								IApLogMask mask = maskTypes.get(columnName);
								data = mask.mask(data);
							}
						}
						sb.append(data).append("\t");
					}
					
				}

				if(!ThreadContext.containsKey(detailCount + ":afterImage")) {
					ThreadContext.put(detailCount + ":afterImage", sb.toString());
				} else {
					detailCount++;
					if(!ThreadContext.containsKey(detailCount + ":beforeImage")) {
						detailCount = getDetailCount();
					}
					ThreadContext.put(detailCount + ":afterImage", sb.toString());
				}
				
				log.info(String.format("processUpdateAfterImg : Current detailcount[%d]", detailCount));
				
				if(!ThreadContext.containsKey(detailCount + ":selectCount")) {
					ThreadContext.put(detailCount + ":selectCount", String.valueOf(result));
				}
				
				if(!ThreadContext.containsKey(detailCount + ":sql")) {
					log.debug(String.format("%d:sql[%s]", detailCount, sql));
					ThreadContext.put(detailCount + ":sql", sql);
				}
				if(!ThreadContext.containsKey(detailCount + ":date")) {
					Date now = new Date(System.currentTimeMillis());
					SimpleDateFormat dsdf = new SimpleDateFormat(ApLogContext.date_format);
					ThreadContext.put(detailCount + ":date", dsdf.format(now));
					SimpleDateFormat tsdf = new SimpleDateFormat(ApLogContext.time_format);
					ThreadContext.put(detailCount + ":time", tsdf.format(now));
				}
			}			
		} catch (SQLException e) {
			new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
			return;
		} catch (IOException e) {
			new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
			return;
		} finally {
			if(afterPs != null) {
				try {
					afterPs.close();
				} catch (SQLException e) {
					new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
					return;
				}
			}
		}
	}

	private String padRight(String s, int n) {
		if (s == null) {
			s = "";
		}
		n = n - (s.getBytes().length - s.length());
		return String.format("%1$-" + n + "s", s);
	}
	
	private void processInsertAfterImg(ParseSqlResult psr, int result, int detailCount) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		PreparedStatement afterPs = null;
		try {
			if (psr.isGetAfter()) {
				String selectSql = psr.getSelectSql().toString() + " ORDER BY ROWID";
				
				afterPs = ps.getConnection().prepareStatement(selectSql);
				
				if(psr.getSelectParaMethod() != null && psr.getSelectParaMethod().size() > 0) {
					for (CallMethod cm : psr.getSelectParaMethod()) {
						try {
							Method m = PreparedStatement.class.getMethod(cm.getName(),
									cm.getType().toArray(new Class[cm.getType().size()]));
							m.invoke(afterPs, cm.getValue().toArray());
						} catch ( SecurityException | IllegalArgumentException
								| NoSuchMethodException | IllegalAccessException | InvocationTargetException  e) {
							e.printStackTrace();
						}
					}
				}
				ResultSet afterRs = afterPs.executeQuery();
				String tableName = psr.getTableName();
				
				while (afterRs.next()) {
					for (int i = 1; i <= afterRs.getMetaData().getColumnCount(); i++) {
						String data = null;
						if (afterRs.getObject(i) != null) {
							if(afterRs.getObject(i) instanceof StringReader) {
								StringReader v = (StringReader) afterRs.getObject(i);
								int intValueOfChar;
								String readV = "";
								while( (intValueOfChar = v.read()) != -1 ) {
									readV += (char) intValueOfChar;
								}
								v.close();
								data = readV;
							} else {
								data = afterRs.getObject(i).toString();
							}
						}
						
						if (data == null) {
							sb.append("NULL").append("\t");
						} else {
							if (ApLogContext.getTableMasks().containsKey(tableName)) {
								Map<String, IApLogMask> maskTypes = ApLogContext.getTableMasks().get(tableName);
								String columnName = afterRs.getMetaData().getColumnName(i);
								if (maskTypes.containsKey(columnName)) {
									IApLogMask mask = maskTypes.get(columnName);
									data = mask.mask(data);
								}
							}
							sb.append(data).append("\t");
						}
					}
				}
			} else {
				Map<Integer, Object> values = new HashMap<>();
				List<CallMethod> insertMList = callMethods;
				if(callMethods == null || callMethods.size() == 0) {
					insertMList = psr.getInsertParaMethod();
				}
				for (CallMethod cm : insertMList) {
					values.put((Integer) (cm.getValue().get(0)), cm.getValue().get(1));
				}
				for (int i = 1; i <= values.size(); i++) {
					if (values.get(i) == null) {
						sb.append("NULL").append("\t");
					} else {
						if( values.get(i) instanceof StringReader) {
							StringReader v = (StringReader) values.get(i);
							int intValueOfChar;
							String readV = "";
							while( (intValueOfChar = v.read()) != -1 ) {
								readV += (char) intValueOfChar;
							}
							v.close();
							sb.append(readV).append("\t");
						} else {
							sb.append(values.get(i).toString()).append("\t");
						}
					}
				}
			}
		} catch (SQLException e) {
			new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
			return;
		} catch (IOException e) {
			new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
			return;
		} finally {
			if(afterPs != null) {
				try {
					afterPs.close();
				} catch (SQLException e) {
					new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
					return;
				}
			}
		}
		
		if(!ThreadContext.containsKey(detailCount + ":afterImage")) {
			ThreadContext.put(detailCount + ":afterImage", sb.toString());
		} else {
			detailCount = getDetailCount();
			ThreadContext.put(detailCount + ":afterImage", sb.toString());
		}
		
		log.info(String.format("processInsertAfterImg : Current detailcount[%d]", detailCount));
		
		if(!ThreadContext.containsKey(detailCount + ":selectCount")) {
			ThreadContext.put(detailCount + ":selectCount", String.valueOf(1));
		}
		
		if(!ThreadContext.containsKey(detailCount + ":sql")) {
			log.debug(String.format("%d:sql[%s]", detailCount, sql));
			ThreadContext.put(detailCount + ":sql", sql);
		}
		if(!ThreadContext.containsKey(detailCount + ":date")) {
			Date now = new Date(System.currentTimeMillis());
			SimpleDateFormat dsdf = new SimpleDateFormat(ApLogContext.date_format);
			ThreadContext.put(detailCount + ":date", dsdf.format(now));
			SimpleDateFormat tsdf = new SimpleDateFormat(ApLogContext.time_format);
			ThreadContext.put(detailCount + ":time", tsdf.format(now));
		}
	}

	private void processBeforeImg(ParseSqlResult psr, int detailCount) {
		if(!ThreadContext.containsKey("apLogUuid")) {
			return;
		}
		
		PreparedStatement beforePs = null;

		try {
			String selectSql = psr.getSelectSql().toString() + " ORDER BY ROWID";
			
			beforePs = ps.getConnection().prepareStatement(selectSql);
			if(psr.getSelectParaMethod() != null && psr.getSelectParaMethod().size() > 0) {
				for (CallMethod cm : psr.getSelectParaMethod()) {
					try {
						StringBuffer sb = new StringBuffer();
						for (Object o : cm.getValue()) {
							if (o != null)
								sb.append(o.toString()).append(", ");
						}
						Method m = PreparedStatement.class.getMethod(cm.getName(),
								cm.getType().toArray(new Class[cm.getType().size()]));
						m.invoke(beforePs, cm.getValue().toArray());
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						log.warn(e);
					}
				}
			} else {
				beforePs.clearParameters();
			}
			
			ResultSet beforeRs = beforePs.executeQuery();
			int cnt = 0;
			while (beforeRs.next()) {
				StringBuffer sb = new StringBuffer();
				for (int i = 1; i <= beforeRs.getMetaData().getColumnCount(); i++) {
					String tableName = "".equals(beforeRs.getMetaData().getTableName(i)) ? psr.getTableName() : beforeRs.getMetaData().getTableName(i);
					String data = null;
					if (beforeRs.getObject(i) != null) {
						if( beforeRs.getObject(i) instanceof StringReader) {
							StringReader v = (StringReader) beforeRs.getObject(i);
							int intValueOfChar;
							String readV = "";
							while( (intValueOfChar = v.read()) != -1 ) {
								readV += (char) intValueOfChar;
							}
							v.close();
							sb.append(readV).append("\t");
						} else {
							data = beforeRs.getObject(i).toString();
						}
					}
					if(data == null) {
						sb.append("NULL").append("\t");
					} else {
						if(ApLogContext.getTableMasks().containsKey(tableName)) {
							Map<String, IApLogMask> maskTypes = ApLogContext.getTableMasks().get(tableName);
							String columnName = beforeRs.getMetaData().getColumnName(i);
							if(maskTypes.containsKey(columnName)) {
								IApLogMask mask = maskTypes.get(columnName);
								data = mask.mask(data);
							}
						}
						sb.append(data).append("\t");
					}
				}
				
				if(!ThreadContext.containsKey(detailCount + ":beforeImage")) {
					ThreadContext.put(detailCount + ":beforeImage", sb.toString());
				} else {
					detailCount = getDetailCount();
					ThreadContext.put(detailCount + ":beforeImage", sb.toString());
				}
				
				log.info(String.format("processBeforeImg : Current detailcount[%d]", detailCount));
				
				ThreadContext.put(detailCount + ":selectCount", String.valueOf(1));
				
				if(!ThreadContext.containsKey(detailCount + ":sql")) {
					ThreadContext.put(detailCount + ":sql", sql);
				}
				if(!ThreadContext.containsKey(detailCount + ":date")) {
					Date now = new Date(System.currentTimeMillis());
					SimpleDateFormat dsdf = new SimpleDateFormat(ApLogContext.date_format);
					ThreadContext.put(detailCount + ":date", dsdf.format(now));
					SimpleDateFormat tsdf = new SimpleDateFormat(ApLogContext.time_format);
					ThreadContext.put(detailCount + ":time", tsdf.format(now));
				}
			}
			log.info(String.format("%d:beforeRs Cnt[%d]", detailCount, cnt));
		} catch (SQLException e) {
			new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
			return;
		} catch (IOException e) {
			new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
			return;
		} catch (Exception e) { 
			log.error(e);
		}
		finally {
			if(beforePs != null) {
				try {
					beforePs.close();
				} catch (SQLException e) {
					new DataAccessExceptionHandler().handleApLogException(sql, new DataAccessException(e));
					return;
				}
			}
		}
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setNull");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(sqlType);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(int.class);
		m.setType(t);
		callMethods.add(m);
		ps.setNull(parameterIndex, sqlType);
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setBoolean");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(boolean.class);
		m.setType(t);
		callMethods.add(m);
		ps.setBoolean(parameterIndex, x);
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setByte");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(byte.class);
		m.setType(t);
		callMethods.add(m);
		ps.setByte(parameterIndex, x);
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setShort");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(short.class);
		m.setType(t);
		callMethods.add(m);
		ps.setShort(parameterIndex, x);
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setInt");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(int.class);
		m.setType(t);
		callMethods.add(m);
		ps.setInt(parameterIndex, x);
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setLong");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(long.class);
		m.setType(t);
		callMethods.add(m);
		ps.setLong(parameterIndex, x);
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setFloat");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(float.class);
		m.setType(t);
		callMethods.add(m);
		ps.setFloat(parameterIndex, x);
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setDouble");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(double.class);
		m.setType(t);
		callMethods.add(m);
		ps.setDouble(parameterIndex, x);
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setBigDecimal");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(BigDecimal.class);
		m.setType(t);
		callMethods.add(m);
		ps.setBigDecimal(parameterIndex, x);
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setString");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(String.class);
		m.setType(t);
		callMethods.add(m);
		ps.setString(parameterIndex, x);
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setBytes");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(byte[].class);
		m.setType(t);
		callMethods.add(m);
		ps.setBytes(parameterIndex, x);
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setDate");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Date.class);
		m.setType(t);
		callMethods.add(m);
		ps.setDate(parameterIndex, x);
	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setTime");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Time.class);
		m.setType(t);
		callMethods.add(m);
		ps.setTime(parameterIndex, x);
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setTimestamp");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Timestamp.class);
		m.setType(t);
		callMethods.add(m);
		ps.setTimestamp(parameterIndex, x);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setAsciiStream");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(InputStream.class);
		t.add(int.class);
		m.setType(t);
		callMethods.add(m);
		ps.setAsciiStream(parameterIndex, x, length);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setUnicodeStream");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(InputStream.class);
		t.add(int.class);
		m.setType(t);
		callMethods.add(m);
		ps.setUnicodeStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setBinaryStream");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(InputStream.class);
		t.add(int.class);
		m.setType(t);
		callMethods.add(m);
		ps.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void clearParameters() throws SQLException {
		callMethods.clear();
		ps.clearParameters();
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setObject");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(targetSqlType);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Object.class);
		t.add(int.class);
		m.setType(t);
		callMethods.add(m);
		ps.setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setObject");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Object.class);
		m.setType(t);
		callMethods.add(m);
		ps.setObject(parameterIndex, x);
	}

	@Override
	public boolean execute() throws SQLException {
		ParseSqlResult psr = parseSql(sql, callMethods);
		int detailCount = getDetailCount();
		int startDetailCount = detailCount;
		if (psr != null && psr.isGetBefore()) {
			processBeforeImg(psr,detailCount);
		}
		boolean result;
		try {
			result = ps.execute();
		} catch (SQLException e) {
          logSqlError(e);
			DataAccessException dae = new DataAccessException(e);
			new DataAccessExceptionHandler().handleException(sql, dae);
			throw dae;
		}
		
		if (psr != null && psr.isInsert()) {
			processInsertAfterImg(psr, 0, detailCount);
		}
		if (psr != null && psr.isUpdate()) {
			processUpdateAfterImg(psr, 0, startDetailCount);
		}
		return result;
	}

	@Override
	public void addBatch() throws SQLException {
		List<CallMethod> mLists = new LinkedList<>();
		for(CallMethod m: callMethods) {
			mLists.add(m);
		}
		ParseSqlResult psr = parseSql(sql, mLists);
		psr.setInsertParaMethod(mLists);
		parseSqlResults.add(psr);
		callMethods.clear();
		ps.addBatch();
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setCharacterStream");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(reader);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Reader.class);
		t.add(int.class);
		m.setType(t);
		callMethods.add(m);
		ps.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setRef");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Ref.class);
		m.setType(t);
		callMethods.add(m);
		ps.setRef(parameterIndex, x);
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setBlob");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Blob.class);
		m.setType(t);
		callMethods.add(m);
		ps.setBlob(parameterIndex, x);
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setClob");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Clob.class);
		m.setType(t);
		callMethods.add(m);
		ps.setClob(parameterIndex, x);
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setArray");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Array.class);
		m.setType(t);
		callMethods.add(m);
		ps.setArray(parameterIndex, x);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return ps.getMetaData();
	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setDate");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(cal);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Date.class);
		t.add(Calendar.class);
		m.setType(t);
		callMethods.add(m);
		ps.setDate(parameterIndex, x, cal);
	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setTime");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(cal);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Time.class);
		t.add(Calendar.class);
		m.setType(t);
		callMethods.add(m);
		ps.setTime(parameterIndex, x, cal);
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setTimestamp");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(cal);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Timestamp.class);
		t.add(Calendar.class);
		m.setType(t);
		callMethods.add(m);
		ps.setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setNull");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(sqlType);
		p.add(typeName);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(int.class);
		t.add(String.class);
		m.setType(t);
		callMethods.add(m);
		ps.setNull(parameterIndex, sqlType, typeName);
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setURL");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(URL.class);
		m.setType(t);
		callMethods.add(m);
		ps.setURL(parameterIndex, x);
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return ps.getParameterMetaData();
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setRowId");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(RowId.class);
		m.setType(t);
		callMethods.add(m);
		ps.setRowId(parameterIndex, x);
	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setNString");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(value);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(String.class);
		m.setType(t);
		callMethods.add(m);
		ps.setNString(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setNCharacterStream");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(value);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Reader.class);
		t.add(long.class);
		m.setType(t);
		callMethods.add(m);
		ps.setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setNClob");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(value);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(NClob.class);
		m.setType(t);
		callMethods.add(m);
		ps.setNClob(parameterIndex, value);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setClob");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(reader);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Reader.class);
		t.add(long.class);
		m.setType(t);
		callMethods.add(m);
		ps.setClob(parameterIndex, reader, length);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setClob");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(inputStream);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(InputStream.class);
		t.add(long.class);
		m.setType(t);
		callMethods.add(m);
		ps.setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setNClob");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(reader);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Reader.class);
		t.add(long.class);
		m.setType(t);
		callMethods.add(m);
		ps.setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setSQLXML");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(xmlObject);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(SQLXML.class);
		m.setType(t);
		callMethods.add(m);
		ps.setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setObject");
		List<Object> p = new ArrayList<>(4);
		p.add(parameterIndex);
		p.add(x);
		p.add(targetSqlType);
		p.add(scaleOrLength);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(4);
		t.add(int.class);
		t.add(Object.class);
		t.add(int.class);
		t.add(int.class);
		m.setType(t);
		callMethods.add(m);
		ps.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setAsciiStream");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(InputStream.class);
		t.add(long.class);
		m.setType(t);
		callMethods.add(m);
		ps.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setBinaryStream");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(x);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(InputStream.class);
		t.add(long.class);
		m.setType(t);
		callMethods.add(m);
		ps.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setCharacterStream");
		List<Object> p = new ArrayList<>(3);
		p.add(parameterIndex);
		p.add(reader);
		p.add(length);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(3);
		t.add(int.class);
		t.add(Reader.class);
		t.add(long.class);
		m.setType(t);
		callMethods.add(m);
		ps.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setAsciiStream");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(InputStream.class);
		m.setType(t);
		callMethods.add(m);
		ps.setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setBinaryStream");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(x);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(InputStream.class);
		m.setType(t);
		callMethods.add(m);
		ps.setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setCharacterStream");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(reader);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Reader.class);
		m.setType(t);
		callMethods.add(m);
		ps.setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setNCharacterStream");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(value);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Reader.class);
		m.setType(t);
		callMethods.add(m);
		ps.setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setClob");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(reader);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Reader.class);
		m.setType(t);
		callMethods.add(m);
		ps.setClob(parameterIndex, reader);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setBlob");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(inputStream);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(InputStream.class);
		m.setType(t);
		callMethods.add(m);
		ps.setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		CallMethod m = new CallMethod();
		m.setName("setNClob");
		List<Object> p = new ArrayList<>(2);
		p.add(parameterIndex);
		p.add(reader);
		m.setValue(p);
		List<Class<?>> t = new ArrayList<>(2);
		t.add(int.class);
		t.add(Reader.class);
		m.setType(t);
		callMethods.add(m);
		ps.setNClob(parameterIndex, reader);
	}

    private void logSqlError(SQLException e) {
      int index = ApLogContext.getCurrentSqlIndex();
      if (index >= 0) {
        String errorCode = "ORA-" + e.getErrorCode();
        ThreadContext.put(index + ":sqlCode", errorCode);
      }
    }

    private int countJdbcParams(Expression expr) {
      if (expr == null)
        return 0;
      if (expr instanceof JdbcParameter)
        return 1;

      String s = expr.toString();
      int c = 0, from = 0;
      while ((from = s.indexOf("?", from)) >= 0) {
        c++;
        from++;
      }
      return c;
    }

}
