package com.gcit.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.connection.ConnectionUtil;
import com.gcit.dao.BookDAO;
import com.gcit.dao.BranchDAO;
import com.gcit.entity.Book;
import com.gcit.entity.Branch;

public class LibrarianService {
	
	ConnectionUtil connUtil = new ConnectionUtil();
	
	public List<Branch> retrieveAllBranches() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BranchDAO bdao = new BranchDAO(conn);
			List<Branch> branch = new ArrayList<>();
			//branch = adao.retrieveAllAuthors();
			branch = bdao.retrieveAllBranches();
			conn.commit();
			return branch;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}
	
	public void updateBranch(Branch branch) throws SQLException {
		AdminService admin = new AdminService();
		admin.updateBranch(branch);
	}
	
	public List<Book> retrieveBookByBranch(Branch branch) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			List<Book> books = new ArrayList<>();
			//branch = adao.retrieveAllAuthors();
			books = bdao.retrieveBookDetailsByBranch(branch);
			conn.commit();
			return books;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}
	
	public void updateNoOfBookCopies(Book book, Branch branch) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			//branch = adao.retrieveAllAuthors();
			if(book.getBranches().get(0).getBranchId() == null || book.getBranches().get(0).getBranchId() == 0) {
				bdao.addNewNoOfBookCopies(book, branch);
			} else {
				bdao.updateNoOfBookCopies(book, branch);
			}
			
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}
}
