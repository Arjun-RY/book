package com.store.book.service;

import java.util.List;

import com.store.book.entity.BookEntity;

public interface BookService {

	public List<BookEntity> viewBooks(String role);
	
	public Boolean addBook(BookEntity bookEntity, Integer userId);
	
	public Boolean editBook(BookEntity bookEntity, Integer userId);
	
	public Boolean deleteBook(Integer bookId, Integer userId);
	
	public List<BookEntity> recentBooks();

	public String getBooks(String category);

	public String getPrice(String category, String book);
}
