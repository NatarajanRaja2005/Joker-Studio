package com.projoker.joker_studio.controller;


import com.projoker.joker_studio.dto.ImageDto;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Image;
import com.projoker.joker_studio.repository.ImageRepository;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/image")
public class ImageController {

    private final IImageService imageService;
    private final ImageRepository imageRepository;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> addImage(@RequestParam List<MultipartFile> file,@RequestParam Long productId){
        try {
            List<ImageDto> image=imageService.addImage(file,productId);
            return ResponseEntity.ok(new ApiResponse("Image Uploaded Successfully.",image));
        } catch (SQLException | ItemNotExistException | IOException e ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Image Failed!",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteImageById(@PathVariable Long id){
        try {
            imageService.deleteImageById(id);
            return ResponseEntity.ok(new ApiResponse("Image Deleted Successfully!",null));
        } catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image Not found with this id: "+id,null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateImage(@RequestParam MultipartFile file,@PathVariable Long id){
        try {
            ImageDto imageDto=imageService.updateImage(file,id);
            return ResponseEntity.ok(new ApiResponse("Image Updated Successfully!",imageDto));
        } catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image Not found with this id: "+id,e.getMessage()));
        }
        catch (Exception e ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Image Failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Resource> getImageById(@PathVariable Long id){
        try {
            Optional<Image> image=imageRepository.findById(id);
            Image imageOg=image.get();
            ByteArrayResource resource=new ByteArrayResource(imageOg.getImage().getBytes(1,(int)imageOg.getImage().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageOg.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" +imageOg.getFileName() + "\"" )
                    .body(resource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get/productimages/{productId}")
    public ResponseEntity<ApiResponse> getImagesByProductId(@PathVariable Long productId){
        try {
            List<ImageDto> image=imageService.getImageByProductId(productId);
            return ResponseEntity.ok(new ApiResponse("Images are Retrived Successfully",image));
        } catch (ItemNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image Not found for this product with this id: "+productId,null));
        }
    }
}
