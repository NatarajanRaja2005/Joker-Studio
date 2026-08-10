package com.projoker.joker_studio.service.event_accessories;

import com.projoker.joker_studio.enums.EventName;
import com.projoker.joker_studio.model.EventAccessories;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface IEventAccessoriesService {
    void createAccessories(String name, String description,HashMap<EventName,BigDecimal> prices);

    List<EventAccessories> getAll();

    void updateAccessories(Long id, String name, String description, HashMap<EventName,BigDecimal> prices);
    void deleteEventAccessories(Long id);
    List<EventAccessories> getAccessoriesByName(String name);
    EventAccessories getAccessoriesById(Long id);
}
