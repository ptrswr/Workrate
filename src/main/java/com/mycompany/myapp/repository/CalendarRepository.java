package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Calendar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Calendar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {}
