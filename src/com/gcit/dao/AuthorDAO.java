package com.gcit.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.entity.Author;
import com.gcit.entity.Book;

public class AuthorDAO extends BaseDAO<Author> {

	public AuthorDAO(Connection conn) {
		super(conn);
	}

	public void addAuthor(Author author) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(author.getAuthorName());
		save("insert into tbl_author (authorName) values (?)", list);
	}

	public Integer addAuthorWithID(Author author) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(author.getAuthorName());
		return saveWithID("insert into tbl_author (authorName) values (?)", list);
	}

	public void updateAuthor(Author author) throws SQLException, ClassNotFoundException {
		if (author.getAuthorName() == null)
			return;
		List<Object> list = new ArrayList<>();
		list.add(author.getAuthorName());
		list.add(author.getAuthorId());
		save("update tbl_author set authorName = ? where authorId = ?", list);
	}

	public void removeAuthor(Author author) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(author.getAuthorName());
		save("delete from tbl_author where authorId = ?", list);
	}

	public List<Author> retrieveAllAuthors() throws SQLException, ClassNotFoundException {
		return read("select * from tbl_author", null);
	}
	
	public List<Author> retrieveBookAuthors(Book book) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		return read("select * from tbl_book_authors a, tbl_author b where a.authorId = b.authorId and a.bookId = ?", list);
	}

	public List<Author> retrieveAuthorsByName(String authorName) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(authorName);
		return read("select * from tbl_author where authorName = ?", list);
	}

	public List<Author> retrieveAuthorByFilter(Author author) throws ClassNotFoundException, SQLException {

		String sql = "SELECT * FROM tbl_author ";
		List<Object> list = new ArrayList<>();

		if (author.getAuthorId() != null) {
			list.add(author.getAuthorId());
			sql += "WHERE authorId = ?";
		} else if (author.getAuthorName() != null) {
			author.setAuthorName("%" + author.getAuthorName() + "%");
			list.add(author.getAuthorName());
			sql += "WHERE authorName LIKE ?";
		}

		return read(sql, list);
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		BookDAO bdao = new BookDAO(conn);
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			a.setBooks(bdao.readFirstLevel(
					"select * from tbl_book where bookId IN (select bookId from tbl_book_authors where authorId = ?)",
					new Object[] { a.getAuthorId() }));
			authors.add(a);
		}
		return authors;
	}

	@Override
	public List<Author> extractDataFirstLevel(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			authors.add(a);
		}
		return authors;
	}

}
