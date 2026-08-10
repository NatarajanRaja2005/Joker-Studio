package com.projoker.joker_studio.service.event_accessories;

import com.projoker.joker_studio.enums.EventName;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.EventAccessories;
import com.projoker.joker_studio.repository.EventAccessoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventAccessoriesService implements IEventAccessoriesService{

    private final EventAccessoriesRepository eventAccessoriesRepository;

    @Override
    public void createAccessories(String name, String description, HashMap<EventName,BigDecimal> prices) {
        EventAccessories accessories=getAccessoriesByNameForDup(name);
        if(accessories!=null){
            updateAccessories(accessories.getId(),name,description,prices);
            return;
        }
        accessories=new com.projoker.joker_studio.model.EventAccessories();
        accessories.setName(name);
        accessories.setDescription(description);
        accessories.setPrice(prices);
        eventAccessoriesRepository.save(accessories);
    }

    private EventAccessories getAccessoriesByNameForDup(String name) {
        return eventAccessoriesRepository.findByNameIgnoreCase(name);
    }

    @Override
    public List<EventAccessories> getAll(){
        return eventAccessoriesRepository.findAll();
    }


    @Override
    public void updateAccessories(Long id, String name, String description, HashMap<EventName,BigDecimal> prices) {
        com.projoker.joker_studio.model.EventAccessories accessories=getAccessoriesById(id);
        if(accessories==null){
            throw new ItemNotExistException("Accessories not exists. Invalid Id: "+id);
        }
        accessories.setName(name);
        accessories.setDescription(description);
        accessories.setPrice(prices);
        eventAccessoriesRepository.save(accessories);
    }

    @Override
    public void deleteEventAccessories(Long id) {
        EventAccessories accessories=getAccessoriesById(id);
        eventAccessoriesRepository.delete(accessories);
    }

    @Override
    public List<EventAccessories> getAccessoriesByName(String name) {
        List<EventAccessories> list=eventAccessoriesRepository.findByNameContainingIgnoreCase(name);

        return list;
    }

    @Override
    public EventAccessories getAccessoriesById(Long id) {
        Optional<EventAccessories> accessories=eventAccessoriesRepository.findById(id);
        if(accessories==null){
            throw new ItemNotExistException("Accessories not exists. Invalid Id: "+id);
        }
        return accessories.get();
    }
}
