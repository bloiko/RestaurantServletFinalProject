package database.dao;

/**
 * DB manager. Works with Apache Derby DB. 
 * Only the required DAO methods are defined!
 * 
 * @author D.Kolesnikov
 * 
 */
public class DBManager {}
	
/*
	private static final Logger log = Logger.getLogger(DBManager.class);
	

	// //////////////////////////////////////////////////////////
	// singleton
	// //////////////////////////////////////////////////////////

	private static DBManager instance;

	public static synchronized DBManager getInstance() {
		if (instance == null)
			instance = new DBManager();
		return instance;
	}
	
	*/
/**
	 * Returns a DB connection from the Pool Connections. Before using this
	 * method you must configure the Date Source and the Connections Pool in your
	 * WEB_APP_ROOT/META-INF/context.xml file.
	 * 
	 * @return A DB connection.
	 *//*

	public Connection getConnection() throws SQLException {
		Connection con = null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			
			// ST4DB - the name of data source
			DataSource ds = (DataSource)envContext.lookup("jdbc/ST4DB");
			con = ds.getConnection();
		} catch (NamingException ex) {
			log.error("Cannot obtain a connection from the pool", ex);			
		}
		return con;
	}

	private DBManager() {
	}


	// //////////////////////////////////////////////////////////
	// DB util methods
	// //////////////////////////////////////////////////////////

	*/
/**
	 * Commits and close the given connection.
	 * 
	 * @param con
	 *            Connection to be committed and closed.
	 *//*

	public void commitAndClose(Connection con) {
		try {
			con.commit();
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	*/
/**
	 * Rollbacks and close the given connection.
	 * 
	 * @param con
	 *            Connection to be rollbacked and closed.
	 *//*

	public void rollbackAndClose(Connection con) {
		try {
			con.rollback();
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
*/
/**************** THIS METHOD IS NOT USED IN THE PROJECT *******//*

	*/
/**
	 * Returns a DB connection. This method is just for a example how to use the
	 * DriverManager to obtain a DB connection. It does not use a pool
	 * connections and not used in this project. It is preferable to use
	 * {@link #getConnection()} method instead.
	 * 
	 * @return A DB connection.
	 *//*

	public Connection getConnectionWithDriverManager() throws SQLException {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = DriverManager
				.getConnection("jdbc:derby://localhost:1527/st4db;create=true;user=test;password=test");
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		connection.setAutoCommit(false);
		return connection;
	}
*/



