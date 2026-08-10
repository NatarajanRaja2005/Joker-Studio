package com.projoker.joker_studio.service.agenda_details;

import com.projoker.joker_studio.model.AgendaDetails;

import java.util.List;

public interface IAgendaDetailsService {
    AgendaDetails createAgendaDetails(Long agendaId,Long eventAccessoriesId,int quantity,String event);
    AgendaDetails updateAgendaDetails(Long id,int quantity);
    AgendaDetails getAgendaDetailsById(Long id);
    List<AgendaDetails> getAgendaDetailsByAgendaId(Long id);
}
