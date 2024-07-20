package com.store.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

	private Integer userId;
	private String userName;
	private String password;
	private String roleName;
	private Boolean isUserExists;
	
	 public UserInfoDTO(Integer userId, String userName, String password, String roleName) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.roleName = roleName;
        this.isUserExists = true; 
    }
}
