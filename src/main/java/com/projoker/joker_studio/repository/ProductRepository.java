package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByNameAndHeightAndWidthAndMaterialTypeAndProductType(String name, int height, int width, String materialType, ProductType productType);

    List<Product> findByName(String productName);

    List<Product> findByProductType(ProductType productType);

    List<Product> findByNameAndProductType(String productName, ProductType productType);
}
