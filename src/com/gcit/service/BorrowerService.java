package com.gcit.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.gcit.connection.ConnectionUtil;
import com.gcit.dao.BookDAO;
import com.gcit.dao.BookLoanDAO;
import com.gcit.dao.BorrowerDAO;
import com.gcit.dao.BranchDAO;
import com.gcit.entity.Book;
import com.gcit.entity.BookLoan;
import com.gcit.entity.Borrower;
import com.gcit.entity.Branch;
import com.gcit.utility.Utils;

public class BorrowerService {

	ConnectionUtil connUtil = new ConnectionUtil();

	public List<Borrower> retrieveBorrowerByFilter(Borrower borrower) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			List<Borrower> borrowers = new ArrayList<Borrower>();
			borrowers = bdao.retrieveBorrowerByFilter(borrower);
			conn.commit();
			return borrowers;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	public Borrower retrieveBorrowerByCardNo(int cardNo) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			Borrower borrower = new Borrower();
			borrower.setCardNo(cardNo);

			List<Borrower> borrowers = new ArrayList<Borrower>();
			borrowers = bdao.retrieveBorrowerByCardNo(borrower);

			if (Utils.isEmpty(borrowers)) {
				return null;
			}
			borrower = borrowers.get(0);
			conn.commit();
			return borrower;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	public List<Branch> retrieveAllBranches() throws SQLException {
		AdminService adminService = new AdminService();
		return adminService.retrieveAllBranches();
	}

	/**
	 * Retrieve all branches in which borrower has due books to return
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Branch> retrieveBranchesWithDueBooks(Borrower borrower) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BranchDAO bdao = new BranchDAO(conn);
			List<Branch> branches = new ArrayList<>();
			branches = bdao.retrieveBranchesWithDueBook(borrower);
			conn.commit();
			return branches;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	/**
	 * service for retrieving available books for a branch
	 * 
	 * @param branch
	 * @return
	 * @throws SQLException
	 */
	public List<Book> retrieveBranchBooksAvailable(Branch branch) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			List<Book> books = new ArrayList<>();
			books = bdao.retrieveAvailableBookInABranch(branch);
			conn.commit();

			return books;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	/**
	 * Service for getting all due books for a particular borrower in a branch
	 * 
	 * @param cardNo
	 * @param branchId
	 * @return
	 * @throws SQLException
	 */
	public List<BookLoan> retrieveBranchDueBookLoans(Integer cardNo, Integer branchId) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookLoanDAO bdao = new BookLoanDAO(conn);
			Borrower borrower = new Borrower();
			borrower.setCardNo(cardNo);
			Branch branch = new Branch();
			branch.setBranchId(branchId);

			List<BookLoan> loans = new ArrayList<>();
			loans = bdao.retrieveBranchDueBookLoans(borrower, branch);
			conn.commit();

			return loans;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	/**
	 * Service allowing a user to borrow a book
	 * 
	 * @param bl
	 * @throws SQLException
	 */
	public void loanABook(BookLoan bl) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookLoanDAO bdao = new BookLoanDAO(conn);

			// get due date 7 days
			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 7);
			Date currentDatePlusOne = c.getTime();
			Timestamp ts = new Timestamp(currentDatePlusOne.getTime());
			bl.setDueDate(ts);

			bdao.loanAbook(bl);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}
	
	/**
	 * Service for returning a book
	 * @param bl
	 * @throws SQLException
	 */
	public void returnABook(BookLoan bl) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookLoanDAO bdao = new BookLoanDAO(conn);

			// get due date 7 days
			Date currentDate = new Date();
			Timestamp ts = new Timestamp(currentDate.getTime());
			bl.setDateIn(ts);
			bdao.returnABook(bl);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

}
