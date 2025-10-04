package nccc.btp.aplog.exception;

import java.io.IOException;
import java.sql.SQLException;

public class DataAccessException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1453572169818570817L;
	
	public DataAccessException(SQLException e) {
		super(e);
	}
	public DataAccessException(IOException e) {
		super(e);
	}
}
