package com.store.book.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.store.book.dto.UserInfoDTO;
import com.store.book.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@GetMapping("/")
	public String index(HttpServletRequest request,Model model) {
		
		logger.info("Entry point of Home Page");
		try {
			List<byte[]> images = loginService.getImages();
			
			List<String> base64Images = images.stream()
	                .map((byte[] image) -> Base64.getEncoder().encodeToString(image))
	                .collect(Collectors.toList());
			String books = loginService.getRecentLesson();
			HttpSession session = request.getSession();
	        Integer userId = (Integer) session.getAttribute("userId");
			model.addAttribute("userId",userId);
			model.addAttribute("books",books);
	        model.addAttribute("images", base64Images);
	        
	        logger.info("Exit point #1 of HomePage");
			return "index.html";
		} catch (Exception e) {
			String errorMessage = "Unable to load HomePage";
			model.addAttribute("error", errorMessage);
			logger.error("Error while loading HomePage , exception e={}", e);
			logger.info("Exit point #2 of HomePage");
			
			return "error.html";
		}
	}

	@GetMapping("/login")
	public String form() {
		
		logger.info("Entry point of Login Page");
		return "login.html";
	}
	
	@GetMapping("/register")
	public String register() {
		
		logger.info("Entry point of Register Page");
		return "register.html";
	}
	
	@PostMapping("/login")
	public String handleLogin(@RequestParam("username") String name, @RequestParam("passkey") String password,
            HttpServletRequest request, HttpServletResponse response, Model model) {
		
		logger.info("Entry point of post login username={},password={}",name,password);
		try {
			UserInfoDTO userInfo = loginService.checkUserExists(name, password);
			if(null != userInfo && userInfo.getIsUserExists()) {
				HttpSession session = request.getSession();
	            session.setAttribute("userName", userInfo.getUserName());
	            session.setAttribute("role", userInfo.getRoleName());
	            session.setAttribute("userId", userInfo.getUserId());
	            
	            model.addAttribute("name", userInfo.getUserName());
	            model.addAttribute("role", userInfo.getRoleName());
				if(userInfo.getRoleName().equals("Shop Keeper")) {
					logger.info("Exit point #1 of post login");
					return "shopkeeper.html";
				} else if (userInfo.getRoleName().equals("Seller")) {
					logger.info("Exit point #2 of post login");
					return "seller.html";
				} else if (userInfo.getRoleName().equals("Customer")) {
					logger.info("Exit point #3 of post login");
					return "customer.html";
				}
			}
			String errorMessage = "Invalid UserName or Password";
			model.addAttribute("error", errorMessage);
			logger.info("Exit point #4 of post login");
			
			return "error.html";
		} catch (Exception e) {
			String errorMessage = "Unable to Login";
			model.addAttribute("error", errorMessage);
			logger.error("Error while post login , exception e={}", e);
			logger.info("Exit point #5 of post login");
			
			return "error.html";
		}
	}
	
	@PostMapping("/register")
	public String handleLogin(@RequestParam("username") String name,
            @RequestParam("passkey") String password,
            @RequestParam("role") String role,
            HttpServletRequest request,
            HttpServletResponse response, Model model) {
		
		logger.info("Entry point of post Register username={}",name);
		try {
			Integer roleId = Integer.parseInt(role);
			if (null != name || null != password) {
				String errorMessage = "Name/Password should not empty";
				model.addAttribute("error", errorMessage);
				logger.info("Exit point #1 of post Register with validation error");
				
				return "error.html";
			}
			Boolean isRegistered = loginService.registerUser(name, password, roleId);
			if(isRegistered) {
				logger.info("Exit point #2 of post Register");
				return "login.html";
			}
			logger.info("Exit point #3 of post Register");
			return "register.html";
		} catch (NumberFormatException pe) {
			String errorMessage = "Role value should be Integer";
			model.addAttribute("error", errorMessage);
			logger.error("Error while loading post Register, exception e={}", pe);
			logger.info("Exit point #4 of post Register");
			
			return "error.html";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		
		logger.info("Entry point of logout");
		HttpSession session=request.getSession();  
        session.invalidate();
        logger.info("Exit point #1 of logout");
        return "login.html";
	}
	
	@PostMapping("/upload")
    public String handleFileUpload(@RequestParam("number") int number,
                                   @RequestPart("image1") MultipartFile image1,
                                   @RequestPart("image2") MultipartFile image2,
                                   @RequestPart("image3") MultipartFile image3,
                                   HttpServletRequest request, Model model) throws IOException {
		
		logger.info("Entry point of upload image");
		try {
			HttpSession session = request.getSession();
	        Integer userId = (Integer) session.getAttribute("userId");
	        MultipartFile[] files = {image1, image2, image3};
	        loginService.uploadImage(files, number, userId);
	        
	        logger.info("Exit point #1 of upload image");
	        return "redirect:/upload";
		} catch(IOException ie) {
			String errorMessage = "Invalid Image format/file";
			model.addAttribute("error", errorMessage);
			logger.error("Error while uploading image, exception e={}", ie);
			logger.info("Exit point #2 of upload image");
			
			return "error.html";
		} catch(Exception e) {
			String errorMessage = "Unable to upload image";
			model.addAttribute("error", errorMessage);
			logger.error("Error while uploading image, exception e={}", e);
			logger.info("Exit point #3 of upload image");
			
			return "error.html";
		}
            
	}
	
	@GetMapping("/upload")
	public String uploadImage(HttpServletRequest request, Model model) {
		
		logger.info("Entry point of get upload image");
		HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        String userName = (String) session.getAttribute("userName");
        model.addAttribute("name", userName);
        model.addAttribute("role", role);
        
        logger.info("Exit point #1 of get upload image");
		return "upload.html";
	}
}