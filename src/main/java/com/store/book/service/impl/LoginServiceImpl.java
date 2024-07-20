package com.store.book.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.store.book.dto.UserInfoDTO;
import com.store.book.entity.BookEntity;
import com.store.book.entity.ImageEntity;
import com.store.book.entity.UserEntity;
import com.store.book.repository.BookRepository;
import com.store.book.repository.ImageRepository;
import com.store.book.repository.UserRepository;
import com.store.book.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Override
	public UserInfoDTO checkUserExists(String userName, String password) {
		
		logger.info("Entry point of checkUserExists userName={},password={}",userName,password);
		UserInfoDTO userInfo = userRepository.findByUserName(userName);
		if (null != userInfo && userInfo.getPassword().equals(password)) {
			userInfo.setIsUserExists(true);
		} else if (null != userInfo && !userInfo.getPassword().equals(password)){
			userInfo.setIsUserExists(false);
		}
		logger.info("Exit point #1 of checkUserExists");
		return userInfo;
	}

	@Override
	public Boolean registerUser(String userName, String password, Integer roleId) {
		
		logger.info("Entry point of registerUser userName={},password={},roleId={}",userName,password,roleId);
		UserEntity userEntity = new UserEntity();
		userEntity.setUserName(userName);
		userEntity.setPassword(password);
		userEntity.setRoleId(roleId);
		userEntity.setCreatedDate(Instant.now());
		
		UserEntity registeredUser = userRepository.save(userEntity);
		if (null != registeredUser.getUserId()) {
			logger.info("Exit point #1 of registerUser");
			return true;
		} else {
			logger.info("Exit point #2 of registerUser");
			return false;
		}
	}

	@Override
	public Boolean uploadImage(MultipartFile[] files, Integer number, Integer userId) throws IOException {
		
		logger.info("Entry point of uploadImage number={},userId={}",number,userId);
		for (int i = 1; i <= 3; i++) {
			MultipartFile file = files[i-1];
            InputStream fileContent = file.getInputStream();
            byte[] imageBytes = convertInputStreamToByteArray(fileContent);
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImage(imageBytes);
            imageEntity.setImageId(i);
            imageEntity.setDate(Date.valueOf(LocalDate.now().plusDays(number)));
            imageEntity.setCreatedBy(userId);
            imageEntity.setCreatedDate(Instant.now());
            imageEntity.setLastUpdatedBy(userId);
            imageEntity.setLastUpdatedDate(Instant.now());
            imageRepository.save(imageEntity);
		}
		
		logger.info("Exit point #1 of uploadImage");
		return true;
	}
	
	public byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
		
		logger.info("Entry point of convertInputStreamToByteArray");
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = inputStream.read(buffer)) != -1) {
	        byteArrayOutputStream.write(buffer, 0, length);
	    }
	    
	    logger.info("Exit point #1 of convertInputStreamToByteArray");
	    return byteArrayOutputStream.toByteArray();
	}

	@Override
	public List<byte[]> getImages() {
		
		logger.info("Entry point of getImages");
		List<byte[]> images = imageRepository.getImages();
		logger.info("Exit point #1 of getImages");
		return images;
	}

	@Override
	public String getRecentLesson() {
		
		logger.info("Entry point of getRecentLesson");
		List<BookEntity> books = bookRepository.getRecentBooks();
		String authorBook="";
		for(BookEntity book:books) {
			authorBook = authorBook + " "  + book.getAuthorName() + " - " + book.getBookName()+ "      ";
		}
		logger.info("Exit point #1 of getRecentLesson");
		return authorBook;
	}
}
