package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.dto.ProductDto;
import com.projoker.joker_studio.exception.AlreadyExistException;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.request.AddProductRequest;
import com.projoker.joker_studio.request.UpdateProductRequest;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/product")
public class ProductController {
    private final IProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request){
        try {
            Product product=productService.addProduct(request);
            ProductDto productDto=productService.productToProductDto(product);
            return ResponseEntity.ok(new ApiResponse("Product Added Successfully.",productDto));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Addition of Product Failed: "+e.getMessage(),request));
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse>  removeProductById(@PathVariable Long productId){
        try {
            productService.removeProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product deleted Successfully!",null));
        } catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product Deletion Failed: "+e.getMessage(),productId));
        }
    }


    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request,@PathVariable Long productId){
        try {
            Product product=productService.updateProduct(request,productId);
            ProductDto productDto=productService.productToProductDto(product);
            return ResponseEntity.ok(new ApiResponse("Product updated Successfully!",productDto));
        } catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product Updation Failed: "+e.getMessage(),productId));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllProduct(){
        try {
            List<Product> product=productService.getAllProduct();
            List<ProductDto> productDto=productService.productDtoList(product);
            return ResponseEntity.ok(new ApiResponse("All Products arrived successfully!",productDto));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products are Empty "+e.getMessage(),null));
        }
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<ApiResponse> getproductByName(@PathVariable String name){
        try {
            List<Product> product=productService.getProductByName(name);
            List<ProductDto> productDto=productService.productDtoList(product);

            return ResponseEntity.ok(new ApiResponse("The Products are Arrived Successfully!",productDto));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products Not exists with this Name:  "+name,e.getMessage()));
        }
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try {
            Product product=productService.getProductById(id);
            ProductDto productDto=productService.productToProductDto(product);
            return ResponseEntity.ok(new ApiResponse("Products arrived by its Id.",productDto));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products Not exists with this id:  "+id,e.getMessage()));
        }
    }

    @GetMapping("/get/{productName}/{productType}")
    public ResponseEntity<ApiResponse> getProductByNameAndType(
            @PathVariable String productName,@PathVariable String productType){
        try {
            List<Product> product=productService.getProductByNameAndType(productName,productType);
            List<ProductDto> productDto=productService.productDtoList(product);
            return ResponseEntity.ok(new ApiResponse("Products are arrived by its name and its type.",productDto));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products Not exists with this name and type:  "+productName+" "+productType,e.getMessage()));
        }
    }

    @GetMapping("/get/{productType}")
    public ResponseEntity<ApiResponse> getProductByProductType(@PathVariable String productType){
        try {
            List<Product> product=productService.getProductsByType(productType);
            List<ProductDto> productDto=productService.productDtoList(product);
            return ResponseEntity.ok(new ApiResponse("The products are retrived successfully",productDto));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product Type given is Not exists:  "+productType,e.getMessage()));
        }
    }

    @GetMapping("/get/count/{productType}")
    public ResponseEntity<ApiResponse> getCountByProductType(@PathVariable String productType){
        try {
            Long count=productService.countProductByProductType(productType);
            return ResponseEntity.ok(new ApiResponse("The counts of Products: "+count,count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error: "+e.getMessage(),null));
        }
    }
}
