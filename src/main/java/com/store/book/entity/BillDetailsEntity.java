package com.store.book.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="bill_details")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer billDetailsId;
	private Integer price;
	private Integer orderedCopies;
	private Integer billId;
	private String bookName;
	private String category;
	private Integer createdBy;
	private Instant createdDate;

}
