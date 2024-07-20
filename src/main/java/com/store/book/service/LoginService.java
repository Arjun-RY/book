package com.store.book.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.store.book.dto.UserInfoDTO;

public interface LoginService {

	public UserInfoDTO checkUserExists(String userName, String password);
	
	public Boolean registerUser(String userName, String password, Integer roleId);
	
	public Boolean uploadImage(MultipartFile[] files, Integer number, Integer userId) throws IOException;
	
	public List<byte[]> getImages();

	public String getRecentLesson();
}
