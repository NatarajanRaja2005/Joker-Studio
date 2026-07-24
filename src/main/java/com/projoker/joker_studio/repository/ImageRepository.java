package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByProductId(Long productId);
}
