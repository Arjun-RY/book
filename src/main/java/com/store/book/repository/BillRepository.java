package com.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.store.book.entity.BillEntity;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Integer> {
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE bill"
			+ " SET pdf_data = :pdfData"
			+ " WHERE  bill_id = :billId ", nativeQuery = true)
	Integer savePdf(@Param("pdfData") byte[] pdfData, @Param("billId") Integer billId);
}
