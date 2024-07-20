package com.store.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="role")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

	@Id
	private Integer roleId;
	private String roleName;
}
