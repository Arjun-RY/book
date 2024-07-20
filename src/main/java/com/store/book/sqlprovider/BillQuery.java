package com.store.book.sqlprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BillQuery {

	private final Logger logger = LoggerFactory.getLogger(BillQuery.class);
	
	public String getBillsQuery(String role, String userName) {
		
		logger.info("Entry point of getBillsQuery");
		String roleQuery = "";
		
		if(role.equals("Seller")) {
			roleQuery = " WHERE seller_name = '" + userName +"'";
		}else if (role.equals("Customer")) {
			roleQuery = " WHERE customer_name = '" + userName +"'";
		}else if (role.equals("Shop Keeper")) {
			roleQuery = "";
		} 
		
		String query = "SELECT bill_id, customer_name, seller_name, billing_date, total FROM bill "
				+ roleQuery + " ORDER BY bill_id DESC;";
		
		logger.info("Exit point #1 of getBillsQuery , query={}", query);
		return query;
	}
}
