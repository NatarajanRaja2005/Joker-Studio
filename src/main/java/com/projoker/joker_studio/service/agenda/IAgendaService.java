package com.projoker.joker_studio.service.agenda;

import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.model.AgendaDetails;
import com.projoker.joker_studio.model.EventAccessories;

import java.util.List;

public interface IAgendaService {
    Agenda createAgenda();
    void addAccessories(Long agendaId,Long accessoriesId,int quantity,String event);
    void updateTotalPrice(Long agendaId);
    Agenda getAgendaById(Long agendaId);
    void deleteAgendaById(Long id);

    List<AgendaDetails> getAllAgendaDetails(Long agendaId);
    List<Agenda> getAllAgenda();
    void updateAgendaDetails(Long agendaId, Long agendaDetailsId, int quantity);
}
