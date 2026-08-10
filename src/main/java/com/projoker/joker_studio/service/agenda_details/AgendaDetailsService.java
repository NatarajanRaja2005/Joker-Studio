package com.projoker.joker_studio.service.agenda_details;

import com.projoker.joker_studio.enums.EventName;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.model.AgendaDetails;
import com.projoker.joker_studio.model.EventAccessories;
import com.projoker.joker_studio.repository.AgendaDetailsRepository;
import com.projoker.joker_studio.repository.AgendaRepository;
import com.projoker.joker_studio.service.event_accessories.IEventAccessoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//@Service
@RequiredArgsConstructor
public class AgendaDetailsService implements IAgendaDetailsService{
    private final AgendaDetailsRepository agendaDetailsRepository;
    private final IEventAccessoriesService eventAccessoriesService;
    private final AgendaRepository agendaRepository;

    @Override
    public AgendaDetails createAgendaDetails(Long agendaId,Long eventAccessoriesId, int quantity,String event) {
        EventAccessories accessories=eventAccessoriesService.getAccessoriesById(eventAccessoriesId);
        if(accessories==null){
            throw new ItemNotExistException("Accessories not found!");
        }
        EventName eventName=EventName.valueOf(event);
        AgendaDetails agendaDetails=new AgendaDetails();
        agendaDetails.setEventAccessories(accessories);
        agendaDetails.setQuantity(quantity);
        agendaDetails.setAgenda(agendaRepository.findById(agendaId).get());
        agendaDetails.setEventName(eventName);
        agendaDetailsRepository.save(agendaDetails);
        updatePriceOfAgendaDetails(agendaDetails.getId(), quantity,agendaDetails.getEventName());

        return agendaDetails;
    }

    @Override
    public AgendaDetails updateAgendaDetails(Long id, int quantity) {
        AgendaDetails agendaDetails=getAgendaDetailsById(id);
        agendaDetails.setQuantity(quantity);
        updatePriceOfAgendaDetails(agendaDetails.getId(), quantity,agendaDetails.getEventName());
        return agendaDetails;
    }

    @Override
    public AgendaDetails getAgendaDetailsById(Long id) {
        Optional<AgendaDetails> details=agendaDetailsRepository.findById(id);
        if(details.isEmpty()){
            throw new ItemNotExistException("Invalid Agenda details id.");
        }
        return details.get();
    }

    @Override
    public List<AgendaDetails> getAgendaDetailsByAgendaId(Long id) {
        Optional<Agenda> agenda=agendaRepository.findById(id);
        if(agenda.isEmpty()){
            throw new ItemNotExistException("Invalid agenda id...");
        }
        return agendaDetailsRepository.findByAgenda(agenda.get());
    }

    private void updatePriceOfAgendaDetails(Long agendaDetailsId,int quantity,EventName eventName){
        AgendaDetails agendaDetails=getAgendaDetailsById(agendaDetailsId);
        EventAccessories accessories=agendaDetails.getEventAccessories();
        agendaDetails.setPrice(BigDecimal.valueOf(quantity).multiply(accessories.getPrice().get(eventName)));
        agendaDetailsRepository.save(agendaDetails);
    }
}
