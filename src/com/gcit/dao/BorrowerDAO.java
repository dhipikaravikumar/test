package com.gcit.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gcit.entity.Borrower;

public class BorrowerDAO extends BaseDAO<Borrower> {

	public BorrowerDAO(Connection conn) {
		super(conn);
	}

	public void addBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(borrower.getName());
		list.add(borrower.getAddress());
		list.add(borrower.getPhone());
		save("insert into tbl_borrower (name, address, phone) values (?,?,?)", list);
	}

	public Integer addBorrowerWithID(Borrower borrower) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(borrower.getName());
		list.add(borrower.getAddress());
		list.add(borrower.getPhone());
		return saveWithID("insert into tbl_borrower (name, address, phone) values (?,?,?)", list);
	}

	public void updateBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("name", borrower.getName());
		params.put("address", borrower.getAddress());
		params.put("phone", borrower.getPhone());

		String sql = "UPDATE tbl_borrower set ";

		boolean first = true;
		List<Object> list = new ArrayList<>();

		for (String paramName : params.keySet()) {
			Object paramValue = params.get(paramName);
			if (paramValue != null) {
				if (first) {
					sql += paramName + "= ?";
					first = false;
					list.add(paramValue);
				} else {
					sql += ", " + paramName + "= ?";
					list.add(paramValue);
				}
			}
		}

		if (list.isEmpty())
			return;

		sql += " where cardNo = ?";
		list.add(borrower.getCardNo());

		save(sql, list);
	}

	public void removeBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(borrower.getCardNo());
		save("delete from tbl_borrower where cardNo = ?", list);
	}

	public List<Borrower> retrieveAllBorrowers() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_borrower", null);
	}

	// retrieve by filter
	public List<Borrower> retrieveBorrowerByFilter(Borrower borrower) throws ClassNotFoundException, SQLException {

		String sql = "SELECT * FROM tbl_borrower ";
		List<Object> list = new ArrayList<>();

		if (borrower.getCardNo() != null) {
			list.add(borrower.getCardNo());
			sql += "WHERE cardNo = ?";
		} else if (borrower.getName() != null) {
			borrower.setName("%" + borrower.getName() + "%");
			list.add(borrower.getName());
			sql += "WHERE name LIKE ?";
		} else if (borrower.getAddress() != null) {
			borrower.setAddress("%" + borrower.getName() + "%");
			list.add(borrower.getAddress());
			sql += "WHERE address LIKE ?";
		}

		return read(sql, list);
	}

	public List<Borrower> retrieveBorrowerByCardNo(Borrower borrower) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(borrower.getCardNo());
		return read("select * from tbl_borrower where cardNo = ?", list);
	}

	public List<Borrower> retrieveBorrowersWithDueBooks() throws ClassNotFoundException, SQLException {
		return read(
				"SELECT a.* FROM tbl_borrower a, tbl_book_loans b where a.cardNo = b.cardNo and b.dateIn is null group by a.cardNo",
				null);
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		BookLoanDAO bdao = new BookLoanDAO(conn);
		List<Borrower> borrowers = new ArrayList<>();
		while (rs.next()) {
			Borrower b = new Borrower();
			b.setCardNo(rs.getInt("cardNo"));
			b.setAddress(rs.getString("address"));
			b.setName(rs.getString("name"));
			b.setPhone(rs.getString("phone"));
			b.setLoans(bdao.readFirstLevel("select * from tbl_book_loans where cardNo = ?",
					new Object[] { b.getCardNo() }));
			borrowers.add(b);
		}
		return borrowers;
	}

	@Override
	public List<Borrower> extractDataFirstLevel(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		while (rs.next()) {
			Borrower b = new Borrower();
			b.setCardNo(rs.getInt("cardNo"));
			b.setAddress(rs.getString("address"));
			b.setName(rs.getString("name"));
			b.setPhone(rs.getString("phone"));
			borrowers.add(b);
		}
		return borrowers;
	}

}
