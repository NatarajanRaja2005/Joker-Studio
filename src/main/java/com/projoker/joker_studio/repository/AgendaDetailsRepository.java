package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.model.AgendaDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaDetailsRepository extends JpaRepository<AgendaDetails,Long> {
    List<AgendaDetails> findByAgenda(Agenda agenda);
}
