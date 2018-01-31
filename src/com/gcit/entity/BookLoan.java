package com.gcit.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class BookLoan implements Serializable {

	private static final long serialVersionUID = -690628012283481833L;

	private Integer bookId;
	private Integer branchId;
	private Integer cardNo;
	private Timestamp dateout;
	private Timestamp dueDate;
	private Timestamp dateIn;
	private Book book;
	private Borrower borrower;
	
	/**
	 * @return the bookId
	 */
	public Integer getBookId() {
		return bookId;
	}

	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	/**
	 * @return the branchId
	 */
	public Integer getBranchId() {
		return branchId;
	}

	/**
	 * @param branchId the branchId to set
	 */
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	/**
	 * @return the cardNo
	 */
	public Integer getCardNo() {
		return cardNo;
	}

	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}

	/**
	 * @return the dateout
	 */
	public Timestamp getDateout() {
		return dateout;
	}

	/**
	 * @param dateout the dateout to set
	 */
	public void setDateout(Timestamp dateout) {
		this.dateout = dateout;
	}

	/**
	 * @return the dueDate
	 */
	public Timestamp getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the dateIn
	 */
	public Timestamp getDateIn() {
		return dateIn;
	}

	/**
	 * @param dateIn the dateIn to set
	 */
	public void setDateIn(Timestamp dateIn) {
		this.dateIn = dateIn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((branchId == null) ? 0 : branchId.hashCode());
		result = prime * result + ((cardNo == null) ? 0 : cardNo.hashCode());
		result = prime * result + ((dateout == null) ? 0 : dateout.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookLoan other = (BookLoan) obj;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		if (branchId == null) {
			if (other.branchId != null)
				return false;
		} else if (!branchId.equals(other.branchId))
			return false;
		if (cardNo == null) {
			if (other.cardNo != null)
				return false;
		} else if (!cardNo.equals(other.cardNo))
			return false;
		if (dateout == null) {
			if (other.dateout != null)
				return false;
		} else if (!dateout.equals(other.dateout))
			return false;
		return true;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Borrower getBorrower() {
		return borrower;
	}

	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}
}
