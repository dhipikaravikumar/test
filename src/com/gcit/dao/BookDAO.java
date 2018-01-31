package com.gcit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.entity.Author;
import com.gcit.entity.Book;
import com.gcit.entity.Branch;
import com.gcit.entity.Genre;
import com.gcit.entity.Publisher;

public class BookDAO extends BaseDAO<Book> {

	public BookDAO(Connection conn) {
		super(conn);
	}

	public void addBook(Book book) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookTitle());
		save("insert into tbl_book (title) values (?)", list);
	}

	public Integer addBookWithID(Book book) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookTitle());
		return saveWithID("insert into tbl_book (title) values (?)", list);
	}

	public void updateBook(Book book) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookTitle());
		list.add(book.getBookId());
		save("update tbl_book set title = ? where bookId = ?", list);
	}

	public void removeBook(Book book) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookTitle());
		save("delete from tbl_book where bookId = ?", list);
	}

	public List<Book> retrieveAllBooks() throws SQLException, ClassNotFoundException {
		return read("select * from tbl_book", null);
	}

	/**
	 * Retrieve all books for a particular branch
	 * 
	 * @param branch
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<Book> retrieveBookByBranch(Branch branch) throws SQLException, ClassNotFoundException {
		String sql = "select b.branchId, a.*, c.noOfCopies from tbl_book a, "
				+ "tbl_library_branch b, tbl_book_copies c "
				+ "where a.bookId = c.bookId and c.branchId = b.branchId and b.branchId = ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setObject(1, branch.getBranchId());

		ResultSet rs = pstmt.executeQuery();
		List<Book> books = new ArrayList<>();

		AuthorDAO adao = new AuthorDAO(conn);

		while (rs.next()) {
			Book a = new Book();
			a.setBookId(rs.getInt("bookId"));
			a.setBookTitle(rs.getString("title"));
			a.setNoOfCopies(rs.getInt("noOfCopies"));
			a.setAuthors(adao.readFirstLevel(
					"select * from tbl_author where authorId IN (select authorId from tbl_book_authors where bookId = ?)",
					new Object[] { a.getBookId() }));
			books.add(a);
		}
		return books;
	}

	public List<Book> retrieveBookDetailsByBranch(Branch branch) throws SQLException, ClassNotFoundException {
		String sql = "select a.*, b.branchId, b.noOfCopies from tbl_book a left join "
				+ "(select * from tbl_book_copies a where a.branchId = ?) b on a.bookId = b.bookId";

		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setObject(1, branch.getBranchId());

		ResultSet rs = pstmt.executeQuery();
		List<Book> books = new ArrayList<>();

		AuthorDAO adao = new AuthorDAO(conn);

		while (rs.next()) {
			Book a = new Book();
			a.setBookId(rs.getInt("bookId"));
			a.setBookTitle(rs.getString("title"));
			a.setNoOfCopies(rs.getInt("noOfCopies"));
			a.setAuthors(adao.readFirstLevel(
					"select * from tbl_author where authorId IN (select authorId from tbl_book_authors where bookId = ?)",
					new Object[] { a.getBookId() }));

			List<Branch> branches = new ArrayList<>();
			Branch b = new Branch();
			b.setBranchId(rs.getInt("branchId"));
			branches.add(b);

			a.setBranches(branches);
			books.add(a);
		}
		return books;
	}

	public void updateNoOfBookCopies(Book book, Branch branch) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getNoOfCopies());
		list.add(branch.getBranchId());
		list.add(book.getBookId());

		save("Update tbl_book_copies SET noOfCopies = ? where branchId = ? and bookId = ?", list);
	}

	public void addNewNoOfBookCopies(Book book, Branch branch) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(branch.getBranchId());
		list.add(book.getNoOfCopies());

		save("Insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)", list);
	}

	/**
	 * add an author for the book
	 * 
	 * @param book
	 * @param author
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void addBookAuthor(Book book, Author author) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(author.getAuthorId());
		save("insert into tbl_book_authors (bookId, authorId) values (?, ?)", list);
	}
	
	public void removeBookAuthor(Book book, Author author) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(author.getAuthorId());
		save("delete from tbl_book_authors where bookId = ? and authorId = ?", list);
	}

	/**
	 * add a genre for the book
	 * 
	 * @param book
	 * @param genre
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void addBookGenre(Book book, Genre genre) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(genre.getGenreId());
		save("insert into tbl_book_genres (bookId, genre_id) values (?, ?)", list);
	}
	
	public void removeBookGenre(Book book, Genre genre) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(genre.getGenreId());
		save("delete from tbl_book_genres where bookId = ? and genre_id = ?", list);
	}
	
	/**
	 * add a branch for the book
	 * 
	 * @param book
	 * @param branch
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void addBookBranch(Book book, Branch branch) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(branch.getBranchId());
		list.add(book.getNoOfCopies());
		save("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)", list);
	}
	
	public void removeBookBranch(Book book, Branch branch) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(branch.getBranchId());
		save("delete from tbl_book_copies where bookId = ? and branchId = ?", list);
	}
	
	/**
	 * add branch for the book with noOfCopies taken inside branch object
	 * @param book
	 * @param branch
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void addBookBranchWithCopies(Book book, Branch branch) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(branch.getBranchId());
		list.add(branch.getNoOfCopies());
		save("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)", list);
	}
	
	/**
	 * update pubId of a book
	 * @param book
	 * @param pub
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateBookPub(Book book, Publisher pub) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(pub.getPubId());
		list.add(book.getBookId());
		save("update tbl_book SET pubId = ? where bookId = ?", list);
	}
	
	public void removeBookPub(Book book) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		save("update tbl_book SET pubId = NULL where bookId = ?", list);
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		AuthorDAO adao = new AuthorDAO(conn);
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book a = new Book();
			a.setBookTitle(rs.getString("title"));
			a.setBookId(rs.getInt("bookId"));
			a.setAuthors(adao.readFirstLevel(
					"select * from tbl_author where authorId IN (select authorId from tbl_book_authors where bookId = ?)",
					new Object[] { a.getBookId() }));
			books.add(a);
		}
		return books;
	}

	/**
	 * Retrieve all books of a particular branch that has at least 1 copy and all copies are not rented yet
	 * 
	 * @param branch
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public List<Book> retrieveAvailableBookInABranch(Branch branch) throws ClassNotFoundException, SQLException {

		String sql = "select b.*,a.branchId from tbl_book_copies a join tbl_book b on a.bookId = b.bookId "
				+ "left join (select a.bookId, count(a.bookId) as rented from tbl_book_loans a "
				+ "where a.branchId = ? and a.dateIn is NULL group by a.bookId) c on a.bookId = c.bookId "
				+ "where a.noOfCopies > 0 and a.branchId = ? and a.noOfCopies > ifnull(rented,0)";

		List<Object> list = new ArrayList<>();
		list.add(branch.getBranchId());
		list.add(branch.getBranchId());

		return read(sql, list);
	}

	@Override
	public List<Book> extractDataFirstLevel(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book a = new Book();
			a.setBookId(rs.getInt("bookId"));
			a.setBookTitle(rs.getString("title"));
			books.add(a);
		}
		return books;
	}
}
