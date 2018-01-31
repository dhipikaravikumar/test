package com.gcit.utility;

import java.text.SimpleDateFormat;
import java.util.List;

import com.gcit.entity.Author;
import com.gcit.entity.Book;
import com.gcit.entity.BookLoan;
import com.gcit.entity.Borrower;
import com.gcit.entity.Branch;
import com.gcit.entity.Genre;
import com.gcit.entity.Publisher;

public class PrintUtils {

	public static void printMainMenu() {
		System.out.println("Welcome to the GCIT Library Management System. Which category of a user are you\n");
		System.out.println("1) Librarian");
		System.out.println("2) Administrator");
		System.out.println("3) Borrower");
		System.out.println();
		System.out.println("4) Exit");

		printTakeInput();
	}

	public static void printLibrarianMainMenu() {
		System.out.println("Welcome Librarian, what do you want to do?");
		System.out.println("1) Enter Branch you manage");

		printGoPrevious(2);
		printTakeInput();
	}

	public static void printLibrarianInnerMenu() {
		System.out.println("1) Update the details of the library");
		System.out.println("2) Add copies of Book to the branch");
		System.out.println("3) Quit to previous");

		printTakeInput();
	}

	/**
	 * print library branches
	 * 
	 * @param branches
	 */
	public static void printLibraryBranchList(List<Branch> branches) {
		int count = 1;

		if (Utils.isEmpty(branches)) {
			System.out.println("Branch list is empty");
		} else {
			for (Branch b : branches) {
				System.out.println(count + ") " + b.getName() + ", " + b.getAddress());
				count++;
			}
		}
		printGoPrevious(count);
	}
	
	public static void printGenreList(List<Genre> genres) {
		int count = 1;

		if (Utils.isEmpty(genres)) {
			System.out.println("Genre list is empty");
		} else {
			for (Genre b : genres) {
				System.out.println(count + ") " + b.getGenreName());
				count++;
			}
		}
		printGoPrevious(count);
	}
	
	public static void printAuthorList(List<Author> authors) {
		int count = 1;

		if (Utils.isEmpty(authors)) {
			System.out.println("Author list is empty");
		} else {
			for (Author b : authors) {
				System.out.println(count + ") " + b.getAuthorName());
				count++;
			}
		}
		printGoPrevious(count);
	}

	/**
	 * print book list
	 * 
	 * @param books
	 */
	public static void printBookList(List<Book> books) {
		int count = 1;

		if (Utils.isEmpty(books)) {
			System.out.println("No Books found");
		} else {
			for (Book b : books) {
				System.out.print(count + ") " + b.getBookTitle() + " by ");

				if (Utils.isEmpty(b.getAuthors())) {
					System.out.println("???");
				} else {
					boolean first = true;
					for (Author a : b.getAuthors()) {
						if (first) {
							System.out.print(a.getAuthorName());
							first = false;
						} else {
							System.out.print(", " + a.getAuthorName());
						}
					}
					System.out.print("\n");
				}
				count++;
			}
		}
		System.out.println(count + ") Quit to cancel operation");
	}
	
	public static void printBorrowerList(List<Borrower> borrowers) {
		int count = 1;

		if (Utils.isEmpty(borrowers)) {
			System.out.println("Borrower list is empty");
		} else {
			for (Borrower b : borrowers) {
				System.out.println(count + ") " + b.getName());
				count++;
			}
		}
		System.out.println(count + ") Quit to cancel operation");
	}
	
	public static void printPublisherList(List<Publisher> pubs) {
		int count = 1;

		if (Utils.isEmpty(pubs)) {
			System.out.println("Publisher list is empty");
		} else {
			for (Publisher b : pubs) {
				System.out.println(count + ") " + b.getName());
				count++;
			}
		}
		System.out.println(count + ") Quit to cancel operation");
	}
	
