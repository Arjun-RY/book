package com.store.book.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import com.store.book.dto.BillDTO;
import com.store.book.dto.BookDTO;
import com.store.book.dto.ViewBillDTO;
import com.store.book.service.BillService;
import com.store.book.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BillController {
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private BookService bookService;
	
	private final Logger logger = LoggerFactory.getLogger(BillController.class);

	@GetMapping("/viewbill/{billId}")
    public ResponseEntity<Resource> getBillPdf(@PathVariable Integer billId) {
		
		logger.info("Entry point of view bill, billId={}",billId);
		try {
	        byte[] pdfData = billService.getPdfData(billId);
	
	        if (pdfData != null && pdfData.length > 0) {
	            ByteArrayResource resource = new ByteArrayResource(pdfData);
	
	            HttpHeaders headers = new HttpHeaders();
	            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=bill.pdf");
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentLength(pdfData.length);
	
	            logger.info("Exit point #1 of view bill");
	            return ResponseEntity.ok().headers(headers).body(resource);
	        } else {
	        	logger.info("Exit point #2 of view bill");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
		} catch(Exception e) {
			logger.info("Exit point #3 of view bill");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
    }
	
	@GetMapping("/downloadbill/{billId}")
    public ResponseEntity<Resource> downloadBillPdf(@PathVariable Integer billId) {
		
		logger.info("Entry point of download bill, billId={}",billId);
		try {
	        byte[] pdfData = billService.getPdfData(billId);
	
	        if (pdfData != null && pdfData.length > 0) {
	            ByteArrayResource resource = new ByteArrayResource(pdfData);
	
	            HttpHeaders headers = new HttpHeaders();
	            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill.pdf");
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentLength(pdfData.length);
	
	            logger.info("Exit point #1 of download bill");
	            return ResponseEntity.ok().headers(headers).body(resource);
	        } else {
	        	logger.info("Exit point #2 of download bill");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
		} catch (Exception e) {
			logger.info("Exit point #3 of download bill");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
    }
	
	@PostMapping("/getprice")
    public ResponseEntity<String> getPriceAndQuantity(@RequestParam("category") String category, 
                                      @RequestParam("book") String book) {
		logger.info("Entry point of get price and quantity, category={},book={}",category,book);
        String prices = bookService.getPrice(category, book);
        logger.info("Exit point #1 of get price and quantity");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(prices.toString());
    }
	
	@GetMapping("/bills")
	public String viewBooks(HttpServletRequest request, Model model) {
		
		logger.info("Entry point of view bill table");
		try {
			HttpSession session = request.getSession();
	    	String role = (String) session.getAttribute("role");
	    	String userName = (String) session.getAttribute("userName");
	        model.addAttribute("name", userName);
	        model.addAttribute("role", role);
	        
	        List<ViewBillDTO> bills = billService.getBills(role, userName);
	        model.addAttribute("bills", bills);
	        logger.info("Exit point #1 of view bill table");
	        return "showbills.html";
		} catch (Exception e) {
			String errorMessage = "Unable to load bills";
			model.addAttribute("error", errorMessage);
			logger.error("Error while viewing books, exception e={}", e);
			logger.info("Exit point #2 of view bill table");
			
			return "error.html";
		}
	}
	
	@GetMapping("/createbill")
	public String createBill(HttpServletRequest request, Model model){
		
		logger.info("Entry point of get bill");
		HttpSession session = request.getSession();
    	String role = (String) session.getAttribute("role");
    	String userName = (String) session.getAttribute("userName");
        model.addAttribute("name", userName);
        model.addAttribute("role", role);
        logger.info("Exit point #1 of get bill");
		return "createbill.html";
	}
	
	@PostMapping("/createnewbill")
    public String createNewBill(@RequestParam("bookNames") String[] bookNames,
                                @RequestParam("categories") String[] categories,
                                @RequestParam("prices") String[] prices,
                                @RequestParam("quantities") String[] quantities,
                                @RequestParam("amounts") String[] amounts,
                                @RequestParam("totalAmount") int totalAmount,
                                @RequestParam("customerName") String customerName, HttpServletRequest request,
                                RedirectAttributes redirectAttributes, Model model) {
		logger.info("Entry point of create new bill");
		try {
			HttpSession session = request.getSession();
	        Integer userId = (Integer) session.getAttribute("userId");
	        String userName = (String) session.getAttribute("userName");
			BillDTO billDTO = new BillDTO();
	        billDTO.setSellerName(userName);
	        billDTO.setCustomerName(customerName);
	        billDTO.setTotalAmount(totalAmount);
	        
	        List<BookDTO> bookDTOList = new ArrayList<>();
	        for (int i = 0; i < bookNames.length; i++) {
	            BookDTO bookDTO = new BookDTO();
	            bookDTO.setBookName(bookNames[i]);
	            bookDTO.setCategory(categories[i]);
	            bookDTO.setPrice(Integer.parseInt(prices[i]));
	            bookDTO.setOrderedCopies(Integer.parseInt(quantities[i]));
	            bookDTOList.add(bookDTO);
	        }
	        billDTO.setBookDTO(bookDTOList);
	        
	        Integer billId = billService.createBill(billDTO, userId);
	        redirectAttributes.addAttribute("billId", billId);
	        logger.info("Exit point #1 of create bill");
	        return "redirect:/viewbill/{billId}";
		} catch (Exception e) {
			String errorMessage = "Unable to create bill";
			model.addAttribute("error", errorMessage);
			logger.error("Error while viewing books, exception e={}", e);
			logger.info("Exit point #2 of create bill");
			
			return "error.html";
		}
	}	
}
