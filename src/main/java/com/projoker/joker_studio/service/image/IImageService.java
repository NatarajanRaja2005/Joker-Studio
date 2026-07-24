package com.projoker.joker_studio.service.image;

import com.projoker.joker_studio.dto.ImageDto;
import com.projoker.joker_studio.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IImageService {
    List<ImageDto> addImage(List<MultipartFile> file, Long productId) throws IOException, SQLException;
    void deleteImageById(Long id);
    ImageDto updateImage(MultipartFile file,Long id);
    ImageDto getImageById(Long id);
    ImageDto imageToimageDto(Image image);
    List<ImageDto> getImageByProductId(Long productId);
}
