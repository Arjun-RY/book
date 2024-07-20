package com.store.book.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ViewBillDTO {

	private Integer billId;
	private String customerName;
	private String sellerName;
	private Date billingDate;
	private Integer total;
	
}
