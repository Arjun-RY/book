package com.store.book.service.impl;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import com.store.book.dto.BillDTO;
import com.store.book.dto.BookDTO;
import com.store.book.dto.ViewBillDTO;
import com.store.book.entity.BillDetailsEntity;
import com.store.book.entity.BillEntity;
import com.store.book.repository.BillDetailsRepository;
import com.store.book.repository.BillRepository;
import com.store.book.repository.BookRepository;
import com.store.book.service.BillService;
import com.store.book.sqlprovider.BillQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class BillServiceImpl implements BillService{
	
	private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

	@Autowired
	private BillRepository billRepository;
	
	@Autowired 
	private BillDetailsRepository billDetailsRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@PersistenceContext 
	private EntityManager entityManager;
	
	@Override
	public byte[] getPdfData(Integer billId) {
		
		logger.info("Entry point of getPdfData billId={}",billId);
		byte[] pdfData = null;
		Optional<BillEntity> billEntity = billRepository.findById(billId);
		if (billEntity.isPresent()) {
			pdfData = billEntity.get().getPdfData();	        
	    }
		logger.info("Exit point #1 of getPdfData");
		return pdfData;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ViewBillDTO> getBills(String role, String userName) {
		
		logger.info("Entry point of getBills role={},userName={}",role,userName);
	    BillQuery billQuery = new BillQuery();
	    String dynamicBillQuery = billQuery.getBillsQuery(role, userName);
	    Query query = entityManager.createNativeQuery(dynamicBillQuery);
	    List<Object[]> resultList = query.getResultList();
	    
	    List<ViewBillDTO> bills = new ArrayList<>();
	    for (Object[] result : resultList) {
	        ViewBillDTO dto = new ViewBillDTO(
	            (Integer) result[0], 
	            (String) result[1], 
	            (String) result[2], 
	            (Date) result[3], 
	            (Integer) result[4]
	        );
	        bills.add(dto);
	    }
	    logger.info("Exit point #1 of getBills");
	    return bills;
	}
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer createBill(BillDTO billDTO, Integer userId) {
		
		logger.info("Entry point of createBill billDTO={},userId={}",billDTO,userId);
		BillEntity billEntity = new BillEntity();
		billEntity.setBillingDate(Date.valueOf(LocalDate.now()));
		billEntity.setCreatedBy(userId);
		billEntity.setCustomerName(billDTO.getCustomerName());
		billEntity.setSellerName(billDTO.getSellerName());
		billEntity.setTotal(billDTO.getTotalAmount());
		billEntity.setCreatedDate(Instant.now());
		BillEntity bill = billRepository.save(billEntity);
		
		if(null != bill) {
			Integer billId = bill.getBillId();
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Document document = new Document();
				PdfWriter.getInstance(document, baos);
				document.open();
				document.add(new Paragraph());
				document.add(new Paragraph());
				document.add(new Paragraph("Bill No       : " + billId));
				document.add(new Paragraph("Customer Name : " + billDTO.getCustomerName()));
				document.add(new Paragraph("Seller Name     : " + billDTO.getSellerName()));
				document.add(new Paragraph("Billing Date     : " + Date.valueOf(LocalDate.now())));
				document.add(new Paragraph("Amount           : " + billDTO.getTotalAmount()));
				document.add(new Paragraph());
				document.add(new Paragraph());
				document.add(new Paragraph());
				PdfPTable table = new PdfPTable(6); 
				table.setWidthPercentage(100);
				table.setSpacingBefore(10f);
				table.setSpacingAfter(10f);
		
				PdfPCell[] headerCells = new PdfPCell[]{
				    new PdfPCell(new Phrase("S.No")),
				    new PdfPCell(new Phrase("Book Name")),
				    new PdfPCell(new Phrase("Category")),
				    new PdfPCell(new Phrase("Price")),
				    new PdfPCell(new Phrase("Quantity")),
				    new PdfPCell(new Phrase("Amount"))
				};
		
				for (PdfPCell cell : headerCells) {
				    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				    table.addCell(cell);
				}
				
				List<BookDTO> billBooks = billDTO.getBookDTO();
		
				for (int i = 0; i < billBooks.size(); i++) {
				    BookDTO book = billBooks.get(i);
				    table.addCell(String.valueOf(i + 1));
				    table.addCell(book.getBookName());
				    table.addCell(book.getCategory());
				    table.addCell(String.valueOf(book.getPrice()));
				    table.addCell(String.valueOf(book.getOrderedCopies()));
				    table.addCell(String.valueOf(book.getPrice()*book.getOrderedCopies()));
				    
				    BillDetailsEntity billDetailsEntity = new BillDetailsEntity();
				    billDetailsEntity.setBillId(billId);
				    billDetailsEntity.setBookName(book.getBookName());
				    billDetailsEntity.setCategory(book.getCategory());
				    billDetailsEntity.setPrice(book.getPrice());
				    billDetailsEntity.setOrderedCopies(book.getOrderedCopies());
				    billDetailsEntity.setCreatedBy(userId);
				    billDetailsEntity.setCreatedDate(Instant.now());
				    billDetailsRepository.save(billDetailsEntity);
				    
				    bookRepository.editCopies(book.getBookName(),book.getCategory(), book.getOrderedCopies());
				}
		
				document.add(table);
		
				document.close();
				
				byte[] pdfData = baos.toByteArray();
				
				BillEntity billPdf = new BillEntity();
				billPdf.setPdfData(pdfData);
				
				Integer success = billRepository.savePdf(pdfData, billId);
				if(success >0) {
					logger.info("Exit point #1 of createBill");
					return billId;
				}
				logger.info("Exit point #2 of createBill");			
				return null;
			} catch (DocumentException de) {
				logger.error("Error while retrieving document de={}",de);
				logger.info("Exit point #3 of createBill");
				return null;
			}
		}
		logger.info("Exit point #4 of createBill");
		return null;
	}
}
