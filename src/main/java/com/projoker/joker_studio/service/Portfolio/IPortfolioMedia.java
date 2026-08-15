package com.projoker.joker_studio.service.Portfolio;

import com.projoker.joker_studio.model.Portfolio;
import com.projoker.joker_studio.model.PortfolioMedia;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPortfolioMedia {
    PortfolioMedia createPortfolioMedia(Portfolio portfolio,MultipartFile file) throws IOException;
    PortfolioMedia getPortfolioMediaById(Long id);
    List<PortfolioMedia> getPortfolioMediaByPortfolioId(Long portfolio);
    void deletePortfolioMedia(Long portfolioMediaId);
    PortfolioMedia updatePortfolioMedia(Long id,MultipartFile file) throws IOException;
}
