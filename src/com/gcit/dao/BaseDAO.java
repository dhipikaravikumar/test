package com.gcit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseDAO<T> {

	public static Connection conn = null;

	public BaseDAO(Connection conn) {
		BaseDAO.conn = conn;
	}

	public void save(String sql, List<Object> vals) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		if (vals != null) {
			int count = 1;
			for (Object o : vals) {
				pstmt.setObject(count, o);
				count++;
			}
		}
		pstmt.executeUpdate();
	}
	
	public Integer saveWithID(String sql, List<Object> vals) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		if (vals != null) {
			int count = 1;
			for (Object o : vals) {
				pstmt.setObject(count, o);
				count++;
			}
		}
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		while(rs.next()){
			return rs.getInt(1);
		}
		return null;
	}

	public List<T> read(String sql, List<Object> vals) throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		if (vals != null) {
			int count = 1;
			for (Object o : vals) {
				pstmt.setObject(count, o);
				count++;
			}
		}
		return extractData(pstmt.executeQuery());
	}

	public abstract List<T> extractData(ResultSet rs) throws ClassNotFoundException, SQLException;

	public List<T> readFirstLevel(String sql, Object[] vals) throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		if (vals != null) {
			int count = 1;
			for (Object o : vals) {
				pstmt.setObject(count, o);
				count++;
			}
		}
		return extractDataFirstLevel(pstmt.executeQuery());
	}

	public abstract List<T> extractDataFirstLevel(ResultSet rs) throws ClassNotFoundException, SQLException;
}
