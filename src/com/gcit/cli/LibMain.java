package com.gcit.cli;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.gcit.entity.Author;
import com.gcit.entity.Book;
import com.gcit.entity.BookLoan;
import com.gcit.entity.Borrower;
import com.gcit.entity.Branch;
import com.gcit.entity.Genre;
import com.gcit.entity.Publisher;
import com.gcit.service.AdminService;
import com.gcit.service.BorrowerService;
import com.gcit.service.LibrarianService;
import com.gcit.utility.PrintUtils;
import com.gcit.utility.ScannerUtils;
import com.gcit.utility.Utils;

public class LibMain {
	// static ConnectionUtil connUtil = new ConnectionUtil();
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		Scanner sc = new Scanner(System.in);
		ScannerUtils myScanner = new ScannerUtils(sc);

		try {
			boolean firstMenu = true;
			while (firstMenu) {
				PrintUtils.printMainMenu();

				int choice = myScanner.getIntegerInput(4);
				System.out.println();

				switch (choice) {
				case 1: // case librarian
					LibrarianService libService = new LibrarianService();
					boolean isLibrarian = true;
					while (isLibrarian) {

						PrintUtils.printLibrarianMainMenu();

						int libMenuChoice = myScanner.getIntegerInput(2);
						System.out.println();

						if (libMenuChoice == 2) {
							break;
						}

						boolean inBranchList = true;
						while (inBranchList) {
							if (libMenuChoice == 1) {
								List<Branch> branches = libService.retrieveAllBranches();
								System.out.println("Choose a branch:");
								System.out.println();
								PrintUtils.printLibraryBranchList(branches);
								PrintUtils.printTakeInput();

								int libChoice = myScanner.getIntegerInput(branches.size() + 1);
								System.out.println();

								if (libChoice == branches.size() + 1) {
									break;
								} else if (libChoice > 0 && libChoice <= branches.size()) {
									Branch chosenBranch = branches.get(libChoice - 1);

									boolean updateLibraryMenu = true;
									while (updateLibraryMenu) {
										PrintUtils.printLibrarianInnerMenu();

										choice = myScanner.getIntegerInput(3);
										System.out.println();

										switch (choice) {
										case 1:
											// update details of the library
											PrintUtils.printUpdateLibraryDetails(chosenBranch);

											System.out.println(
													"Please enter new branch name or enter N/A for no change:");
											PrintUtils.printTakeInput();

											myScanner.getNextLine();
											String branchName = myScanner.getNextLine();
											System.out.println();

											if (Utils.checkIfQuit(branchName)) {
												break;
											}
											if (!Utils.checkIfNA(branchName)) {
												chosenBranch.setName(branchName);
											}

											System.out.println(
													"Please enter new branch address or enter N/A for no change:");

											PrintUtils.printTakeInput();

											String branchAddress = myScanner.getNextLine();
											System.out.println();

											if (Utils.checkIfQuit(branchAddress)) {
												break;
											}
											if (!Utils.checkIfNA(branchAddress)) {
												chosenBranch.setAddress(branchAddress);
											}

											// update the library branch details
											libService.updateBranch(chosenBranch);

											System.out.println("Library branch details successfully updated!");
											System.out.println();
											break;

										case 2:
											// add copies
											List<Book> books = libService.retrieveBookByBranch(chosenBranch);

											System.out.println(
													"Pick the Book you want to add copies of, to your branch:");
											System.out.println();
											PrintUtils.printBookList(books);
											PrintUtils.printTakeInput();

											int bookChoice = myScanner.getIntegerInput(books.size() + 1);
											System.out.println();

											if (bookChoice == books.size() + 1) {
												// System.out.println("exit");
												updateLibraryMenu = false;
												break;
											} else if (bookChoice > 0 && bookChoice <= books.size()) {
												Book chosenBook = books.get(bookChoice - 1);

												PrintUtils.printNoOfCopies(chosenBook.getNoOfCopies());
												PrintUtils.printTakeInput();

												int newNoOfCopies = myScanner.getIntegerInput(null);
												System.out.println();

												chosenBook.setNoOfCopies(newNoOfCopies);
												libService.updateNoOfBookCopies(chosenBook, chosenBranch);

												System.out.println("Successfully updated No of Book copies for book: "
														+ chosenBook.getBookTitle());
											}
											System.out.println();
											break;
										case 3:
											// return to previous menu
											updateLibraryMenu = false;
											break;

										default:
											break;
										}
									}
								}
							} /* else {} */
						}
					}
					break;
				case 2: // admin menu
					// System.out.println("Administrator Chosen");
					AdminService adminService = new AdminService();
					System.out.println("Hello admin, what do you want to do?\n");
					Boolean isAdmin = true;
					while (isAdmin) {
						PrintUtils.printAdminMainMenu();

						int adminChoice = myScanner.getIntegerInput(8);
						System.out.println();

						switch (adminChoice) {
						case 1:// crud for book
							boolean loopCheck = true;
							while (loopCheck) {
								PrintUtils.printGenericCRUDMenu("Book");
								List<Book> books;

								int bookChoice = myScanner.getIntegerInput(4);
								System.out.println();

								switch (bookChoice) {
								case 1: // add book
									Book book = new Book();
									String myAnswer = "";
									System.out.println("Please enter book title");
									PrintUtils.printTakeInput();
									myScanner.getNextLine();
									book.setBookTitle(myScanner.getNextLine());
									System.out.println();

									// add publisher
									System.out.println("Add publisher? y/n");
									PrintUtils.printTakeInput();

									// String answer = myScanner.getNextLine();
									myAnswer = myScanner.getYesOrNo();
									System.out.println();

									if (myAnswer.equals("y")) {
										boolean outerLoop = true;
										while (outerLoop) {
											System.out.println("1) Choose from existing publisher");
											System.out.println("2) Add a new publisher");
											System.out.println("3) Go previous");

											PrintUtils.printTakeInput();

											int pubChoice = myScanner.getIntegerInput(3);
											int innerInput = 0;

											switch (pubChoice) {
											case 1: // choose from existing pub
												List<Publisher> pubs = adminService.retrieveAllPublishers();
												PrintUtils.printPublisherList(pubs);
												PrintUtils.printTakeInput();

												innerInput = myScanner.getIntegerInput(pubs.size() + 1);
												if (innerInput == pubs.size() + 1)
													break;
												else if (innerInput > 0 && innerInput <= pubs.size()) {
													Publisher pubchosen = pubs.get(innerInput - 1);
													book.setPublisher(pubchosen);
													System.out.println("Publisher added to the book, moving on");
													System.out.println();
													outerLoop = false;
												}
												break;
											case 2: // add a new one
												Publisher pubNew = new Publisher();
												System.out.println("Please enter publisher name");
												PrintUtils.printTakeInput();
												myScanner.getNextLine();
												pubNew.setName(myScanner.getNextLine());
												System.out.println();

												System.out.println("Please enter publisher address");
												PrintUtils.printTakeInput();
												pubNew.setAddress(myScanner.getNextLine());
												System.out.println();

												System.out.println("Please enter publisher phone");
												PrintUtils.printTakeInput();
												pubNew.setPhone(myScanner.getNextLine());
												System.out.println();

												book.setPublisher(pubNew);
												System.out.println("Publisher added to the book, moving on");
												System.out.println();
												outerLoop = false;

												break;
											case 3:
												outerLoop = false;
												break;

											default:
												break;
											}
										}
									}

									// add author
									System.out.println("Add author? y/n");
									PrintUtils.printTakeInput();

									myAnswer = myScanner.getYesOrNo();
									System.out.println();

									if (myAnswer.equals("y")) {
										boolean outerLoop = true;
										List<Author> authorsToAdd = new ArrayList<>();
										while (outerLoop) {
											System.out.println("1) Choose from existing author");
											System.out.println("2) Add a new author");
											System.out.println("3) Go previous");

											PrintUtils.printTakeInput();

											int authorChoice = myScanner.getIntegerInput(3);
											int innerInput = 0;

											switch (authorChoice) {
											case 1: // choose from existing author
												List<Author> authors = adminService.retrieveAllAuthors();
												// if not empty, remove author from retrieve list
												if (!Utils.isEmpty(authorsToAdd)) {
													authors.removeAll(authorsToAdd);
												}
												PrintUtils.printAuthorList(authors);
												PrintUtils.printTakeInput();

												innerInput = myScanner.getIntegerInput(authors.size() + 1);
												if (innerInput == authors.size() + 1)
													break;
												else if (innerInput > 0 && innerInput <= authors.size()) {
													Author authorChosen = authors.get(innerInput - 1);
													authorsToAdd.add(authorChosen);
													System.out.println(
															"Author added to the book. You can add a co-author");
													System.out.println();
												}
												break;
											case 2: // add a new one
												Author authorNew = new Author();
												System.out.println("Please enter author name");
												PrintUtils.printTakeInput();
												myScanner.getNextLine();
												authorNew.setAuthorName(myScanner.getNextLine());
												System.out.println();

												authorsToAdd.add(authorNew);
												System.out.println("Author added to the book. You can add a co-author");
												System.out.println();
												break;
											case 3:
												outerLoop = false;
												break;

											default:
												break;
											}
										}
										// add to the book all the authors added
										if (!Utils.isEmpty(authorsToAdd)) {
											book.setAuthors(authorsToAdd);
										}
									}

									// add genre
									System.out.println("Add genre? y/n");
									PrintUtils.printTakeInput();

									myAnswer = myScanner.getYesOrNo();
									System.out.println();

									if (myAnswer.equals("y")) {
										boolean outerLoop = true;
										List<Genre> genresToAdd = new ArrayList<>();
										while (outerLoop) {
											System.out.println("1) Choose from existing genre");
											System.out.println("2) Add a new genre");
											System.out.println("3) Go previous");

											PrintUtils.printTakeInput();

											int genreChoice = myScanner.getIntegerInput(3);
											int innerInput = 0;

											switch (genreChoice) {
											case 1: // choose from existing author
												List<Genre> genres = adminService.retrieveAllGenres();
												// if not empty, remove author from retrieve list
												if (!Utils.isEmpty(genresToAdd)) {
													genres.removeAll(genresToAdd);
												}
												PrintUtils.printGenreList(genres);
												PrintUtils.printTakeInput();

												innerInput = myScanner.getIntegerInput(genres.size() + 1);
												if (innerInput == genres.size() + 1)
													break;
												else if (innerInput > 0 && innerInput <= genres.size()) {
													Genre genreChosen = genres.get(innerInput - 1);
													genresToAdd.add(genreChosen);
													System.out.println(
															"Book listed under a new genre. Can still add a new one");
													System.out.println();
												}
												break;
											case 2: // add a new one
												Genre genreNew = new Genre();
												System.out.println("Please enter genre name");
												PrintUtils.printTakeInput();
												myScanner.getNextLine();
												genreNew.setGenreName(myScanner.getNextLine());
												System.out.println();

												genresToAdd.add(genreNew);
												System.out.println(
														"Book listed under a new genre. Can still add a new one");
												System.out.println();
												break;
											case 3:
												outerLoop = false;
												break;

											default:
												break;
											}
										}
										// add to the book all the authors added
										if (!Utils.isEmpty(genresToAdd)) {
											book.setGenres(genresToAdd);
										}
									}

									// add branch copy
									System.out.println("Add Branch? y/n");
									PrintUtils.printTakeInput();

									myAnswer = myScanner.getYesOrNo();
									System.out.println();

									if (myAnswer.equals("y")) {
										boolean outerLoop = true;
										List<Branch> branchesToAdd = new ArrayList<>();
										while (outerLoop) {
											System.out.println("1) Choose from existing branches");
											System.out.println("2) Add a new branch");
											System.out.println("3) Go previous");

											PrintUtils.printTakeInput();

											int branchChoice = myScanner.getIntegerInput(3);
											int innerInput = 0;

											switch (branchChoice) {
											case 1: // choose from existing branch
												List<Branch> branches = adminService.retrieveAllBranches();
												// if not empty, remove author from retrieve list
												if (!Utils.isEmpty(branchesToAdd)) {
													branches.removeAll(branchesToAdd);
												}
												PrintUtils.printLibraryBranchList(branches);
												PrintUtils.printTakeInput();

												innerInput = myScanner.getIntegerInput(branches.size() + 1);
												if (innerInput == branches.size() + 1)
													break;
												else if (innerInput > 0 && innerInput <= branches.size()) {
													Branch branchChosen = branches.get(innerInput - 1);

													System.out.println(
															"Please enter no of copies for this book in this branch");
													PrintUtils.printTakeInput();
													int copies = myScanner.getIntegerInput(null);
													branchChosen.setNoOfCopies(copies);
													System.out.println();

													branchesToAdd.add(branchChosen);
													System.out.println(
															"Book added to branch. Can still add book to a new branch");
													System.out.println();
												}
												break;
											case 2: // add a new one
												Branch branchNew = new Branch();
												System.out.println("Please enter branch name");
												PrintUtils.printTakeInput();
												myScanner.getNextLine();
												branchNew.setName(myScanner.getNextLine());
												System.out.println();

												System.out.println("Please enter branch address");
												PrintUtils.printTakeInput();
												branchNew.setAddress(myScanner.getNextLine());
												System.out.println();

												System.out.println(
														"Please enter no of copies for this book in this branch");
												PrintUtils.printTakeInput();
												int copies = myScanner.getIntegerInput(null);
												branchNew.setNoOfCopies(copies);
												System.out.println();

												branchesToAdd.add(branchNew);
												System.out.println(
														"Book added to new branch. Can still add a new branch");
												System.out.println();
												break;
											case 3:
												outerLoop = false;
												break;

											default:
												break;
											}
										}
										// add to the book all the branches added
										if (!Utils.isEmpty(branchesToAdd)) {
											book.setBranches(branchesToAdd);
										}
									}

									// add the book
									adminService.addBook(book);

									System.out.println("Successfully added new book.");
									System.out.println();

									break;
								case 2: // update book
									List<Book> booksToUpdate = adminService.retrieveAllBooks();
									boolean updateloopCheck = true;
									while (updateloopCheck) {
										System.out.println("Choose which book to update");
										System.out.println();
										PrintUtils.printBookList(booksToUpdate);
										PrintUtils.printTakeInput();

										int updateChoice = myScanner.getIntegerInput(booksToUpdate.size() + 1);
										System.out.println();

										if (updateChoice == booksToUpdate.size() + 1) {
											break;
										} else if (updateChoice > 0 && updateChoice <= booksToUpdate.size()) {
											Book chosenBook = booksToUpdate.get(updateChoice - 1);

											boolean isBookDetails = true;
											// inner menu
											while (isBookDetails) {
												PrintUtils.printBookMenuUpdate();
												int BookDetailToUpdate = myScanner.getIntegerInput(6);
												System.out.println();

												switch (BookDetailToUpdate) {
												case 1:// update book title
													PrintUtils.printBookDetails(chosenBook);

													System.out.println(
															"Please enter new book title or enter N/A for no change:");
													PrintUtils.printTakeInput();

													myScanner.getNextLine();
													String bookTitle = myScanner.getNextLine();
													System.out.println();

													if (Utils.checkIfQuit(bookTitle)) {
														break;
													}
													if (!Utils.checkIfNA(bookTitle)) {
														chosenBook.setBookTitle(bookTitle);
													}

													adminService.updateBook(chosenBook);

													System.out.println("Book title successfully updated!");
													System.out.println();
													break;
												case 2: // book authors
													boolean isBookAuthor = true;
													while (isBookAuthor) {
														List<Author> currentBookAuthor = adminService
																.retrieveBookAuthors(chosenBook);

														PrintUtils.printBookAuthorsMenu();
														PrintUtils.printTakeInput();

														choice = myScanner.getIntegerInput(3);
														System.out.println();
														switch (choice) {
														case 1: // add
															if (Utils.isEmpty(currentBookAuthor)) {
																System.out.println("No author for this book yet");
															} else {
																// chosenBook.setGenres(currentBookGenres);
																System.out.print(
																		"This book is written under the following authors: ");
																boolean first = true;
																for (Author a : currentBookAuthor) {
																	if (first) {
																		System.out.print(a.getAuthorName());
																		first = false;
																	} else {
																		System.out.print(", " + a.getAuthorName());
																	}
																}
															}

															List<Author> availableBookAuthors = adminService
																	.retrieveAllAuthors();
															availableBookAuthors.removeAll(currentBookAuthor);

															System.out.println("\nAdd a new author");
															System.out.println();
															PrintUtils.printAuthorList(availableBookAuthors);
															PrintUtils.printTakeInput();

															int newAuthor = myScanner
																	.getIntegerInput(availableBookAuthors.size() + 1);
															System.out.println();

															if (newAuthor == availableBookAuthors.size() + 1) {
																break;
															} else if (newAuthor > 0
																	&& newAuthor <= availableBookAuthors.size()) {
																List<Author> addAUthors = new ArrayList<>();
																addAUthors.add(availableBookAuthors.get(newAuthor - 1));
																chosenBook.setAuthors(addAUthors);
																adminService.addBookAuthor(chosenBook);
																System.out.println("Added new author");
																System.out.println();
															}
															break;
														case 2: // remove
															if (Utils.isEmpty(currentBookAuthor)) {
																System.out.println("No author for this book yet");
																System.out.println("Returning to previous");
																System.out.println();
															} else {
																System.out.println(
																		"Choose which author to be remove from this book");
																System.out.println();
																PrintUtils.printAuthorList(currentBookAuthor);
																PrintUtils.printTakeInput();

																int authorToRemove = myScanner
																		.getIntegerInput(currentBookAuthor.size() + 1);
																System.out.println();

																if (authorToRemove == currentBookAuthor.size() + 1) {
																	break;
																} else if (authorToRemove > 0
																		&& authorToRemove <= currentBookAuthor.size()) {
																	List<Author> removeAuthors = new ArrayList<>();
																	removeAuthors.add(
																			currentBookAuthor.get(authorToRemove - 1));
																	chosenBook.setAuthors(removeAuthors);
																	adminService.removeBookAuthor(chosenBook);
																	System.out.println("Removed author");
																	System.out.println();
																}
															}
															break;
														case 3:// previous
															isBookAuthor = false;
															break;

														default:
															break;
														}
													}
													break;
												case 3: // book publisher
													boolean isBookPub = true;
													while (isBookPub) {
														Publisher currentPub = adminService
																.retrieveBookPublisher(chosenBook);
														chosenBook.setPublisher(currentPub);

														PrintUtils.printBookPublisherMenu();
														PrintUtils.printTakeInput();

														choice = myScanner.getIntegerInput(3);
														System.out.println();

														switch (choice) {
														case 1: // change pub
															if (Utils.isEmpty(currentPub)) {
																System.out.println(
																		"This book currently has no publisher");
																System.out.println();
															} else {
																chosenBook.setPublisher(currentPub);
																System.out.println(
																		"This book currently has this publisher: "
																				+ currentPub.getName());
																System.out.println();
															}
															List<Publisher> availablebookPubs = adminService
																	.retrieveAvailablePublisher(chosenBook);

															System.out.println("Choose new publisher from the list");
															System.out.println();
															PrintUtils.printPublisherList(availablebookPubs);
															PrintUtils.printTakeInput();

															int newPub = myScanner
																	.getIntegerInput(availablebookPubs.size() + 1);
															System.out.println();

															if (newPub == availablebookPubs.size() + 1) {
																break;
															} else if (newPub > 0
																	&& newPub <= availablebookPubs.size()) {
																chosenBook.setPublisher(
																		availablebookPubs.get(newPub - 1));
																adminService.updateBookPublisher(chosenBook);
																System.out.println("Publisher changed");
																System.out.println();
															}
															break;
														case 2: // remove pub
															if (Utils.isEmpty(currentPub)) {
																System.out.println(
																		"This book currently has no publisher");
																System.out.println("Returning to previous");
																System.out.println();
															} else {
																chosenBook.setPublisher(null);
																adminService.updateBookPublisher(chosenBook);
																// chosenBook.setPublisher(null);
																System.out.println("Publisher removed from this book");
																System.out.println();
															}
															break;
														case 3:
															isBookPub = false;
															break;
														}
													}
													break;
												case 4: // book genre
													boolean isBookGenre = true;
													while (isBookGenre) {
														List<Genre> currentBookGenres = adminService
																.retrieveBookGenres(chosenBook);

														PrintUtils.printBookGenresMenu();
														PrintUtils.printTakeInput();

														choice = myScanner.getIntegerInput(3);
														System.out.println();

														switch (choice) {
														case 1: // add
															if (Utils.isEmpty(currentBookGenres)) {
																System.out.println(
																		"This book currently has no genre it is under");
															} else {
																// chosenBook.setGenres(currentBookGenres);
																System.out.print(
																		"This book currently has the following genre: ");
																boolean first = true;
																for (Genre a : currentBookGenres) {
																	if (first) {
																		System.out.print(a.getGenreName());
																		first = false;
																	} else {
																		System.out.print(", " + a.getGenreName());
																	}
																}
															}

															List<Genre> availablebookGenres = adminService
																	.retrieveAllGenres();
															availablebookGenres.removeAll(currentBookGenres);

															System.out.println("\nChoose a new genre from the list");
															System.out.println();
															PrintUtils.printGenreList(availablebookGenres);
															PrintUtils.printTakeInput();

															int newGenre = myScanner
																	.getIntegerInput(availablebookGenres.size() + 1);
															System.out.println();

															if (newGenre == availablebookGenres.size() + 1) {
																break;
															} else if (newGenre > 0
																	&& newGenre <= availablebookGenres.size()) {
																List<Genre> addGenres = new ArrayList<>();
																addGenres.add(availablebookGenres.get(newGenre - 1));
																chosenBook.setGenres(addGenres);
																adminService.addBookGenre(chosenBook);
																System.out.println("Added new genre");
																System.out.println();
															}

															break;
														case 2: // remove
															if (Utils.isEmpty(currentBookGenres)) {
																System.out.println(
																		"This book currently is listed under no genre");
																System.out.println("Returning to previous");
																System.out.println();
															} else {
																System.out.println(
																		"Choose which genre to remove from this book");
																System.out.println();
																PrintUtils.printGenreList(currentBookGenres);
																PrintUtils.printTakeInput();

																int genreToRemove = myScanner
																		.getIntegerInput(currentBookGenres.size() + 1);
																System.out.println();

																if (genreToRemove == currentBookGenres.size() + 1) {
																	break;
																} else if (genreToRemove > 0
																		&& genreToRemove <= currentBookGenres.size()) {
																	List<Genre> removeGenres = new ArrayList<>();
																	removeGenres.add(
																			currentBookGenres.get(genreToRemove - 1));
																	chosenBook.setGenres(removeGenres);
																	adminService.removeBookGenre(chosenBook);
																	System.out.println("Removed chosen genre");
																	System.out.println();
																}
															}
															break;
														case 3:// previous
															isBookGenre = false;
															break;

														default:
															break;
														}
													}

													break;
												case 5: // book branch
													boolean isBookBranch = true;
													while (isBookBranch) {
														List<Branch> currentBookBranch = adminService
																.retrieveBookBranches(chosenBook);

														PrintUtils.printBookBranchesMenu();
														PrintUtils.printTakeInput();

														choice = myScanner.getIntegerInput(3);
														System.out.println();
														switch (choice) {
														case 1: // add
															if (Utils.isEmpty(currentBookBranch)) {
																System.out.println("No branch has this book yet");
																System.out.println();
															} else {
																// chosenBook.setGenres(currentBookGenres);
																System.out.print(
																		"This book is available to the following branches: ");
																boolean first = true;
																for (Branch a : currentBookBranch) {
																	if (first) {
																		System.out.print(a.getName());
																		first = false;
																	} else {
																		System.out.print(", " + a.getName());
																	}
																}
															}
															List<Branch> availablebookBranches = adminService
																	.retrieveAllBranches();
															availablebookBranches.removeAll(currentBookBranch);

															System.out.println("\nChoose a branch from the list");
															System.out.println();
															PrintUtils.printLibraryBranchList(availablebookBranches);
															PrintUtils.printTakeInput();

															int newBranch = myScanner
																	.getIntegerInput(availablebookBranches.size() + 1);
															System.out.println();

															if (newBranch == availablebookBranches.size() + 1) {
																break;
															} else if (newBranch > 0
																	&& newBranch <= availablebookBranches.size()) {
																List<Branch> addBranches = new ArrayList<>();
																addBranches
																		.add(availablebookBranches.get(newBranch - 1));
																chosenBook.setBranches(addBranches);
																adminService.addBookBranch(chosenBook);
																System.out.println("Added new branch");
																System.out.println();
															}
															break;
														case 2: // remove
															if (Utils.isEmpty(currentBookBranch)) {
																System.out.println("No branch has this book yet");
																System.out.println("Returning to previous");
																System.out.println();
															} else {
																System.out.println(
																		"Choose which branch to remove this book");
																System.out.println();
																PrintUtils.printLibraryBranchList(currentBookBranch);
																PrintUtils.printTakeInput();

																int branchToRemove = myScanner
																		.getIntegerInput(currentBookBranch.size() + 1);
																System.out.println();

																if (branchToRemove == currentBookBranch.size() + 1) {
																	break;
																} else if (branchToRemove > 0
																		&& branchToRemove <= currentBookBranch.size()) {
																	List<Branch> removeBranches = new ArrayList<>();
																	removeBranches.add(
																			currentBookBranch.get(branchToRemove - 1));
																	chosenBook.setBranches(removeBranches);
																	adminService.removeBookBranch(chosenBook);
																	System.out.println("Removed chosen branch");
																	System.out.println();
																}
															}
															break;
														case 3:// previous
															isBookBranch = false;
															break;

														default:
															break;
														}
													}
													break;
												case 6: // go previous
													isBookDetails = false;
													break;

												default:
													break;
												}

											}
										}
									}

									break;
								case 3: // remove
									books = adminService.retrieveAllBooks();
									System.out.println("Choose which book to delete");
									System.out.println();
									PrintUtils.printBookList(books);
									PrintUtils.printTakeInput();

									int deleteChoice = myScanner.getIntegerInput(books.size() + 1);
									System.out.println();

									if (deleteChoice == books.size() + 1) {
										break;
									} else if (deleteChoice > 0 && deleteChoice <= books.size()) {
										Book chosenBook = books.get(deleteChoice - 1);
										adminService.removeBook(chosenBook);

										System.out.println("Successfully remove Book");
										System.out.println();
									}
									break;
								case 4:
									loopCheck = false;
									break;

								default:
									break;
								}
							}
							break;
						case 2:// crud for author
							adminAuthorCRUD(myScanner, adminService);
							break;
						case 3:// crud for publisher
							adminPublisherCRUD(myScanner, adminService);
							break;
						case 4: // crud for branches
							adminBranchCRUD(myScanner, adminService);
							break;
						case 5: // crud for borrowers
							adminBorrowerCRUD(myScanner, adminService);
							break;
						case 6:// crud for genre
							adminGenreCRUD(myScanner, adminService);
							break;
						case 7: // override due date
							boolean overrideDueDate = true;
							while (overrideDueDate) {
								System.out.println("Choose from the list of borrowers with loaned books");
								System.out.println();

								List<Borrower> borrowers = adminService.retrieveBorrowersWithDueBooks();
								PrintUtils.printBorrowerList(borrowers);
								PrintUtils.printTakeInput();

								int borrowerChoice = myScanner.getIntegerInput(borrowers.size() + 1);
								System.out.println();

								if (borrowerChoice == borrowers.size() + 1) {
									break;
								} else if (borrowerChoice > 0 && borrowerChoice <= borrowers.size()) {
									Borrower chosenBorrower = borrowers.get(borrowerChoice - 1);
									boolean continueOperation = true;

									while (continueOperation) {
										System.out.println("Which loan due date would you like to override for user "
												+ chosenBorrower.getName() + "?");
										System.out.println();

										List<BookLoan> bl = adminService.retrieveUnreturnedBookByUser(chosenBorrower);
										PrintUtils.printBookLoanList(bl);
										PrintUtils.printTakeInput();

										int loanChoice = myScanner.getIntegerInput(bl.size() + 1);
										System.out.println();

										if (loanChoice == bl.size() + 1) {
											break;
										} else if (loanChoice > 0 && loanChoice <= bl.size()) {
											BookLoan loan = bl.get(loanChoice - 1);

											SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy");

											System.out.println("Due in: " + ft.format(loan.getDueDate()));
											System.out.println("Extend due date by how many days?");
											PrintUtils.printTakeInput();

											int daysExtend = myScanner.getIntegerInput(null);
											System.out.println();

											// extend days by input
											Calendar c = Calendar.getInstance();
											c.setTime(loan.getDueDate());
											c.add(Calendar.DATE, daysExtend);
											Date extendDueDate = c.getTime();
											Timestamp ts = new Timestamp(extendDueDate.getTime());
											loan.setDueDate(ts);

											adminService.overrideDueDate(loan);

											System.out.println("Successfully overridden due date!");
											System.out.println();
											break;
										}
									}
								}
							}
							break;
						case 8: // Option 7 for admin, return to main menu
							isAdmin = false;
							break;
						default:
							break;
						}
					}
					break;
				case 3: // borrower menu
					// System.out.println("Borrower chosen");
					BorrowerService borrowerService = new BorrowerService();

					Borrower borrower = null;
					Boolean isBorrower = true;
					Boolean cardDoesNoExist = true;

					while (isBorrower) {
						while (cardDoesNoExist) {
							System.out.println("Enter your card Number, enter 0 to go previous");
							PrintUtils.printTakeInput();

							int cardNo = myScanner.getIntegerInput(null);
							System.out.println();

							if (cardNo == 0) {
								isBorrower = false;
								break;
							}
							borrower = borrowerService.retrieveBorrowerByCardNo(cardNo);
							if (Utils.isEmpty(borrower)) {
								System.out.println("Borrower with card number: " + cardNo + " does not exist");
								System.out.println();
							} else {
								cardDoesNoExist = false;
							}
						}
						if (!isBorrower)
							break;

						System.out.println("Welcome " + borrower.getName() + ", What would you like to do?");

						PrintUtils.printBorrowerMainMenu();

						int borrowerchoice = myScanner.getIntegerInput(3);
						System.out.println();

						switch (borrowerchoice) {
						case 1:// borrow a book
							List<Branch> branches = borrowerService.retrieveAllBranches();

							System.out.println("Pick the Branch you want to check out from:");
							System.out.println();
							PrintUtils.printLibraryBranchList(branches);
							PrintUtils.printTakeInput();

							int libChoice = myScanner.getIntegerInput(branches.size() + 1);
							System.out.println();

							if (libChoice == branches.size() + 1) {
								// System.out.println("exit");
								// isBorrower = false;
								break;
							} else if (libChoice > 0 && libChoice <= branches.size()) {
								Branch chosenBranch = branches.get(libChoice - 1);
								List<Book> books = borrowerService.retrieveBranchBooksAvailable(chosenBranch);

								System.out.println("Pick the Book you want to check out:");
								System.out.println();
								PrintUtils.printBookList(books);
								PrintUtils.printTakeInput();

								int bookChoice = myScanner.getIntegerInput(books.size() + 1);
								System.out.println();

								if (bookChoice == books.size() + 1) {
									// System.out.println();
									// System.out.println("exit");
									// isBorrower = false;
									break;
								} else if (bookChoice > 0 && bookChoice <= books.size()) {
									Book chosenBook = books.get(bookChoice - 1);
									BookLoan bl = new BookLoan();
									bl.setBookId(chosenBook.getBookId());
									bl.setBranchId(chosenBranch.getBranchId());
									bl.setCardNo(borrower.getCardNo());
									borrowerService.loanABook(bl);

									System.out.println("Successfully borrowed book: " + chosenBook.getBookTitle());
									System.out.println();
								}
							}
							break;
						case 2: // return a book
							List<Branch> branchesWithDueBooks = borrowerService.retrieveBranchesWithDueBooks(borrower);

							System.out.println("Pick the branch you want to return a book to:");
							System.out.println();
							PrintUtils.printLibraryBranchList(branchesWithDueBooks);
							PrintUtils.printTakeInput();

							int branchChosen = myScanner.getIntegerInput(branchesWithDueBooks.size() + 1);
							System.out.println();

							if (branchChosen == branchesWithDueBooks.size() + 1) {
								break;
							} else if (branchChosen > 0 && branchChosen <= branchesWithDueBooks.size()) {
								Branch chosenBranch = branchesWithDueBooks.get(branchChosen - 1);
								List<BookLoan> loans = borrowerService.retrieveBranchDueBookLoans(borrower.getCardNo(),
										chosenBranch.getBranchId());
								System.out.println("Which book do you want to return?");
								System.out.println();
								PrintUtils.printBookLoanList(loans);
								PrintUtils.printTakeInput();

								int dueBookChosen = myScanner.getIntegerInput(loans.size() + 1);
								System.out.println();

								if (dueBookChosen == loans.size() + 1) {
									break;
								} else if (dueBookChosen > 0 && dueBookChosen <= loans.size()) {
									BookLoan bl = loans.get(dueBookChosen - 1);
									borrowerService.returnABook(bl);

									System.out
											.println("Successfully returned the book: " + bl.getBook().getBookTitle());
									System.out.println();
								}
							}
							break;
						case 3: // Option 3 for borrower, return to main menu
							isBorrower = false;
							break;
						default:
							break;
						}
					}
					break;
				case 4:// exit program
					System.out.println("Bye!");
					firstMenu = false;
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Ugg! something went wrong!");
		} finally {
			sc.close(); // close scanner
		}
	}

	private static void adminBorrowerCRUD(ScannerUtils myScanner, AdminService adminService) throws SQLException {
		boolean loopCheck = true;
		while (loopCheck) {
			List<Borrower> borrowers;
			PrintUtils.printGenericCRUDMenu("Borrower");

			int bInput = myScanner.getIntegerInput(4);
			System.out.println();

			switch (bInput) {
			case 1: // add
				Borrower borrower = new Borrower();
				System.out.println("Please enter borrower name");
				PrintUtils.printTakeInput();
				myScanner.getNextLine();
				borrower.setName(myScanner.getNextLine());
				System.out.println();

				System.out.println("Please enter borrower address");
				PrintUtils.printTakeInput();
				borrower.setAddress(myScanner.getNextLine());
				System.out.println();

				System.out.println("Please enter borrower phone");
				PrintUtils.printTakeInput();
				borrower.setPhone(myScanner.getNextLine());
				System.out.println();

				adminService.addBorrower(borrower);

				System.out.println("Successfully added new borrower");
				System.out.println();
				break;
			case 2: // update
				borrowers = adminService.retrieveAllBorrowers();
				System.out.println("Choose which borrower to update");
				System.out.println();
				PrintUtils.printBorrowerList(borrowers);
				PrintUtils.printTakeInput();

				int updateChoice = myScanner.getIntegerInput(borrowers.size() + 1);
				System.out.println();

				if (updateChoice == borrowers.size() + 1) {
					break;
				} else if (updateChoice > 0 && updateChoice <= borrowers.size()) {
					Borrower chosenB = borrowers.get(updateChoice - 1);

					boolean updateBorrower = true;
					while (updateBorrower) {
						System.out.println("You have chosen to update this borrower: \nCard no: " + chosenB.getCardNo()
								+ "\nName: " + chosenB.getName() + "\nAddress: " + chosenB.getAddress() + "\nPhone: "
								+ chosenB.getPhone() + "\n");
						System.out.println("Enter ‘quit’ at any prompt to cancel operation.");
						System.out.println();

						System.out.println("Please enter new borrower name or enter N/A for no change:");
						PrintUtils.printTakeInput();

						myScanner.getNextLine();
						String name = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(name))
							break;
						if (!Utils.checkIfNA(name))
							chosenB.setName(name);

						System.out.println("Please enter new borrower address or enter N/A for no change:");
						PrintUtils.printTakeInput();

						String address = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(address))
							break;
						if (!Utils.checkIfNA(address))
							chosenB.setAddress(address);

						System.out.println("Please enter new borrower phone number or enter N/A for no change:");
						PrintUtils.printTakeInput();

						String phone = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(phone))
							break;
						if (!Utils.checkIfNA(phone))
							chosenB.setPhone(phone);

						adminService.updateBorrower(chosenB);

						System.out.println("Successfully updated new borrower");
						System.out.println();

						break;
					}
				}
				break;
			case 3: // delete
				borrowers = adminService.retrieveAllBorrowers();
				System.out.println("Choose which borrower to delete");
				System.out.println();
				PrintUtils.printBorrowerList(borrowers);
				PrintUtils.printTakeInput();

				int deleteChoice = myScanner.getIntegerInput(borrowers.size() + 1);
				System.out.println();

				if (deleteChoice == borrowers.size() + 1) {
					break;
				} else if (deleteChoice > 0 && deleteChoice <= borrowers.size()) {
					Borrower chosenB = borrowers.get(deleteChoice - 1);
					adminService.removeBorrower(chosenB);

					System.out.println("Successfully remove borrower");
					System.out.println();
				}
				break;
			case 4:
				loopCheck = false;
				break;
			default:
				break;
			}
		}
	}

	private static void adminPublisherCRUD(ScannerUtils myScanner, AdminService adminService) throws SQLException {
		boolean loopCheck = true;
		while (loopCheck) {
			List<Publisher> publishers;
			PrintUtils.printGenericCRUDMenu("Publisher");

			int bInput = myScanner.getIntegerInput(4);
			System.out.println();

			switch (bInput) {
			case 1: // add
				Publisher publisher = new Publisher();
				System.out.println("Please enter publisher name");
				PrintUtils.printTakeInput();
				myScanner.getNextLine();
				publisher.setName(myScanner.getNextLine());
				System.out.println();

				System.out.println("Please enter publisher address");
				PrintUtils.printTakeInput();
				publisher.setAddress(myScanner.getNextLine());
				System.out.println();

				System.out.println("Please enter publisher phone");
				PrintUtils.printTakeInput();
				publisher.setPhone(myScanner.getNextLine());
				System.out.println();

				adminService.addPublisher(publisher);

				System.out.println("Successfully added new publisher");
				System.out.println();
				break;
			case 2: // update
				publishers = adminService.retrieveAllPublishers();
				System.out.println("Choose which publisher to update");
				System.out.println();
				PrintUtils.printPublisherList(publishers);
				PrintUtils.printTakeInput();

				int updateChoice = myScanner.getIntegerInput(publishers.size() + 1);
				System.out.println();

				if (updateChoice == publishers.size() + 1) {
					break;
				} else if (updateChoice > 0 && updateChoice <= publishers.size()) {
					Publisher chosenPub = publishers.get(updateChoice - 1);

					boolean updateBorrower = true;
					while (updateBorrower) {
						System.out.println("You have chosen to update this publisher: \nPubId: " + chosenPub.getPubId()
								+ "\nName: " + chosenPub.getName() + "\nAddress: " + chosenPub.getAddress()
								+ "\nPhone: " + chosenPub.getPhone() + "\n");
						System.out.println("Enter ‘quit’ at any prompt to cancel operation.");
						System.out.println();

						System.out.println("Please enter new Publisher name or enter N/A for no change:");
						PrintUtils.printTakeInput();

						myScanner.getNextLine();
						String name = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(name))
							break;
						if (!Utils.checkIfNA(name))
							chosenPub.setName(name);

						System.out.println("Please enter new Publisher address or enter N/A for no change:");
						PrintUtils.printTakeInput();

						String address = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(address))
							break;
						if (!Utils.checkIfNA(address))
							chosenPub.setAddress(address);

						System.out.println("Please enter new Publisher phone number or enter N/A for no change:");
						PrintUtils.printTakeInput();

						String phone = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(phone))
							break;
						if (!Utils.checkIfNA(phone))
							chosenPub.setPhone(phone);

						adminService.updatePublisher(chosenPub);

						System.out.println("Successfully updated new Publisher");
						System.out.println();

						break;
					}
				}
				break;
			case 3: // delete
				publishers = adminService.retrieveAllPublishers();
				System.out.println("Choose which publisher to delete");
				System.out.println();
				PrintUtils.printPublisherList(publishers);
				PrintUtils.printTakeInput();

				int deleteChoice = myScanner.getIntegerInput(publishers.size() + 1);
				System.out.println();

				if (deleteChoice == publishers.size() + 1) {
					break;
				} else if (deleteChoice > 0 && deleteChoice <= publishers.size()) {
					Publisher chosenPub = publishers.get(deleteChoice - 1);
					adminService.removePublisher(chosenPub);

					System.out.println("Successfully remove Publisher");
					System.out.println();
				}
				break;
			case 4:
				loopCheck = false;
				break;
			default:
				break;
			}
		}
	}

	private static void adminBranchCRUD(ScannerUtils myScanner, AdminService adminService) throws SQLException {
		boolean loopCheck = true;
		while (loopCheck) {
			List<Branch> branches;
			PrintUtils.printGenericCRUDMenu("Branch");

			int bInput = myScanner.getIntegerInput(4);
			System.out.println();

			switch (bInput) {
			case 1: // add
				Branch branch = new Branch();
				System.out.println("Please enter branch name");
				PrintUtils.printTakeInput();
				myScanner.getNextLine();
				branch.setName(myScanner.getNextLine());
				System.out.println();

				System.out.println("Please enter branch address");
				PrintUtils.printTakeInput();
				branch.setAddress(myScanner.getNextLine());
				System.out.println();

				adminService.addBranch(branch);

				System.out.println("Successfully added new branch");
				System.out.println();
				break;
			case 2: // update
				branches = adminService.retrieveAllBranches();
				System.out.println("Choose which branch to update");
				System.out.println();
				PrintUtils.printLibraryBranchList(branches);
				PrintUtils.printTakeInput();

				int updateChoice = myScanner.getIntegerInput(branches.size() + 1);
				System.out.println();

				if (updateChoice == branches.size() + 1) {
					break;
				} else if (updateChoice > 0 && updateChoice <= branches.size()) {
					Branch chosenBranch = branches.get(updateChoice - 1);

					boolean updateBorrower = true;
					while (updateBorrower) {
						System.out.println("You have chosen to update this branch: \nBranchId: "
								+ chosenBranch.getBranchId() + "\nName: " + chosenBranch.getName() + "\nAddress: "
								+ chosenBranch.getAddress() + "\n");
						System.out.println("Enter ‘quit’ at any prompt to cancel operation.");
						System.out.println();

						System.out.println("Please enter new Branch name or enter N/A for no change:");
						PrintUtils.printTakeInput();

						myScanner.getNextLine();
						String name = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(name))
							break;
						if (!Utils.checkIfNA(name))
							chosenBranch.setName(name);

						System.out.println("Please enter new Branch address or enter N/A for no change:");
						PrintUtils.printTakeInput();

						String address = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(address))
							break;
						if (!Utils.checkIfNA(address))
							chosenBranch.setAddress(address);

						adminService.updateBranch(chosenBranch);

						System.out.println("Successfully updated new Branch");
						System.out.println();

						break;
					}
				}
				break;
			case 3: // delete
				branches = adminService.retrieveAllBranches();
				System.out.println("Choose which branch to delete");
				System.out.println();
				PrintUtils.printLibraryBranchList(branches);
				PrintUtils.printTakeInput();

				int deleteChoice = myScanner.getIntegerInput(branches.size() + 1);
				System.out.println();

				if (deleteChoice == branches.size() + 1) {
					break;
				} else if (deleteChoice > 0 && deleteChoice <= branches.size()) {
					Branch chosenBranch = branches.get(deleteChoice - 1);
					adminService.removeBranch(chosenBranch);

					System.out.println("Successfully remove Branch");
					System.out.println();
				}
				break;
			case 4:
				loopCheck = false;
				break;
			default:
				break;
			}
		}
	}

	private static void adminGenreCRUD(ScannerUtils myScanner, AdminService adminService) throws SQLException {
		boolean loopCheck = true;
		while (loopCheck) {
			List<Genre> genres;
			PrintUtils.printGenericCRUDMenu("Genre");

			int bInput = myScanner.getIntegerInput(4);
			System.out.println();

			switch (bInput) {
			case 1: // add
				Genre genre = new Genre();
				System.out.println("Please enter genre name");
				PrintUtils.printTakeInput();
				myScanner.getNextLine();
				genre.setGenreName(myScanner.getNextLine());
				System.out.println();

				adminService.addGenre(genre);
				System.out.println("Successfully added new genre");
				System.out.println();
				break;
			case 2: // update
				genres = adminService.retrieveAllGenres();
				System.out.println("Choose which genre to update");
				System.out.println();
				PrintUtils.printGenreList(genres);
				PrintUtils.printTakeInput();

				int updateChoice = myScanner.getIntegerInput(genres.size() + 1);
				System.out.println();

				if (updateChoice == genres.size() + 1) {
					break;
				} else if (updateChoice > 0 && updateChoice <= genres.size()) {
					Genre chosenGenre = genres.get(updateChoice - 1);

					boolean updateBorrower = true;
					while (updateBorrower) {
						System.out.println("You have chosen to update this genre: \nGenreId: "
								+ chosenGenre.getGenreId() + "\nName: " + chosenGenre.getGenreName() + "\n");
						System.out.println("Enter ‘quit’ at any prompt to cancel operation.");
						System.out.println();

						System.out.println("Please enter new Genre name or enter N/A for no change:");
						PrintUtils.printTakeInput();

						myScanner.getNextLine();
						String name = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(name))
							break;
						if (!Utils.checkIfNA(name))
							chosenGenre.setGenreName(name);

						adminService.updateGenre(chosenGenre);

						System.out.println("Successfully updated new Genre");
						System.out.println();

						break;
					}
				}
				break;
			case 3: // delete
				genres = adminService.retrieveAllGenres();
				System.out.println("Choose which genre to delete");
				System.out.println();
				PrintUtils.printGenreList(genres);
				PrintUtils.printTakeInput();

				int deleteChoice = myScanner.getIntegerInput(genres.size() + 1);
				System.out.println();

				if (deleteChoice == genres.size() + 1) {
					break;
				} else if (deleteChoice > 0 && deleteChoice <= genres.size()) {
					Genre chosenGenre = genres.get(deleteChoice - 1);
					adminService.removeGenre(chosenGenre);

					System.out.println("Successfully remove Genre");
					System.out.println();
				}
				break;
			case 4:
				loopCheck = false;
				break;
			default:
				break;
			}
		}
	}

	private static void adminAuthorCRUD(ScannerUtils myScanner, AdminService adminService) throws SQLException {
		boolean loopCheck = true;
		while (loopCheck) {
			List<Author> authors;
			PrintUtils.printGenericCRUDMenu("Author");

			int bInput = myScanner.getIntegerInput(4);
			System.out.println();

			switch (bInput) {
			case 1: // add
				Author author = new Author();
				System.out.println("Please enter author name");
				PrintUtils.printTakeInput();
				myScanner.getNextLine();
				author.setAuthorName(myScanner.getNextLine());
				System.out.println();

				adminService.addAuthor(author);
				System.out.println("Successfully added new author");
				System.out.println();
				break;
			case 2: // update
				authors = adminService.retrieveAllAuthors();
				System.out.println("Choose which author to update");
				System.out.println();
				PrintUtils.printAuthorList(authors);
				PrintUtils.printTakeInput();

				int updateChoice = myScanner.getIntegerInput(authors.size() + 1);
				System.out.println();

				if (updateChoice == authors.size() + 1) {
					break;
				} else if (updateChoice > 0 && updateChoice <= authors.size()) {
					Author chosenAuthor = authors.get(updateChoice - 1);

					boolean updateAuthor = true;
					while (updateAuthor) {
						System.out.println("You have chosen to update this author: \nAuthorId: "
								+ chosenAuthor.getAuthorId() + "\nName: " + chosenAuthor.getAuthorName() + "\n");
						System.out.println("Enter ‘quit’ at any prompt to cancel operation.");
						System.out.println();

						System.out.println("Please enter new Author name or enter N/A for no change:");
						PrintUtils.printTakeInput();

						myScanner.getNextLine();
						String name = myScanner.getNextLine();
						System.out.println();
						if (Utils.checkIfQuit(name))
							break;
						if (!Utils.checkIfNA(name))
							chosenAuthor.setAuthorName(name);

						adminService.updateAuthor(chosenAuthor);

						System.out.println("Successfully updated new Author");
						System.out.println();

						break;
					}
				}
				break;
			case 3: // delete
				authors = adminService.retrieveAllAuthors();
				System.out.println("Choose which author to delete");
				System.out.println();
				PrintUtils.printAuthorList(authors);
				PrintUtils.printTakeInput();

				int deleteChoice = myScanner.getIntegerInput(authors.size() + 1);
				System.out.println();

				if (deleteChoice == authors.size() + 1) {
					break;
				} else if (deleteChoice > 0 && deleteChoice <= authors.size()) {
					Author chosenAuthor = authors.get(deleteChoice - 1);
					adminService.removeAuthor(chosenAuthor);

					System.out.println("Successfully remove Author");
					System.out.println();
				}
				break;
			case 4:
				loopCheck = false;
				break;
			default:
				break;
			}
		}
	}
}
