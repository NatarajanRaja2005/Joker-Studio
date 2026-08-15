package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    Portfolio findByAgenda(Agenda agenda);
}
