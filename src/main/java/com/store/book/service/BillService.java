package com.store.book.service;

import java.util.List;

import com.store.book.dto.BillDTO;
import com.store.book.dto.ViewBillDTO;

public interface BillService {

	public byte[] getPdfData(Integer billId);
	
	public List<ViewBillDTO> getBills(String role, String userName);
	
	public Integer createBill(BillDTO billDTO, Integer userId);
}
