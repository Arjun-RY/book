package com.store.book.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.book.dto.BookModelDTO;
import com.store.book.entity.BookEntity;
import com.store.book.repository.BookRepository;
import com.store.book.service.BookService;
import com.store.book.sqlprovider.BookQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class BookServiceImpl implements BookService {
	
	@PersistenceContext 
	private EntityManager entityManager;
	
	@Autowired
	private BookRepository bookRepository;
	
	private final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BookEntity> viewBooks(String role) {
		
		logger.info("Entry point of viewBooks role={}",role);
		BookQuery bookQuery = new BookQuery();
		String dynamicBookQuery = bookQuery.getBooksQuery(role);
		List<BookEntity> bookEntity = new ArrayList<>();
		Query query = entityManager.createNativeQuery(dynamicBookQuery, BookEntity.class);
		bookEntity = query.getResultList();
		
		logger.info("Exit point #1 of viewbooks");
		return bookEntity;
	}

	@Override
	public Boolean addBook(BookEntity bookEntity, Integer userId) {
		
		logger.info("Entry point of addBook bookEntity={},userId={}",bookEntity,userId);
		try {
			BookEntity newBook = new BookEntity();
			newBook.setAuthorName(bookEntity.getAuthorName());
			newBook.setAvailableCopies(bookEntity.getAvailableCopies());
			newBook.setBookName(bookEntity.getBookName());
			newBook.setCategory(bookEntity.getCategory());
			newBook.setPrice(bookEntity.getPrice());
			newBook.setShopId(1);
			newBook.setSoftDelete(0);
			newBook.setCreatedBy(userId);
			newBook.setLastUpdatedBy(userId);
			newBook.setCreatedDate(Instant.now());
			newBook.setLastUpdatedDate(Instant.now());
			BookEntity bookAdded = bookRepository.save(newBook);
			if(null != bookAdded && bookAdded.getBookId() != null) {
				logger.info("Exit point #1 of addbook");
				return true;
			}
			logger.info("Exit point #2 of addbook");
			return false;
		} catch (Exception e) {
			logger.error("Error while adding book, e={}",e);
			logger.info("Exit point #3 of addbook");
			return false;
		}
	}

	@Override
	public Boolean editBook(BookEntity bookEntity, Integer userId) {
		
		logger.info("Entry point of editBook bookEntity={},userId={}",bookEntity,userId);
		try {
			BookEntity editedBook = new BookEntity();
			editedBook.setBookId(bookEntity.getBookId());
			editedBook.setAuthorName(bookEntity.getAuthorName());
			editedBook.setAvailableCopies(bookEntity.getAvailableCopies());
			editedBook.setBookName(bookEntity.getBookName());
			editedBook.setCategory(bookEntity.getCategory());
			editedBook.setPrice(bookEntity.getPrice());
			editedBook.setCreatedBy(userId);
			editedBook.setCreatedDate(Instant.now());
			editedBook.setLastUpdatedBy(userId);
			editedBook.setLastUpdatedDate(Instant.now());
			Integer bookEdited = bookRepository.editBookById(editedBook);
			if(bookEdited >= 1) {
				logger.info("Exit point #1 of editbook");
				return true;
			}
			logger.info("Exit point #2 of editbook");
			return false;
		} catch (Exception e) {
			logger.error("Error while editing book, e={}",e);
			logger.info("Exit point #3 of editbook");
			return false;
		}
	}

	@Override
	public Boolean deleteBook(Integer bookId, Integer userId) {
		
		logger.info("Entry point of deleteBook bookId={},userId={}",bookId,userId);
		try {
			Integer deletedBook = bookRepository.softDelete(bookId,userId);
			if(deletedBook >= 1) {
				logger.info("Exit point #1 of deletebook");
				return true;
			}
			logger.info("Exit point #2 of deletebook");
			return false;
		} catch (Exception e) {
			logger.error("Error while editing book, e={}",e);
			logger.info("Exit point #3 of deletebook");
			return false;
		}
	}

	@Override
	public List<BookEntity> recentBooks() {
		
		logger.info("Entry point of recentBooks");
		try {
			List<BookEntity> books = new ArrayList<>();
			books = bookRepository.getRecentBooks();
			logger.info("Exit point #1 of recentBook");
			return books;
		} catch (Exception e) {
			logger.error("Error while getting book, e={}",e);
			logger.info("Exit point #2 of recentBook");
			return null;
		}
	}

	@Override
	public String getBooks(String category) {
		
		logger.info("Entry point of getBooks category={}",category);
		StringBuilder string = new StringBuilder();
		List<String> books = bookRepository.getBooks(category);
		if (null != books) {
            for (String book : books) {
                string.append("<option value='").append(book).append("'>").append(book).append("</option>");
            }
        }
		logger.info("Exit point #1 of getBooks");
		return string.toString();
	}

	@SuppressWarnings("unused")
	@Override
	public String getPrice(String category, String bookName) {
		
		StringBuilder books = new StringBuilder();
		String sql = "SELECT price, available_copies AS quantity FROM book WHERE category = :category AND book_name = :bookName ORDER BY last_updated_date DESC LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("category", category);
        query.setParameter("bookName", bookName);

        Object[] result = (Object[]) query.getSingleResult();
        BookModelDTO priceQuantity = new BookModelDTO((Integer) result[0], (Integer) result[1]);
		if (null != priceQuantity) {
            String jsonResponse = "{ \"price\": " +priceQuantity.getPrice() + ", \"quantity\": " + priceQuantity.getQuantity() + " }";
            logger.info("Exit point #1 of getBooks");
            return jsonResponse;
        }
        return "{}";
	}

}
