package com.gcit.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gcit.entity.Book;
import com.gcit.entity.Publisher;
import com.gcit.utility.Utils;

public class PublisherDAO extends BaseDAO<Publisher> {

	public PublisherDAO(Connection conn) {
		super(conn);
	}

	// add
	public void addPublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(publisher.getName());
		list.add(publisher.getAddress());
		list.add(publisher.getPhone());
		save("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?,?,?)", list);
	}

	// add with ID
	public Integer addPublisherWithID(Publisher publisher) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(publisher.getName());
		list.add(publisher.getAddress());
		list.add(publisher.getPhone());
		return saveWithID("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?,?,?)", list);
	}

	// update
	public void updatePublisher(Publisher publisher) throws ClassNotFoundException, SQLException {

		Map<String, Object> params = new LinkedHashMap<>();
		params.put("publisherName", publisher.getName());
		params.put("publisherAddress", publisher.getAddress());
		params.put("publisherPhone", publisher.getPhone());

		String sql = "UPDATE tbl_publisher set ";

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

		if(list.isEmpty()) return;
		
		sql += " where publisherId = ?";
		list.add(publisher.getPubId());
		
		save(sql, list);
	}

	public void removePublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(publisher.getPubId());
		save("delete from tbl_publisher where publisherId = ?", list);
	}

	public List<Publisher> retrieveAllPublishers() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_publisher", null);
	}
	
	public List<Publisher> retrieveAvailablePublishers(Book book) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		
		if(Utils.isEmpty(book.getPublisher())) {
			return retrieveAllPublishers();
		}
		//if book has a publisher use this sql
		list.add(book.getPublisher().getPubId());
		return read("select * from tbl_publisher a where a.publisherId != ?", list);
	}
	
	public List<Publisher> retrieveBookPublisher(Book book) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		return read("select a.* from tbl_publisher a, tbl_book b where a.publisherId = b.pubId and b.bookId = ?", list);
	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		BookDAO bdao = new BookDAO(conn);
		List<Publisher> pubs = new ArrayList<>();
		while (rs.next()) {
			Publisher a = new Publisher();
			a.setPubId(rs.getInt("publisherId"));
			a.setName(rs.getString("publisherName"));
			a.setAddress(rs.getString("publisherAddress"));
			a.setPhone(rs.getString("publisherPhone"));
			a.setBooks(bdao.readFirstLevel(
					"select * from tbl_book where bookId IN (select bookId from tbl_book where pubId = ?)",
					new Object[] { a.getPubId() }));
			pubs.add(a);
		}
		return pubs;
	}

	@Override
	public List<Publisher> extractDataFirstLevel(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Publisher> pubs = new ArrayList<>();
		while (rs.next()) {
			Publisher a = new Publisher();
			a.setPubId(rs.getInt("publisherId"));
			a.setName(rs.getString("publisherName"));
			a.setAddress(rs.getString("publisherAddress"));
			a.setPhone(rs.getString("publisherPhone"));
			pubs.add(a);
		}
		return pubs;
	}

}
