package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.model.Portfolio;
import com.projoker.joker_studio.model.PortfolioMedia;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.Portfolio.IPortfolioMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("${api.prefix}/portfolio-media")
@RequiredArgsConstructor
public class PortfolioFileController {
    private final IPortfolioMedia portfolioMediaService;

    @GetMapping("/{portfolioMediaId}/download")
    public ResponseEntity<Resource> getPortfolioFile(@PathVariable Long portfolioMediaId) throws MalformedURLException {
        PortfolioMedia portfolioMedia=portfolioMediaService.getPortfolioMediaById(portfolioMediaId);
        Portfolio portfolio=portfolioMedia.getPortfolio();
        Path filePath= Paths.get(
                "uploads",
                "portfolio",
                String.valueOf(portfolio.getId()),
                portfolioMedia.getFileName()
        );
        Resource resource=new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File not found");
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(portfolioMedia.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" +portfolioMedia.getFileName() + "\"" )
                .body(resource);
    }
}
