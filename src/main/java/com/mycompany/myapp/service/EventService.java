package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.repository.EventRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Event}.
 */
@Service
@Transactional
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Save a event.
     *
     * @param event the entity to save.
     * @return the persisted entity.
     */
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    /**
     * Partially update a event.
     *
     * @param event the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Event> partialUpdate(Event event) {
        log.debug("Request to partially update Event : {}", event);

        return eventRepository
            .findById(event.getId())
            .map(
                existingEvent -> {
                    if (event.getTitle() != null) {
                        existingEvent.setTitle(event.getTitle());
                    }
                    if (event.getStart_date() != null) {
                        existingEvent.setStart_date(event.getStart_date());
                    }
                    if (event.getEnd_date() != null) {
                        existingEvent.setEnd_date(event.getEnd_date());
                    }
                    if (event.getStart_time() != null) {
                        existingEvent.setStart_time(event.getStart_time());
                    }
                    if (event.getEnd_time() != null) {
                        existingEvent.setEnd_time(event.getEnd_time());
                    }
                    if (event.getColor() != null) {
                        existingEvent.setColor(event.getColor());
                    }
                    if (event.getIs_all_day() != null) {
                        existingEvent.setIs_all_day(event.getIs_all_day());
                    }
                    if (event.getRecurring_day() != null) {
                        existingEvent.setRecurring_day(event.getRecurring_day());
                    }

                    return existingEvent;
                }
            )
            .map(eventRepository::save);
    }

    /**
     * Get all the events.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        log.debug("Request to get all Events");
        return eventRepository.findAll();
    }

    /**
     * Get one event by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Event> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id);
    }

    /**
     * Delete the event by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
    }
}