	public static void printBookLoanList(List<BookLoan> loans) {
		int count = 1;
		if (Utils.isEmpty(loans)) {
			System.out.println("Great you have no due books");
		} else {

			for (BookLoan b : loans) {
				System.out.print(count + ") " + b.getBook().getBookTitle() + " by ");

				if (Utils.isEmpty(b.getBook().getAuthors())) {
					System.out.println("???");
				} else {
					boolean first = true;
					for (Author a : b.getBook().getAuthors()) {
						if (first) {
							System.out.print(a.getAuthorName());
							first = false;
						} else {
							System.out.print(", " + a.getAuthorName());
						}
					}
					System.out.print("\n");
				}
				if (Utils.isEmpty(b.getDueDate())) {
					System.out.println("\t Due Date: ???");
				} else {
					SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy");
					System.out.println("\t Due Date: " + ft.format(b.getDueDate()));
				}
				count++;
			}
		}

		System.out.println(count + ") Quit to cancel operation");
		System.out.println();
	}

	public static void printNoOfCopies(Integer noOfCopies) {
		int nCopies = noOfCopies != null ? noOfCopies : 0;

		System.out.println("Existing number of copies: " + nCopies);
		System.out.println();
		System.out.println("Enter new number of copies:");
	}

	public static void printUpdateLibraryDetails(Branch branch) {
		System.out.println("You have chosen to update the Branch with Branch Id: " + branch.getBranchId()
				+ " and Branch Name: " + branch.getName() + ".");
		System.out.println("Enter ‘quit’ at any prompt to cancel operation.");
		System.out.println();
	}
	
	public static void printBookDetails(Book book) {
		System.out.println("You have chosen to update the Book with title: " + book.getBookTitle() + ".");
		System.out.println("Enter ‘quit’ at any prompt to cancel operation.");
		System.out.println();
	}
	
	public static void printBookPublisherMenu() {
		System.out.println("1) Change publisher");
		System.out.println("2) Remove publisher");
		printGoPrevious(3);
		System.out.println();
	}
	
	public static void printBookGenresMenu() {
		System.out.println("1) Add Genre");
		System.out.println("2) Remove genre");
		printGoPrevious(3);
		System.out.println();
	}

	public static void printBookBranchesMenu() {
		System.out.println("1) Add book to a branch");
		System.out.println("2) Remove book from a branch");
		printGoPrevious(3);
		System.out.println();
	}
	
	public static void printBookAuthorsMenu() {
		System.out.println("1) Add book under an author");
		System.out.println("2) Remove book from an author");
		printGoPrevious(3);
		System.out.println();
	}

	/**
	 * print the borrower main menu
	 */
	public static void printBorrowerMainMenu() {
		System.out.println("1) Check out a book");
		System.out.println("2) Return a book");
		System.out.println("3) Quit to previous");

		printTakeInput();
	}
	
	/**
	 * print the admin main menu
	 * 
	 */
	public static void printAdminMainMenu() {
		System.out.println("1) Add/Update/Delete Books");
		System.out.println("2) Add/Update/Delete Authors");
		System.out.println("3) Add/Update/Delete Publishers");
		System.out.println("4) Add/Update/Delete Library Branches");
		System.out.println("5) Add/Update/Delete Borrowers");
		System.out.println("6) Add/Update/Delete Genres");
		System.out.println("7) Over-ride Due Date for a Book Loan");
		
		printGoPrevious(8);
		printTakeInput();
	}
	
	public static void printBookMenuUpdate() {
		System.out.println("What details of this book do you want to update?\n");
		System.out.println("1) Book Title");
		System.out.println("2) Authors");
		System.out.println("3) Publisher");
		System.out.println("4) Genre");
		System.out.println("5) Branches");
		
		printGoPrevious(6);
		printTakeInput();
	}

	public static void printGenericCRUDMenu(String s) {
		System.out.println("1) Add a " + s);
		System.out.println("2) Update a " + s);
		System.out.println("3) Delete a "+ s);
		
		printGoPrevious(4);
		printTakeInput();
	}
	
	public static void printTakeInput() {
		System.out.println();
		System.out.print("<take input> ");
	}

	public static void printGoPrevious(int count) {
		System.out.println(count + ") Quit to previous");
	}
}
