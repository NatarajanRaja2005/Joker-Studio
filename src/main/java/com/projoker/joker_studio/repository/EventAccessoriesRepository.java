package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.EventAccessories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAccessoriesRepository extends JpaRepository<EventAccessories,Long> {

    List<EventAccessories> findByNameContainingIgnoreCase(String name);

    EventAccessories findByNameIgnoreCase(String name);
}
