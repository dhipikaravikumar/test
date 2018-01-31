package com.gcit.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Publisher entity
 * @author meitantei
 *
 */
public class Publisher implements Serializable {

	private static final long serialVersionUID = -8441268676351108966L;
	
	private Integer pubId;
	private String name;
	private String phone;
	private String address;
	private List<Book> books;

	/**
	 * @return the pubId
	 */
	public Integer getPubId() {
		return pubId;
	}

	/**
	 * @param pubId
	 *            the pubId to set
	 */
	public void setPubId(Integer pubId) {
		this.pubId = pubId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the books
	 */
	public List<Book> getBooks() {
		return books;
	}

	/**
	 * @param books
	 *            the books to set
	 */
	public void setBooks(List<Book> books) {
		this.books = books;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pubId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Publisher other = (Publisher) obj;
		if (pubId != other.pubId)
			return false;
		return true;
	}

	
}
