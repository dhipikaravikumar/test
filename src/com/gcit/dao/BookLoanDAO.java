package com.gcit.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.entity.BookLoan;
import com.gcit.entity.Borrower;
import com.gcit.entity.Branch;

public class BookLoanDAO extends BaseDAO<BookLoan>{

	public BookLoanDAO(Connection conn) {
		super(conn);
	}
	
	public void loanAbook(BookLoan bl) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(bl.getBookId());
		list.add(bl.getBranchId());
		list.add(bl.getCardNo());
		list.add(bl.getDueDate());
		
		String sql = "insert into tbl_book_loans (bookId, branchId, cardNo, dueDate) values (?, ?, ?, ?)";
		
		save(sql, list);
	}
	
	/**
	 * Retrieve all due books by a particular borrower in a particular branch
	 * 
	 * @param borrower
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<BookLoan> retrieveBranchDueBookLoans(Borrower borrower, Branch branch) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(borrower.getCardNo());
		list.add(branch.getBranchId());
		
		return read("select * from tbl_book a, tbl_book_loans b" + 
				" where a.bookId = b.bookId" + 
				" and b.cardNo = ? and b.branchId = ? and b.dateIn is null", list);
	}
	
	/**
	 * update book loan table with date in for a particular book returned
	 * @param bl
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void returnABook(BookLoan bl) throws SQLException, ClassNotFoundException {

		List<Object> list = new ArrayList<>();
		list.add(bl.getDateIn());
		list.add(bl.getBookId());
		list.add(bl.getBranchId());
		list.add(bl.getCardNo());
		list.add(bl.getDateout());

		save("update tbl_book_loans set dateIn = ? where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?", list);
	}
	
	/**
	 * Update due date for a book loan
	 * @param bl
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void overrideDueDate(BookLoan bl) throws SQLException, ClassNotFoundException {

		List<Object> list = new ArrayList<>();
		list.add(bl.getDueDate());
		list.add(bl.getBookId());
		list.add(bl.getBranchId());
		list.add(bl.getCardNo());
		list.add(bl.getDateout());

		save("update tbl_book_loans set dueDate = ? where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?", list);
	}
	
	public List<BookLoan> retrieveUnreturnedBookByUser(Borrower borrower) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(borrower.getCardNo());
		
		return read("SELECT b.* FROM tbl_borrower a, tbl_book_loans b where a.cardNo = b.cardNo and b.dateIn is null and a.cardNo = ?", list);
	}
	
	@Override
	public List<BookLoan> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		BookDAO bdao = new BookDAO(conn);
		BorrowerDAO bodao = new BorrowerDAO(conn);
		List<BookLoan> bl = new ArrayList<>();
		while (rs.next()) {
			BookLoan a = new BookLoan();
			a.setBookId(rs.getInt("bookId"));
			a.setBranchId(rs.getInt("branchId"));
			a.setCardNo(rs.getInt("cardNo"));
			a.setDateIn(rs.getTimestamp("dateIn"));
			a.setDateout(rs.getTimestamp("dateOut"));
			a.setDueDate(rs.getTimestamp("dueDate"));
			List<Object> list = new ArrayList<>();
			list.add(rs.getInt("bookId"));
			
			//List<Book> books = bdao.read("select * from tbl_book where bookId = ?", list);
			//set the book retrieve 
			a.setBook(bdao.read("select * from tbl_book where bookId = ?", list).get(0));
			a.setBorrower(bodao.readFirstLevel("select * from tbl_borrower where cardNo = ?",new Object[] {rs.getInt("cardNo")} ).get(0));
			bl.add(a);
		}
		return bl;
	}

	@Override
	public List<BookLoan> extractDataFirstLevel(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<BookLoan> bls = new ArrayList<>();
		while (rs.next()) {
			BookLoan a = new BookLoan();
			a.setBookId(rs.getInt("bookId"));
			a.setBranchId(rs.getInt("branchId"));
			a.setCardNo(rs.getInt("cardNo"));
			a.setDateIn(rs.getTimestamp("dateIn"));
			a.setDateout(rs.getTimestamp("dateOut"));
			a.setDueDate(rs.getTimestamp("dueDate"));
			
			bls.add(a);
		}
		return bls;
	}
}
