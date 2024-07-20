package com.store.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
	
	private Integer price;
	private Integer orderedCopies;
	private Integer billId;
	private String bookName;
	private String category;
	
}
