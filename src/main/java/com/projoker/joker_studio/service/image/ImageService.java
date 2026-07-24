package com.projoker.joker_studio.service.image;

import com.projoker.joker_studio.dto.ImageDto;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Image;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.repository.ImageRepository;
import com.projoker.joker_studio.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final IProductService productService;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ImageDto> addImage(List<MultipartFile> file, Long productId){
        Product product=productService.getProductById(productId);
        List<ImageDto> listImages=new ArrayList<>();
        for(MultipartFile curFile: file){
            try {
                Image image=new Image();
                image.setFileName(curFile.getOriginalFilename());
                image.setFileType(curFile.getContentType());
                image.setImage(new SerialBlob(curFile.getBytes()));
                image.setProduct(product);

                String downloadUrl="/api/v1/image/get/";
                Image savedImage=imageRepository.save(image);
                downloadUrl+=savedImage.getId().toString();
                System.out.println(downloadUrl);
                savedImage.setDownloadUrl(downloadUrl);
                imageRepository.save(savedImage);

                ImageDto imageDto=imageToimageDto(savedImage);

                listImages.add(imageDto);
            } catch (SQLException | IOException  e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return listImages;
    }

    @Override
    public void deleteImageById(Long id) {
        Optional<Image> image=imageRepository.findById(id);
        if(image.isEmpty()){
            throw new ItemNotExistException("Image is not found!");
        }
        imageRepository.delete(image.get());
    }

    @Override
    public ImageDto updateImage(MultipartFile file, Long id) {
        Image exitsImage=modelMapper.map(getImageById(id),Image.class);
        try {
            exitsImage.setFileName(file.getOriginalFilename());
            exitsImage.setFileType(file.getContentType());
            exitsImage.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(exitsImage);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return imageToimageDto(exitsImage);
    }

    @Override
    public ImageDto getImageById(Long id) {
        Optional<Image> image=imageRepository.findById(id);
        if(image.isPresent()){
           return imageToimageDto(image.get());
        }
        throw new ItemNotExistException("Image is not exists");
    }

    @Override
    public ImageDto imageToimageDto(Image image) {
        ImageDto imageDto=new ImageDto();

        imageDto.setImageName(image.getFileName());
        imageDto.setId(image.getId());
        imageDto.setDownloadUrl(image.getDownloadUrl());

        return imageDto;
    }

    @Override
    public List<ImageDto> getImageByProductId(Long productId) {
        List<Image> images=imageRepository.findByProductId(productId);
        List<ImageDto> imageDto=images.stream()
                .map(image -> modelMapper.map(image,ImageDto.class))
                .toList();
        return imageDto;
    }
}
