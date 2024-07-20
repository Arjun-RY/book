package com.store.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.store.book.entity.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

	@Query(value="SELECT image FROM image WHERE image_id < 4 AND date >= CURDATE()", nativeQuery = true)
	List<byte[]> getImages();
}
