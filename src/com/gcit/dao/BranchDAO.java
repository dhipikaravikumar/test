package com.gcit.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gcit.entity.Book;
import com.gcit.entity.Borrower;
import com.gcit.entity.Branch;
import com.gcit.utility.Utils;

public class BranchDAO extends BaseDAO<Branch> {

	public BranchDAO(Connection conn) {
		super(conn);
	}

	public void addBranch(Branch branch) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(branch.getName());
		list.add(branch.getAddress());
		save("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)", list);
	}

	public Integer addBranchWithID(Branch branch) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(branch.getName());
		list.add(branch.getAddress());
		return saveWithID("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)", list);
	}

	public void updateBranch(Branch branch) throws ClassNotFoundException, SQLException {
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("branchName", branch.getName());
		params.put("branchAddress", branch.getAddress());

		String sql = "UPDATE tbl_library_branch set ";

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

		sql += " where branchId = ?";

		list.add(branch.getBranchId());

		save(sql, list);
	}

	public void removeBranch(Branch branch) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(branch.getBranchId());
		save("delete from tbl_library_branch where branchId = ?", list);
	}

	public List<Branch> retrieveAllBranches() throws ClassNotFoundException, SQLException {
		return readFirstLevel("select * from tbl_library_branch", null);
	}
	
	public List<Branch> retrieveBookBranches(Book book) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		return read("select * from tbl_book_copies a, tbl_library_branch b where a.branchId = b.branchId and a.bookId = ?", list);
	}

	public List<Branch> retrieveBranchesWithDueBook(Borrower borrower) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(borrower.getCardNo());
		return read("select a.* from tbl_library_branch a, tbl_book_loans b " + 
				"where a.branchId = b.branchId " + 
				"and a.branchId in (select branchId from tbl_book_loans where " + 
				"cardNo = ? and dateIn is null) " + 
				"group by a.branchId", list);
	}

	public List<Branch> retrieveBranchByFilter(Branch branch) throws ClassNotFoundException, SQLException {

		String sql = "SELECT * FROM tbl_library_branch ";
		List<Object> list = new ArrayList<>();

		if (!Utils.isEmpty(branch.getBranchId())) {
			list.add(branch.getBranchId());
			sql += "WHERE branchId = ?";
		} else if (Utils.isEmpty(branch.getName())) {
			branch.setName("%" + branch.getName() + "%");
			list.add(branch.getName());
			sql += "WHERE branchName LIKE ?";
		} else if (Utils.isEmpty(branch.getAddress())) {
			branch.setAddress("%" + branch.getAddress() + "%");
			list.add(branch.getAddress());
			sql += "WHERE branchAddress LIKE ?";
		}

		return read(sql, list);
	}

	@Override
	public List<Branch> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		return extractDataFirstLevel(rs);
	}

	@Override
	public List<Branch> extractDataFirstLevel(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Branch> branch = new ArrayList<>();
		while (rs.next()) {
			Branch a = new Branch();

			a.setBranchId(rs.getInt("branchId"));
			a.setName(rs.getString("branchName"));
			a.setAddress(rs.getString("branchAddress"));
			branch.add(a);
		}
		return branch;
	}

}
