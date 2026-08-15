package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.Portfolio;
import com.projoker.joker_studio.model.PortfolioMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioMediaRepository extends JpaRepository<PortfolioMedia,Long> {
    List<PortfolioMedia> findAllByPortfolio_id(Long portfolio);
}
