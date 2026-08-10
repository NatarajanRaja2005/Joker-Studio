package com.projoker.joker_studio.service.agenda;

import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.model.AgendaDetails;
import com.projoker.joker_studio.repository.AgendaRepository;
import com.projoker.joker_studio.service.agenda_details.AgendaDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//@Service
@RequiredArgsConstructor
public class AgendaService implements IAgendaService{
    private final AgendaRepository agendaRepository;
    private final AgendaDetailsService agendaDetailsService;

    @Override
    public Agenda createAgenda() {
        Agenda agenda=new Agenda();
        agendaRepository.save(agenda);
        return agenda;
    }

    @Override
    public void addAccessories(Long agendaId,Long accessoriesId,int quantity,String event) {
        Agenda agenda=getAgendaById(agendaId);
        AgendaDetails agendaDetails=agendaDetailsService.createAgendaDetails(agendaId,accessoriesId,quantity,event);
        agenda.getAccessoriesSet().add(agendaDetails);
        agendaRepository.save(agenda);
        updateTotalPrice(agendaId);
    }

    @Override
    public void updateTotalPrice(Long agendaId) {
        Agenda agenda=getAgendaById(agendaId);
        BigDecimal price=new BigDecimal(0);
        for(AgendaDetails details: agenda.getAccessoriesSet()){
            price.add(details.getPrice());
        }
        agenda.setTotalPrice(price);
        agendaRepository.save(agenda);
    }

    @Override
    public Agenda getAgendaById(Long agendaId) {
        Optional<Agenda> agenda=agendaRepository.findById(agendaId);
        if(agenda.isEmpty()){
            throw new ItemNotExistException("Invalid agenda id: "+agendaId);
        }
        return agenda.get();
    }

    @Override
    public void deleteAgendaById(Long id) {
        Agenda agenda=getAgendaById(id);
        agendaRepository.delete(agenda);
    }

    @Override
    public List<AgendaDetails> getAllAgendaDetails(Long agendaId){
        getAgendaById(agendaId);
        return agendaDetailsService.getAgendaDetailsByAgendaId(agendaId);
    }

    @Override
    public void updateAgendaDetails(Long agendaId, Long agendaDetailsId, int quantity){
        Agenda agenda=getAgendaById(agendaId);
        agendaDetailsService.updateAgendaDetails(agendaDetailsId,quantity);
    }
}
