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
@Table(name="image")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageId;
	@Lob
	@Column(name="image", columnDefinition = "LONGBLOB")
	private byte[] image;
	private Date date;
	private Integer createdBy;
	private Instant createdDate;
	private Integer lastUpdatedBy;
	private Instant lastUpdatedDate;
}
