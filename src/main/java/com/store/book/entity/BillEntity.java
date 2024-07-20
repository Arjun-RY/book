package com.store.book.entity;

import java.sql.Date;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="bill")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer billId;
	private String customerName;
	private String sellerName;
	private Date billingDate;
	private Integer total;
	@Lob
	@Column(name="pdf_data", columnDefinition = "BLOB")
	private byte[] pdfData;
	private Integer createdBy;
	private Instant createdDate;
	
}
