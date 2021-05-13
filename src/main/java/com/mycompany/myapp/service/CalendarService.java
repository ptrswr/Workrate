package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Calendar;
import com.mycompany.myapp.repository.CalendarRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Calendar}.
 */
@Service
@Transactional
public class CalendarService {

    private final Logger log = LoggerFactory.getLogger(CalendarService.class);

    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    /**
     * Save a calendar.
     *
     * @param calendar the entity to save.
     * @return the persisted entity.
     */
    public Calendar save(Calendar calendar) {
        log.debug("Request to save Calendar : {}", calendar);
        return calendarRepository.save(calendar);
    }

    /**
     * Partially update a calendar.
     *
     * @param calendar the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Calendar> partialUpdate(Calendar calendar) {
        log.debug("Request to partially update Calendar : {}", calendar);

        return calendarRepository
            .findById(calendar.getId())
            .map(
                existingCalendar -> {
                    if (calendar.getName() != null) {
                        existingCalendar.setName(calendar.getName());
                    }

                    return existingCalendar;
                }
            )
            .map(calendarRepository::save);
    }

    /**
     * Get all the calendars.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Calendar> findAll() {
        log.debug("Request to get all Calendars");
        return calendarRepository.findAll();
    }

    /**
     * Get one calendar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Calendar> findOne(Long id) {
        log.debug("Request to get Calendar : {}", id);
        return calendarRepository.findById(id);
    }

    /**
     * Delete the calendar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Calendar : {}", id);
        calendarRepository.deleteById(id);
    }
}
