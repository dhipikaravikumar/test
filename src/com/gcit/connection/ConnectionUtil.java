package com.gcit.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for establishing db connection
 * @author meitantei
 *
 */
public class ConnectionUtil {
	public String driver = "com.mysql.cj.jdbc.Driver";
	public String url = "jdbc:mysql://localhost/library?useSSL=false&zeroDateTimeBehavior=convertToNull";
	public String username = "root";
	public String password = "admin";
	
	/**
	 * creates the connection
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		
		Connection conn = null;
		conn = DriverManager.getConnection(url, username, password);
		conn.setAutoCommit(Boolean.FALSE);
		return conn;
		
	}
}
