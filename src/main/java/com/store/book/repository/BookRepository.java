package com.store.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.store.book.dto.BookModelDTO;
import com.store.book.entity.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

	@Modifying
	@Transactional
	@Query(value = "UPDATE book"
			+ " SET book_name = :#{#book.bookName} ,"
			+ " author_name= :#{#book.authorName}, "
			+ " category = :#{#book.category},"
			+ " price = :#{#book.price}, "
			+ " available_copies= :#{#book.availableCopies}, "
			+ " last_updated_by= :#{#book.lastUpdatedBy}, "
			+ " last_updated_date = :#{#book.lastUpdatedDate} "
			+ " WHERE book_id = :#{#book.bookId} ", nativeQuery = true)
	Integer editBookById(@Param("book") BookEntity book);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE book"
			+ " SET soft_delete = 1 , last_updated_by = :userId ,"
			+ " last_updated_date = NOW() "
			+ " WHERE  book_id = :bookId ", nativeQuery = true)
	Integer softDelete(@Param("bookId") Integer bookId, @Param("userId") Integer userId);
	
	@Query(value = "SELECT * FROM book WHERE soft_delete = 0 ORDER BY last_updated_date DESC LIMIT 5", nativeQuery = true)
    List<BookEntity> getRecentBooks();
	
	@Query(value = "SELECT book_name FROM book WHERE soft_delete = 0 AND category = :category ORDER BY last_updated_date DESC", nativeQuery = true)
    List<String> getBooks(@Param("category") String category);

	@Query(value = "SELECT price, available_copies AS quantity FROM book WHERE category = :category AND book_name = :bookName ORDER BY last_updated_date DESC LIMIT 1", nativeQuery = true)
    BookModelDTO getPrice(@Param("category") String category, @Param("bookName") String bookName );

	@Modifying
	@Transactional
	@Query(value = "UPDATE book SET available_copies = available_copies - :orderedCopies "
	             + "WHERE book_name = :bookName AND category = :category", nativeQuery = true)
	void editCopies(@Param("bookName") String bookName, @Param("category") String category, @Param("orderedCopies") Integer orderedCopies);

}
