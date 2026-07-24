package com.projoker.joker_studio.service.product_type;

import com.projoker.joker_studio.dto.ProductDto;
import com.projoker.joker_studio.exception.AlreadyExistException;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.model.ProductType;
import com.projoker.joker_studio.repository.ProductRepository;
import com.projoker.joker_studio.repository.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductTypeService implements IProductTypeService{
    private final ProductTypeRepository productTypeRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductType addProductType(String productType) {
        ProductType existProductType=productTypeRepository.findByName(productType);
        if(existProductType!=null){
            throw new AlreadyExistException("This Product type is Already Exists!");
        }
        ProductType newProductType=new ProductType();
        newProductType.setName(productType);
        return productTypeRepository.save(newProductType);
    }

    @Override
    public void deleteProductTypeById(Long id) {
        Optional<ProductType> existProductType=productTypeRepository.findById(id);
        if(existProductType.isEmpty()){
            throw new ItemNotExistException("This Product type is not Exists!");
        }
        productTypeRepository.deleteById(id);
    }

    @Override
    public ProductType updateProductTypeById(String productType, Long id) {
        ProductType existProductType=getProductTypeById(id);
        //Here is updating the product type
        existProductType.setName(productType);
        return productTypeRepository.save(existProductType);
    }

    @Override
    public List<ProductType> getAllProductType() {
        List<ProductType> list=productTypeRepository.findAll();
        if(list.isEmpty()){
            throw new ItemNotExistException("This Product type is not Exists!");
        }
        return list;
    }

    @Override
    public ProductType getProductTypeById(Long id) {
        Optional<ProductType> existProductType=productTypeRepository.findById(id);
        if(existProductType.isEmpty()){
            throw new ItemNotExistException("This Product type is not Exists!");
        }
        return existProductType.get();
    }

    @Override
    public ProductType getProductTypeByName(String productName) {
        List<Product> list=productRepository.findByName(productName);
        if(list ==null || list.isEmpty()){
            throw new ItemNotExistException("This Product type is not Exists!");
        }
        return list.get(0).getProductType();
    }

    @Override
    public void addProductsToProductType(String name, Product product) {
        ProductType existProduct=productTypeRepository.findByName(name);

        if(product==null){
            throw new ItemNotExistException("Product Not exists!");
        }
        if(existProduct==null){
            throw new ItemNotExistException("Product Type is Invalid!");
        }
        List<Product> list=existProduct.getProduct();
        list.add(product);
        product.setProductType(existProduct);
        productRepository.save(product);
        productTypeRepository.save(existProduct);
    }
}
