package com.store.book.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="book")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookId;
	private String bookName;
	private String authorName;
	private String category;
	private Integer price;
	private Integer availableCopies;
	private Integer shopId;
	private Integer softDelete;
	private Integer createdBy;
	private Instant createdDate;
	private Integer lastUpdatedBy;
	private Instant lastUpdatedDate;
 
}
