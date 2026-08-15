package com.projoker.joker_studio.service.Portfolio;

import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Portfolio;
import com.projoker.joker_studio.model.PortfolioMedia;
import com.projoker.joker_studio.repository.PortfolioMediaRepository;
import com.projoker.joker_studio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioMediaService implements IPortfolioMedia{
    private final PortfolioMediaRepository portfolioMediaRepository;

    @Override
    public PortfolioMedia createPortfolioMedia(Portfolio portfolio,MultipartFile file) throws IOException {
        PortfolioMedia portfolioMedia=new PortfolioMedia();
        portfolioMedia.setPortfolio(portfolio);
        portfolioMediaRepository.save(portfolioMedia);
        String originalFileName= portfolioMedia.getId()+file.getOriginalFilename();
        //uploads/portfolio/id/pick
        Path uploadPath= Paths.get("uploads",
                "portfolio",
                String.valueOf(portfolio.getId()));

        //Create directories
        Files.createDirectories(uploadPath);

        //Keep this image inside that path
        Path filePath=uploadPath.resolve(originalFileName);

        file.transferTo(filePath);

        //Store meta data
        portfolioMedia.setFileName(portfolioMedia.getId()+file.getOriginalFilename());
        portfolioMedia.setDownloadUrl("/api/v1/portfolio-media/" + portfolioMedia.getId() + "/download");
        portfolioMedia.setFileSize(file.getSize());
        portfolioMedia.setFileType(file.getContentType());
        portfolioMediaRepository.save(portfolioMedia);


        return portfolioMedia;
    }

    @Override
    public PortfolioMedia getPortfolioMediaById(Long id) {
        return portfolioMediaRepository.findById(id).orElseThrow(()-> new ItemNotExistException("Invalid portfolio media id."));
    }

    @Override
    public List<PortfolioMedia> getPortfolioMediaByPortfolioId(Long portfolioId) {
        return portfolioMediaRepository.findAllByPortfolio_id(portfolioId);
    }

    @Override
    public void deletePortfolioMedia(Long portfolioMediaId) {
        PortfolioMedia portfolioMedia=getPortfolioMediaById(portfolioMediaId);
        portfolioMediaRepository.delete(portfolioMedia);
    }

    @Override
    public PortfolioMedia updatePortfolioMedia(Long id, MultipartFile file) throws IOException {
        PortfolioMedia portfolioMedia=getPortfolioMediaById(id);
        String storedFileName=String.valueOf(id)+file.getOriginalFilename();

        Path uploadPath=Paths.get(
          "uploads","portfolio",String.valueOf(portfolioMedia.getPortfolio().getId())
        );



        Files.createDirectories(uploadPath);

        //Checks and deletes the file
        if(portfolioMedia.getFileName()!=null) {
            Path oldFilePath = uploadPath.resolve(
                    portfolioMedia.getFileName()
            );
            Files.deleteIfExists(oldFilePath);
        }

        Path filePath=uploadPath.resolve(storedFileName);
        file.transferTo(filePath);

        portfolioMedia.setFileType(file.getContentType());
        portfolioMedia.setDownloadUrl( "/api/v1/portfolio-media/"
                + portfolioMedia.getId()
                + "/download");
        portfolioMedia.setFileSize(file.getSize());
        portfolioMedia.setFileName(storedFileName);

        return portfolioMediaRepository.save(portfolioMedia);
    }
}
