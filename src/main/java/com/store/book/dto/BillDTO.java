package com.store.book.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BillDTO {

	private String sellerName;
	private String customerName;
	private Integer totalAmount;
	private List<BookDTO> bookDTO;
	
}
