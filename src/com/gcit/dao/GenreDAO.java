package com.gcit.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.entity.Book;
import com.gcit.entity.Genre;

public class GenreDAO extends BaseDAO<Genre> {

	public GenreDAO(Connection conn) {
		super(conn);
	}

	public void addGenre(Genre genre) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(genre.getGenreName());
		save("insert into tbl_genre (genre_name) values (?)", list);
	}

	public Integer addGenreWithID(Genre genre) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(genre.getGenreName());
		return saveWithID("insert into tbl_genre (genre_name) values (?)", list);
	}

	public void updateGenre(Genre genre) throws SQLException, ClassNotFoundException {
		if (genre.getGenreName() == null)
			return;

		List<Object> list = new ArrayList<>();
		list.add(genre.getGenreName());
		list.add(genre.getGenreId());

		save("update tbl_genre set genre_name = ? where genre_id = ?", list);
	}

	public void removeGenre(Genre genre) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(genre.getGenreId());
		save("delete from tbl_genre where genre_id = ?", list);
	}

	public List<Genre> retrieveAllGenres() throws SQLException, ClassNotFoundException {
		return read("select * from tbl_genre", null);
	}

	public List<Genre> readGenresByName(String genreName) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(genreName);
		return read("select * from tbl_genre where genre_name = ?", list);
	}
	
	public List<Genre> retrieveBookGenres(Book book) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		return read("select * from tbl_book_genres a, tbl_genre b where a.genre_id = b.genre_id and bookId = ?", list);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		BookDAO bdao = new BookDAO(conn);
		List<Genre> genres = new ArrayList<>();
		while (rs.next()) {
			Genre a = new Genre();
			a.setGenreId(rs.getInt("genre_id"));
			a.setGenreName(rs.getString("genre_name"));
			a.setBooks(bdao.readFirstLevel(
					"select * from tbl_book where bookId IN (select bookId from tbl_book_genres where genre_id = ?)",
					new Object[] { a.getGenreId() }));
			genres.add(a);
		}
		return genres;
	}

	@Override
	public List<Genre> extractDataFirstLevel(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Genre> genres = new ArrayList<>();
		while (rs.next()) {
			Genre a = new Genre();
			a.setGenreId(rs.getInt("genre_id"));
			a.setGenreName(rs.getString("genre_name"));
			genres.add(a);
		}
		return genres;
	}
}
