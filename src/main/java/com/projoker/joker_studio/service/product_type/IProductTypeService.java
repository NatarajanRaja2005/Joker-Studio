package com.projoker.joker_studio.service.product_type;

import com.projoker.joker_studio.dto.ProductDto;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.model.ProductType;

import java.util.List;

public interface IProductTypeService {
    ProductType addProductType(String productType);
    void deleteProductTypeById(Long id);
    ProductType updateProductTypeById(String productType,Long id);
    List<ProductType> getAllProductType();
    ProductType getProductTypeById(Long id);
    ProductType getProductTypeByName(String productName);
    void addProductsToProductType(String name, Product product);
}
