package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType,Long> {
    ProductType findByName(String productType);
}
