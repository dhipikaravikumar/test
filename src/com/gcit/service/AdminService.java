package com.gcit.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.connection.ConnectionUtil;
import com.gcit.dao.AuthorDAO;
import com.gcit.dao.BookDAO;
import com.gcit.dao.BookLoanDAO;
import com.gcit.dao.BorrowerDAO;
import com.gcit.dao.BranchDAO;
import com.gcit.dao.GenreDAO;
import com.gcit.dao.PublisherDAO;
import com.gcit.entity.Author;
import com.gcit.entity.Book;
import com.gcit.entity.BookLoan;
import com.gcit.entity.Borrower;
import com.gcit.entity.Branch;
import com.gcit.entity.Genre;
import com.gcit.entity.Publisher;
import com.gcit.utility.Utils;

/**
 * @author meitantei
 *
 */
public class AdminService {

	ConnectionUtil connUtil = new ConnectionUtil();

	/**
	 * admin privilege add an author
	 * 
	 * @param author
	 * @throws SQLException
	 */
	public void addAuthor(Author author) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			AuthorDAO adao = new AuthorDAO(conn);
			adao.addAuthor(author);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * admin privilege update author information
	 * 
	 * @param author
	 * @throws SQLException
	 */
	public void updateAuthor(Author author) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			AuthorDAO adao = new AuthorDAO(conn);
			adao.updateAuthor(author);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * admin privilege remove author
	 * 
	 * @param author
	 * @throws SQLException
	 */
	public void removeAuthor(Author author) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			AuthorDAO adao = new AuthorDAO(conn);
			adao.removeAuthor(author);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * admin privilege retrieve all authors
	 * 
	 * @throws SQLException
	 */
	public List<Author> retrieveAllAuthors() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			AuthorDAO adao = new AuthorDAO(conn);
			List<Author> authors = new ArrayList<>();
			authors = adao.retrieveAllAuthors();
			conn.commit();
			return authors;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}

	public List<Author> retrieveAuthorsByName(String name) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			AuthorDAO adao = new AuthorDAO(conn);
			List<Author> authors = new ArrayList<>();
			authors = adao.retrieveAuthorsByName(name);
			conn.commit();
			return authors;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}

	public List<Author> retrieveBookAuthors(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			AuthorDAO bdao = new AuthorDAO(conn);
			List<Author> authors = new ArrayList<>();
			authors = bdao.retrieveBookAuthors(book);
			conn.commit();
			return authors;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}

	public List<Author> retrieveAuthorByFilter(Author author) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			AuthorDAO adao = new AuthorDAO(conn);
			List<Author> authors = new ArrayList<>();
			authors = adao.retrieveAuthorByFilter(author);
			conn.commit();
			return authors;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}

	/**
	 * admin privilege add new book
	 * 
	 * @param book
	 * @throws SQLException
	 */
	public void addBook(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			AuthorDAO adao = new AuthorDAO(conn);
			GenreDAO gdao = new GenreDAO(conn);
			BranchDAO brdao = new BranchDAO(conn);
			PublisherDAO pdao = new PublisherDAO(conn);
			int bookId = bdao.addBookWithID(book);
			book.setBookId(bookId);

			// add to table book_authors if book has authors
			if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
				for (Author a : book.getAuthors()) {
					// check if its a new author and insert into author table, otherwise skip
					if (a.getAuthorId() == null) {
						a.setAuthorId(adao.addAuthorWithID(a));
					}
					// insert into book_authors table
					bdao.addBookAuthor(book, a);
				}
			}

			// bdao.savebookgenres
			if (book.getGenres() != null && !book.getGenres().isEmpty()) {
				for (Genre g : book.getGenres()) {
					// check if its a new genre and insert into genre table, otherwise skip
					if (g.getGenreId() == null) {
						g.setGenreId(gdao.addGenreWithID(g));
					}
					// insert into book_genres table
					bdao.addBookGenre(book, g);
				}
			}

			// pdao.savepublisher
			if (book.getPublisher() != null) {
				// check if its a new pub and insert into the pub table, otherwise skip
				if (book.getPublisher().getPubId() == null) {
					int pubId = pdao.addPublisherWithID(book.getPublisher());
					book.getPublisher().setPubId(pubId);
				}
				// update book with a new pub id
				bdao.updateBookPub(book, book.getPublisher());
			}

			// brdao.savebrach
			if (book.getBranches() != null && !book.getBranches().isEmpty()) {
				for (Branch b : book.getBranches()) {
					// check if its a new branch and insert into branch table, otherwise skip
					if (b.getBranchId() == null) {
						b.setBranchId(brdao.addBranchWithID(b));
					}
					// insert into book_branch table
					bdao.addBookBranchWithCopies(book, b);
				}
			}

			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * admin privilege update book details
	 * 
	 * @param book
	 * @throws SQLException
	 */
	public void updateBook(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			bdao.updateBook(book);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * admin privilege remove book
	 * 
	 * @param author
	 * @throws SQLException
	 */
	public void removeBook(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			bdao.removeBook(book);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * admin privilege retrieve all books
	 * 
	 * @throws SQLException
	 */
	public List<Book> retrieveAllBooks() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			List<Book> books = new ArrayList<>();
			books = bdao.retrieveAllBooks();
			conn.commit();
			return books;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}
	/*
	 * public void saveBook(Book book) throws SQLException{ Connection conn = null;
	 * try { conn = connUtil.getConnection(); BookDAO bdao = new BookDAO(conn);
	 * //bdao.saveBook(book); //bdao.savebookauthors //bdao.savebookgenres
	 * //pdao.savepublisher //brdao.savebrach conn.commit(); } catch
	 * (ClassNotFoundException | SQLException e) { e.printStackTrace();
	 * conn.rollback(); } finally{ conn.close(); }
	 * 
	 * }
	 */

	public void addGenre(Genre genre) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			GenreDAO gdao = new GenreDAO(conn);
			gdao.addGenre(genre);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * Update genre information
	 * 
	 * @param genre
	 * @throws SQLException
	 */
	public void updateGenre(Genre genre) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			GenreDAO gdao = new GenreDAO(conn);
			gdao.updateGenre(genre);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void removeGenre(Genre genre) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			GenreDAO gdao = new GenreDAO(conn);
			gdao.removeGenre(genre);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * retrieve all genres
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Genre> retrieveAllGenres() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			GenreDAO gdao = new GenreDAO(conn);
			List<Genre> genre = new ArrayList<>();
			genre = gdao.retrieveAllGenres();
			conn.commit();
			return genre;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}

	// library CRUD
	public void addBranch(Branch branch) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BranchDAO bdao = new BranchDAO(conn);
			bdao.addBranch(branch);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * Retrieve all branches available
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Branch> retrieveAllBranches() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BranchDAO bdao = new BranchDAO(conn);
			List<Branch> branches = new ArrayList<>();
			branches = bdao.retrieveAllBranches();
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

	public List<Branch> retrieveBookBranches(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BranchDAO bdao = new BranchDAO(conn);
			List<Branch> branches = new ArrayList<>();
			branches = bdao.retrieveBookBranches(book);
			conn.commit();
			return branches;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}

	public void updateBranch(Branch branch) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BranchDAO bdao = new BranchDAO(conn);
			bdao.updateBranch(branch);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void removeBranch(Branch branch) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BranchDAO bdao = new BranchDAO(conn);
			bdao.removeBranch(branch);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	// borrower crud
	/**
	 * Add a borrower
	 * 
	 * @param borrower
	 * @throws SQLException
	 */
	public void addBorrower(Borrower borrower) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			bdao.addBorrower(borrower);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public List<Borrower> retrieveAllBorrowers() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			List<Borrower> borrowers = new ArrayList<>();
			borrowers = bdao.retrieveAllBorrowers();
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

	public void updateBorrower(Borrower borrower) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			bdao.updateBorrower(borrower);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public List<Publisher> retrieveAllPublishers() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			List<Publisher> pubs = new ArrayList<>();
			pubs = bdao.retrieveAllPublishers();
			conn.commit();
			return pubs;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	public Publisher retrieveBookPublisher(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			List<Publisher> pubs = new ArrayList<>();
			pubs = bdao.retrieveBookPublisher(book);
			if (Utils.isEmpty(pubs)) {
				return null;
			}
			Publisher pub = pubs.get(0);
			conn.commit();
			return pub;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	public List<Publisher> retrieveAvailablePublisher(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			List<Publisher> pubs = new ArrayList<>();
			pubs = bdao.retrieveAvailablePublishers(book);
			conn.commit();
			return pubs;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	public void removeBorrower(Borrower borrower) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			bdao.removeBorrower(borrower);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	// publisher crud
	public void addPublisher(Publisher pub) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			bdao.addPublisher(pub);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void updatePublisher(Publisher pub) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			bdao.updatePublisher(pub);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void updateBookPublisher(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			if (Utils.isEmpty(book.getPublisher())) {
				bdao.removeBookPub(book);
			} else {
				bdao.updateBookPub(book, book.getPublisher());
			}

			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void removePublisher(Publisher pub) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			bdao.removePublisher(pub);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	/**
	 * Service for retrieving borrowers with due books
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Borrower> retrieveBorrowersWithDueBooks() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			List<Borrower> list = bdao.retrieveBorrowersWithDueBooks();

			conn.commit();
			return list;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	/**
	 * service for retrieving list of unreturned books for a single user
	 * 
	 * @param borrower
	 * @return
	 * @throws SQLException
	 */
	public List<BookLoan> retrieveUnreturnedBookByUser(Borrower borrower) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookLoanDAO bdao = new BookLoanDAO(conn);
			List<BookLoan> list = bdao.retrieveUnreturnedBookByUser(borrower);

			conn.commit();
			return list;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return null;
	}

	/**
	 * service for overriding due date of a particular loan
	 * 
	 * @param bl
	 * @throws SQLException
	 */
	public void overrideDueDate(BookLoan bl) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookLoanDAO bdao = new BookLoanDAO(conn);

			bdao.overrideDueDate(bl);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public List<Genre> retrieveBookGenres(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			GenreDAO gdao = new GenreDAO(conn);
			List<Genre> genre = new ArrayList<>();
			genre = gdao.retrieveBookGenres(book);
			conn.commit();
			return genre;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return null;
	}

	public void addBookAuthor(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);

			// add to table book_authors if book has authors
			if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
				for (Author a : book.getAuthors()) {
					// insert into book_authors table
					bdao.addBookAuthor(book, a);
				}
			}
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}
	
	public void removeBookAuthor(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);

			if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
				for (Author a : book.getAuthors()) {
					// insert into book_authors table
					bdao.removeBookAuthor(book, a);
				}
			}
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void addBookBranch(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);

			if (book.getBranches() != null && !book.getBranches().isEmpty()) {
				for (Branch b : book.getBranches()) {
					// insert into book_branch table
					bdao.addBookBranchWithCopies(book, b);
				}
			}
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void removeBookBranch(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);

			if (book.getBranches() != null && !book.getBranches().isEmpty()) {
				for (Branch b : book.getBranches()) {
					// delete from book_genres table
					bdao.removeBookBranch(book, b);
				}
			}
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void addBookGenre(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);

			if (book.getGenres() != null && !book.getGenres().isEmpty()) {
				for (Genre g : book.getGenres()) {
					// insert into book_genres table
					bdao.addBookGenre(book, g);
				}
			}
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void removeBookGenre(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);

			if (book.getGenres() != null && !book.getGenres().isEmpty()) {
				for (Genre g : book.getGenres()) {
					// delete from book_genres table
					bdao.removeBookGenre(book, g);
				}
			}
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}
}
