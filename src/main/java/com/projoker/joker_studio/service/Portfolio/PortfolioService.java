package com.projoker.joker_studio.service.Portfolio;

import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.model.Portfolio;
import com.projoker.joker_studio.model.PortfolioMedia;
import com.projoker.joker_studio.repository.PortfolioRepository;
import com.projoker.joker_studio.service.agenda.IAgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService implements IPortfolio{
    private final PortfolioRepository portfolioRepository;
    private final IAgendaService agendaService;
    private final IPortfolioMedia portfolioMediaService;

    @Override
    public Portfolio createPortfolio(String eventName, String description, Long agendaId) {
        Portfolio portfolio=new Portfolio();
        Agenda agenda=agendaService.getAgendaById(agendaId);
        portfolio.setEventName(eventName);
        portfolio.setEventDescription(description);
        portfolio.setCreatedDate(LocalDate.now());
        portfolio.setAgenda(agenda);
        return portfolioRepository.save(portfolio);
    }

    @Override
    public void deletePortfolio(Long id) throws IOException {
        Portfolio portfolio=getPortfolioById(id);
        List<PortfolioMedia> media=portfolio.getPortfolioMedia();
        for(PortfolioMedia med:media){
            Path path=Paths.get(
                    "uploads","portfolio",String.valueOf(portfolio.getId()),med.getFileName()
            );
            Files.deleteIfExists(path);
        }
        portfolioRepository.delete(portfolio);
    }

    @Override
    public Portfolio updatePortfolio(Long portfolioId, String eventName, String description, Long agendaId) {
        Portfolio  portfolio=getPortfolioById(portfolioId);
        Agenda agenda=agendaService.getAgendaById(agendaId);
        portfolio.setEventName(eventName);
        portfolio.setEventDescription(description);
        portfolio.setCreatedDate(LocalDate.now());
        portfolio.setAgenda(agenda);

        return portfolioRepository.save(portfolio);
    }

    @Override
    public Portfolio removeMediaFromPortfolio(Long portfolioId, Long portfolioMediaId) throws IOException {
        Portfolio portfolio=getPortfolioById(portfolioId);

        PortfolioMedia portfolioMedia=portfolio.getPortfolioMedia()
                .stream()
                .filter(item->item.getId()==portfolioMediaId)
                .findFirst()
                .orElseThrow(()->new ItemNotExistException("Portfoliomedia not found!"));

        Path path= Paths.get(
                "uploads",
                "portfolio",
                String.valueOf(portfolioId),
                portfolioMedia.getFileName()
        );

        Files.deleteIfExists(path);

        portfolio.getPortfolioMedia().remove(portfolioMedia);
        return portfolioRepository.save(portfolio);
    }

    @Override
    public Portfolio addMediaToPortfolio(Long portfolioId, List<MultipartFile> files) throws IOException {
        Portfolio portfolio=getPortfolioById(portfolioId);

        for(MultipartFile file:files){
            PortfolioMedia media=portfolioMediaService.createPortfolioMedia(portfolio,file);
            portfolio.getPortfolioMedia().add(media);
        }
        return portfolioRepository.save(portfolio);
    }

    @Override
    public List<Portfolio> getAllPortfolio() {
        return portfolioRepository.findAll();
    }

    @Override
    public Portfolio getPortfolioById(Long id) {
        return portfolioRepository.findById(id).orElseThrow(()->new ItemNotExistException("Portfolio not found."));
    }

    @Override
    public Portfolio getPortfolioByAgendaId(Long agendaId) {
        Agenda agenda=agendaService.getAgendaById(agendaId);
        Portfolio portfolio=portfolioRepository.findByAgenda(agenda);
        if(portfolio==null){
            throw new ItemNotExistException("Portfolio not found for this agenda.");
        }
        return portfolio;
    }

    @Override
    public Portfolio updateMediaToPortfolio(Long portfolioId, List<MultipartFile> files) throws IOException {
        Portfolio portfolio=getPortfolioById(portfolioId);

        for(PortfolioMedia media:portfolio.getPortfolioMedia()){
            Path path=Paths.get(
                    "uploads","portfolio",String.valueOf(portfolio.getId()),
                    media.getFileName()
            );
            Files.deleteIfExists(path);
        }
        portfolio.getPortfolioMedia().clear();
        for(MultipartFile file:files){
            PortfolioMedia portfolioMedia=portfolioMediaService.createPortfolioMedia(portfolio,file);
            portfolio.getPortfolioMedia().add(portfolioMedia);
        }
        return portfolioRepository.save(portfolio);
    }

}
