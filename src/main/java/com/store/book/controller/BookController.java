package com.store.book.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.store.book.entity.BookEntity;
import com.store.book.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class BookController {

	@Autowired
	private BookService bookService;
	
	private final Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@GetMapping({"/viewbook","/books"})
	public String viewBooks(HttpServletRequest request, Model model) {
		
		logger.info("Entry point of view books");
		try {
			HttpSession session = request.getSession();
	        String role = (String) session.getAttribute("role");
	        String userName = (String) session.getAttribute("userName");
	        model.addAttribute("name", userName);
	        model.addAttribute("role", role);
	        
	        List<BookEntity> books = bookService.viewBooks(role);
	        model.addAttribute("books", books);
	        if(role.equals("Seller") || role.equals("Customer")) {
	        	logger.info("Exit point #1 of view books");
	            return "books.html";
	    	} else if (role.equals("Shop Keeper")) {
	    		logger.info("Exit point #2 of view books");
	    		return "allbooks.html";
	    	}
		} catch (Exception e) {
			String errorMessage = "Unable to load books";
			model.addAttribute("error", errorMessage);
			logger.error("Error while viewing books, exception e={}", e);
			logger.info("Exit point #3 of load books");
			
			return "error.html";
		}
        return null;
	}
	
	@GetMapping("/delete/{bookId}")
	public String deleteBook(@PathVariable("bookId") Integer bookId, HttpServletRequest request, Model model) {
		
		logger.info("Entry point of delete book bookId={}",bookId);
		try {
			HttpSession session = request.getSession();
	        Integer userId = (Integer) session.getAttribute("userId");
	        
	        Boolean isDeleted = bookService.deleteBook(bookId, userId);
	        if(isDeleted) {
	        	logger.info("Exit point #1 of delete book");
	        	return "redirect:/books";
	        }
	        logger.info("Exit point #2 of delete book");
	        return "redirect:/books";
		} catch (Exception e) {
			String errorMessage = "Unable to delete book";
			model.addAttribute("error", errorMessage);
			logger.error("Error while deleting books, exception e={}", e);
			logger.info("Exit point #3 of delete book");
			
			return "error.html";
		}
	}
	
	@PostMapping("/bookoptions")
	public ResponseEntity<String> addBook(@RequestParam("category") String category,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		logger.info("Entry point of book list based on category={}",category);
        String booksAvailable = bookService.getBooks(category);
        
        logger.info("Exit point #1 of book list based on category");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(booksAvailable.toString());
	}
	
	@PostMapping("/editbook")
	public String getEditBook(@RequestParam("bookid") Integer bookId, @RequestParam("bookname") String bookName,
			@RequestParam("authorname") String authorName, @RequestParam("category") String category,
			@RequestParam("price") Integer price,@RequestParam("quantity") Integer quantity, HttpServletRequest request, Model model) {
		
		logger.info("Entry point of edit book bookId={},bookName={},authorName={},category={},price={},quantity={}",bookId,bookName,authorName,
				category,price,quantity);
		try {
			BookEntity bookEntity = new BookEntity();
			bookEntity.setAuthorName(authorName);
			bookEntity.setAvailableCopies(quantity);
			bookEntity.setBookId(bookId);
			bookEntity.setBookName(bookName);
			bookEntity.setCategory(category);
			bookEntity.setPrice(price);
			model.addAttribute("bookValues", bookEntity);
			HttpSession session = request.getSession();
			String role = (String) session.getAttribute("role");
	        String userName = (String) session.getAttribute("userName");
	        model.addAttribute("name", userName);
	        model.addAttribute("role", role);
	        
	        logger.info("Exit point #1 of edit book");
			return "editbooks.html";
		} catch (Exception e) {
			String errorMessage = "Unable to edit book";
			model.addAttribute("error", errorMessage);
			logger.error("Error while editing books, exception e={}", e);
			logger.info("Exit point #2 of edit book");
			
			return "error.html";
		}
	}
	
	@PostMapping("/updatebook")
	public String updateBook(@RequestParam("bookid") Integer bookId,  @RequestParam("bookname") String bookName,
			@RequestParam("authorname") String authorName, @RequestParam("genre") String category,
			@RequestParam("price") Integer price,@RequestParam("quantity") Integer quantity, HttpServletRequest request, Model model) {
		
		logger.info("Entry point of update book bookId={},bookName={},authorName={},category={},price={},quantity={}",bookId,bookName,authorName,
				category,price,quantity);
		try {
			HttpSession session = request.getSession();
	        Integer userId = (Integer) session.getAttribute("userId");
	        BookEntity bookEntity = new BookEntity();
			bookEntity.setAuthorName(authorName);
			bookEntity.setAvailableCopies(quantity);
			bookEntity.setBookId(bookId);
			bookEntity.setBookName(bookName);
			bookEntity.setCategory(category);
			bookEntity.setPrice(price);
	        Boolean isEdited = bookService.editBook(bookEntity, userId);
	        if(isEdited) {
	        	logger.info("Exit point #1 of update book");
	        	return "redirect:/books";
	        }
	        logger.info("Exit point #2 of update book");
	        return "redirect:/books";
		} catch (Exception e) {
			String errorMessage = "Unable to edit book";
			model.addAttribute("error", errorMessage);
			logger.error("Error while editing books, exception e={}", e);
			logger.info("Exit point #3 of edit book");
			
			return "error.html";
		}
	}
	
	@GetMapping("/addbook")
	public String addbook(HttpServletRequest request, Model model) {
		
		logger.info("Entry point of add book");
		HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        String userName = (String) session.getAttribute("userName");
        model.addAttribute("name", userName);
        model.addAttribute("role", role);
        logger.info("Exit point #1 of add book");
		return "addbook.html";
	}
	
	@PostMapping("/addbooks")
	public String addBook( @RequestParam("bookname") String bookName,
			@RequestParam("authorname") String authorName, @RequestParam("genre") String category,
			@RequestParam("price") Integer price,@RequestParam("quantity") Integer quantity, HttpServletRequest request, Model model) {
		
		logger.info("Entry point of add book bookName={},authorName={},category={},price={},quantity={}",bookName,authorName,category,price,quantity);
		try {	
			HttpSession session = request.getSession();
	        Integer userId = (Integer) session.getAttribute("userId");
	        BookEntity bookEntity = new BookEntity();
			bookEntity.setAuthorName(authorName);
			bookEntity.setAvailableCopies(quantity);
			bookEntity.setBookName(bookName);
			bookEntity.setCategory(category);
			bookEntity.setPrice(price);
	        Boolean isAdded = bookService.addBook(bookEntity, userId);
	        if(isAdded) {
	        	logger.info("Exit point #1 of add books");
	        	return "redirect:/books";
	        }
	        logger.info("Exit point #2 of add books");
	        return "redirect:/books";
		} catch (Exception e) {
			String errorMessage = "Unable to add book";
			model.addAttribute("error", errorMessage);
			logger.error("Error while editing books, exception e={}", e);
			logger.info("Exit point #3 of edit book");
			
			return "error.html";
		}
	}
	
}
