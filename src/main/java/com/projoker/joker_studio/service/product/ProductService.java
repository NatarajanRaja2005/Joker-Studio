package com.projoker.joker_studio.service.product;

import com.projoker.joker_studio.dto.ImageDto;
import com.projoker.joker_studio.dto.ProductDto;
import com.projoker.joker_studio.exception.AlreadyExistException;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Image;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.model.ProductType;
import com.projoker.joker_studio.repository.ImageRepository;
import com.projoker.joker_studio.repository.ProductRepository;
import com.projoker.joker_studio.repository.ProductTypeRepository;
import com.projoker.joker_studio.request.AddProductRequest;
import com.projoker.joker_studio.request.UpdateProductRequest;
import com.projoker.joker_studio.service.image.IImageService;
import com.projoker.joker_studio.service.product_type.IProductTypeService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final IProductTypeService productTypeService;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Product addProduct(AddProductRequest request) {
        ProductType productType=productTypeRepository.findByName(request.getProductType().getName());
        //Here creating new product type If it not exists
        if(productType==null){
            productType=productTypeService.addProductType(request.getProductType().getName());
        }
        request.setProductType(productType);
        Product existProduct=productRepository.findByNameAndHeightAndWidthAndMaterialTypeAndProductType
                (request.getName(),request.getHeight(),request.getWidth(),request.getMaterialType(),productType);
        if(existProduct!=null){
            throw new AlreadyExistException("The product is Already exists. You may Update this one.");
        }


        Product newProduct = getProduct(request);
        productTypeService.addProductsToProductType(request.getProductType().getName(),newProduct);
        return newProduct;
    }

    private @NonNull Product getProduct(AddProductRequest request) {

        Product newProduct=new Product();
        newProduct.setName(request.getName());
        newProduct.setDescription(request.getDescription());
        newProduct.setInventory(request.getInventory());
        newProduct.setProductType(request.getProductType());
        newProduct.setPrice(request.getPrice());
        newProduct.setHeight(request.getHeight());
        newProduct.setWidth(request.getWidth());
        newProduct.setMaterialType(request.getMaterialType());
        return newProduct;
    }

    @Override
    public void removeProductById(Long productId) {
        Product exitProduct=getProductById(productId);
        if(exitProduct==null){
            throw new ItemNotExistException("Product Not Exist Exception!");
        }
        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        ProductType productType=productTypeRepository.findByName(request.getProductType().getName());
        //Here creating new product type If it not exists
        if(productType==null){
            productType=productTypeService.addProductType(request.getProductType().getName());
        }
        request.setProductType(productType);

        Product exitProduct=getProductById(productId);
        if(exitProduct==null){
            throw new ItemNotExistException("Product Not Exist Exception!");
        }
        //Here updating the product
        exitProduct.setName(request.getName());
        exitProduct.setDescription(request.getDescription());
        exitProduct.setInventory(request.getInventory());
        exitProduct.setProductType(request.getProductType());
        exitProduct.setPrice(request.getPrice());
        exitProduct.setHeight(request.getHeight());
        exitProduct.setWidth(request.getWidth());
        exitProduct.setMaterialType(request.getMaterialType());

        productRepository.save(exitProduct);
        return exitProduct;
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> list=productRepository.findAll();
        if(list.isEmpty()){
            throw new ItemNotExistException("No products are Present");
        }
        return list;
    }

    @Override
    public List<Product> getProductByName(String productName) {
        List<Product> list=productRepository.findByName(productName);
        if(list.isEmpty()){
            throw new ItemNotExistException("No products are Present");
        }
        return list;
    }

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> product=productRepository.findById(productId);
        if(product.isEmpty()){
            throw new ItemNotExistException("Product Not Found!");
        }
        return product.get();
    }

    @Override
    public List<Product> getProductByType(String productType) {
        ProductType productType1=productTypeRepository.findByName(productType);
        if(productType1==null){
            throw new ItemNotExistException("Product Type is Invalid");
        }
        List<Product> list=productRepository.findByProductType(productType1);
        if(list.isEmpty()){
            throw new ItemNotExistException("Product was not found!");
        }
        return list;
    }

    @Override
    public List<Product> getProductByNameAndType(String productName, String Type) {
        List<Product> list= null;
        try {
            ProductType productType=productTypeRepository.findByName(Type);
            list = productRepository.findByNameAndProductType(productName,productType);
            if(list.isEmpty()){
                throw new ItemNotExistException("Product was not found!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Long countProductByProductType(String productType) {
        ProductType productType1=productTypeRepository.findByName(productType);
        if(productType1==null){
            throw new ItemNotExistException("Product Type is Invalid");
        }
        return productRepository.findByProductType(productType1).stream().count();
    }

    @Override
    public List<Product> getProductsByType(String productType){
        ProductType productType1=productTypeRepository.findByName(productType);
        if(productType1==null){
            throw new ItemNotExistException("Product Type is Invalid");
        }
        List<Product> product=productRepository.findByProductType(productType1);
        return product;
    }

    @Override
    public ProductDto productToProductDto(Product product) {
        ProductDto productDto=modelMapper.map(product,ProductDto.class);
        List<Image> images=imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDto=images.stream()
                        .map(image->modelMapper.map(image,ImageDto.class))
                                .toList();
        productDto.setImages(imageDto);
        return productDto;
    }

    @Override
    public List<ProductDto> productDtoList(List<Product> product){
        List<ProductDto> productDto=product.stream()
                .map(product1 -> modelMapper.map(product1,ProductDto.class))
                .toList();
        return productDto;
    }


}
