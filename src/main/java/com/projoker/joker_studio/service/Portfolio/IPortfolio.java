package com.projoker.joker_studio.service.Portfolio;

import com.projoker.joker_studio.model.Portfolio;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPortfolio {
    Portfolio createPortfolio(String eventName,String description,Long agendaId);
    void deletePortfolio(Long id) throws IOException;

    Portfolio updatePortfolio(Long portfolioId,String eventName,String description,Long agendaId);
    Portfolio removeMediaFromPortfolio(Long portfolioId,Long portfolioMediaId) throws IOException;

    Portfolio addMediaToPortfolio(Long portfolioId, List<MultipartFile> files) throws IOException;
    List<Portfolio> getAllPortfolio();

    Portfolio getPortfolioById(Long id);
    Portfolio getPortfolioByAgendaId(Long agendaId);

    Portfolio updateMediaToPortfolio(Long portfolioId, List<MultipartFile> files) throws IOException;
}
