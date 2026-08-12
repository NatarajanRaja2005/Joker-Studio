package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.enums.EventStatus;
import com.projoker.joker_studio.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

    Optional<Event> findByIdAndUserId(Long userId, Long eventId);

    List<Event> findAllByUserId(Long userId);

    List<Event> findAllByFromDateBetween(LocalDate startDate, LocalDate endDate);

    List<Event> findAllByFromDateBetweenAndUserId(LocalDate startDate, LocalDate endDate,Long userId);

    List<Event> findAllByEventStatus(EventStatus eventStatus);

    List<Event> findAllByEventStatusAndUser_Id(EventStatus eventStatus, Long userId);

    @Query("""
    SELECT e
    FROM Event e
    WHERE e.fromDate >= CURRENT_DATE
    ORDER BY e.fromDate ASC
    """)
    List<Event> getLatestUpcomingEvents();

    @Query("""
    SELECT e
    FROM Event e
    WHERE e.user.id = :userId
      AND e.fromDate >= CURRENT_DATE
    ORDER BY e.fromDate ASC
    """)
    List<Event> getLastestUpComingEventsForUser(Long userId);
}
