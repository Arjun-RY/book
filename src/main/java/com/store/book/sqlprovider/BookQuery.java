package com.store.book.sqlprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookQuery {
	
	private final Logger logger = LoggerFactory.getLogger(BookQuery.class);
	
	public String getBooksQuery(String role) {
		
		logger.info("Entry point of getBooksQuery");
		String roleQuery = "";
		
		if(role.equals("Seller") || role.equals("Customer")) {
			roleQuery = " AND available_copies > 0 ";
		} else if (role.equals("Shop Keeper")) {
			roleQuery = "";
		} 
		
		String query = "SELECT * FROM book "
				+ " WHERE soft_delete = 0 "
				+ roleQuery + " ORDER BY book_id DESC;";
		
		logger.info("Exit point #1 of getBooksQuery, query={}",query);
		return query;
	}

}
