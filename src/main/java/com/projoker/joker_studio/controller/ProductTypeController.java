package com.projoker.joker_studio.controller;


import com.projoker.joker_studio.dto.ProductDto;
import com.projoker.joker_studio.exception.AlreadyExistException;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.model.ProductType;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.product_type.IProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/producttype")
public class ProductTypeController {
    private final IProductTypeService productTypeService;

    @PostMapping("/add/{productType}")
    public ResponseEntity<ApiResponse> addProductType(@PathVariable String productType){
        try {
            ProductType type=productTypeService.addProductType(productType);
            return ResponseEntity.ok(new ApiResponse("Product Type added Successfully!",type));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Already Exists",productType));
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProductTypeById(@PathVariable Long id){
        try {
            productTypeService.deleteProductTypeById(id);
            return ResponseEntity.ok(new ApiResponse("Product Type deletd Successfully!",id));
        } catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Item not exist with this ID: "+id,null));
        }
    }

    @PutMapping("/update/{productName}/{id}")
    public ResponseEntity<ApiResponse> updateProductTypeById(@PathVariable String productName,@PathVariable Long id){
        try {
            ProductType productType= productTypeService.updateProductTypeById(productName, id);
            return ResponseEntity.ok(new ApiResponse("Product Updated Successfully",productType));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Item not exist with this ID: "+id,null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllProductType(){
        try {
            List<ProductType> list=productTypeService.getAllProductType();
            return ResponseEntity.ok(new ApiResponse("All productTypes are retrived Successfully.",list));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("The product type is empty.",null));
        }
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ApiResponse> getProductTypeById(@PathVariable Long id){
        try {
            ProductType productType= productTypeService.getProductTypeById(id);
            return ResponseEntity.ok(new ApiResponse("Product retrived successfully",productType));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("The product type is not exists with this id: "+id,null));
        }
    }

    @GetMapping("/get/productname/{productName}")
    public ResponseEntity<ApiResponse> getProductTypeByName(@PathVariable String productName){
        try {
            ProductType productType=productTypeService.getProductTypeByName(productName);
            return ResponseEntity.ok(new ApiResponse("ProductType got Successfully.",productType));
        }catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("The product type is not exists with this name: "+productName,null));
        }
    }

}
