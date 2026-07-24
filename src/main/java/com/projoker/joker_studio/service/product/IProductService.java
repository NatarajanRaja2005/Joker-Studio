package com.projoker.joker_studio.service.product;

import com.projoker.joker_studio.dto.ProductDto;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.request.AddProductRequest;
import com.projoker.joker_studio.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    void removeProductById(Long productId);
    Product updateProduct(UpdateProductRequest request,Long productId);

    List<Product> getAllProduct();
    List<Product> getProductByName(String productName);
    Product getProductById(Long productId);
    List<Product> getProductByType(String productType);
    List<Product> getProductByNameAndType(String productName,String productType);
    Long countProductByProductType(String productType);

    List<Product> getProductsByType(String productType);

    ProductDto productToProductDto(Product product);

    List<ProductDto> productDtoList(List<Product> product);
}
