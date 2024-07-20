package com.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.book.dto.UserInfoDTO;
import com.store.book.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {	
	
	 @Query("SELECT new com.store.book.dto.UserInfoDTO(u.id, u.userName, u.password, r.roleName) " +
	           "FROM UserEntity u " +
	           "JOIN RoleEntity r ON u.roleId = r.roleId " +
	           "WHERE u.userName = :userName")
	    UserInfoDTO findByUserName(@Param("userName") String userName);
	
}
