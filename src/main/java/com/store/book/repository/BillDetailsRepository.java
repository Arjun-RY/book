package com.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.book.entity.BillDetailsEntity;

@Repository
public interface BillDetailsRepository extends JpaRepository<BillDetailsEntity, Integer> {

}
