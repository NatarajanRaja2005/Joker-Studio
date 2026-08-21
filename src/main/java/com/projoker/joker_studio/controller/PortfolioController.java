package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.model.Portfolio;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.Portfolio.IPortfolio;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final IPortfolio portfolioService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createPortfolio(@RequestParam String eventName,
                                                       @RequestParam String description,
                                                       @RequestParam Long agendaId){
        try {
            Portfolio portfolio=portfolioService.createPortfolio(eventName,description,agendaId);
            return ResponseEntity.ok(new ApiResponse("Portfolio created Successfully!",portfolio));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Portfolio creation failed!",e.getMessage()));
        }
    }

    @PostMapping("/media/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addMediaToPortfolio(@RequestParam Long portfolioId,
                                                           @RequestParam List<MultipartFile> files){
        try {
            Portfolio portfolio=portfolioService.addMediaToPortfolio(portfolioId,files);
            return ResponseEntity.ok(new ApiResponse("Item added to Portfolio Successfully!",portfolio));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Adding media to Portfolio failed!",e.getMessage()));
        }
    }

    @PutMapping("/update/{portfolioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updatePortfolio(@PathVariable Long portfolioId,
                                                       @RequestParam String eventName,
                                                       @RequestParam String description,
                                                       @RequestParam Long agendaId){
        try {
            Portfolio portfolio=portfolioService.updatePortfolio(portfolioId,eventName,description,agendaId);
            return ResponseEntity.ok(new ApiResponse("Portfolio created Successfully!",portfolio));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Portfolio updation failed!",e.getMessage()));
        }
    }

    @PutMapping("/media/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> removeMediaFromPortfolio(@RequestParam Long portfolioId,
                                                                @RequestParam Long portfolioMediaId){
        try {
            Portfolio portfolio=portfolioService.removeMediaFromPortfolio(portfolioId,portfolioMediaId);
            return ResponseEntity.ok(new ApiResponse("Item removed from the Portfolio Successfully!",portfolio));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Item removal failed!",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{portfolioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePortfolio(@PathVariable Long portfolioId){
        try {
            portfolioService.deletePortfolio(portfolioId);
            return ResponseEntity.ok(new ApiResponse("Portfolio Deleted Successfully",null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Portfolio creation failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllPortfolios(){
        try {
            List<Portfolio> portfolios=portfolioService.getAllPortfolio();
            return ResponseEntity.ok(new ApiResponse("Portfolio Retrived Successfully",portfolios));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Portfolio retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/id/{agendaId}")
    public ResponseEntity<ApiResponse> getPortfolioByAgendaId(@PathVariable Long agendaId){
        try {
            Portfolio portfolio=portfolioService.getPortfolioByAgendaId(agendaId);
            return ResponseEntity.ok(new ApiResponse("Portfolio retrived by agendaId",portfolio));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Portfolio retrival failed!",e.getMessage()));
        }
    }

    @PutMapping("/media/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateMediaToPortfolio(@RequestParam Long portfolioId,
                                                              @RequestParam List<MultipartFile> files){
        try {
            Portfolio portfolio=portfolioService.updateMediaToPortfolio(portfolioId,files);
            return ResponseEntity.ok(new ApiResponse("Updation of portfolio media",portfolio));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Item Updation failed!",e.getMessage()));
        }
    }
}
