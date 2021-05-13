package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Calendar;
import com.mycompany.myapp.repository.CalendarRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CalendarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CalendarResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/calendars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalendarMockMvc;

    private Calendar calendar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calendar createEntity(EntityManager em) {
        Calendar calendar = new Calendar().name(DEFAULT_NAME);
        return calendar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calendar createUpdatedEntity(EntityManager em) {
        Calendar calendar = new Calendar().name(UPDATED_NAME);
        return calendar;
    }

    @BeforeEach
    public void initTest() {
        calendar = createEntity(em);
    }

    @Test
    @Transactional
    void createCalendar() throws Exception {
        int databaseSizeBeforeCreate = calendarRepository.findAll().size();
        // Create the Calendar
        restCalendarMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isCreated());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeCreate + 1);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCalendarWithExistingId() throws Exception {
        // Create the Calendar with an existing ID
        calendar.setId(1L);

        int databaseSizeBeforeCreate = calendarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarRepository.findAll().size();
        // set the field null
        calendar.setName(null);

        // Create the Calendar, which fails.

        restCalendarMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalendars() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get the calendar
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL_ID, calendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCalendar() throws Exception {
        // Get the calendar
        restCalendarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar
        Calendar updatedCalendar = calendarRepository.findById(calendar.getId()).get();
        // Disconnect from session so that the updates on updatedCalendar are not directly saved in db
        em.detach(updatedCalendar);
        updatedCalendar.name(UPDATED_NAME);

        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCalendar.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCalendar))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendar.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalendarWithPatch() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar using partial update
        Calendar partialUpdatedCalendar = new Calendar();
        partialUpdatedCalendar.setId(calendar.getId());

        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendar))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCalendarWithPatch() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar using partial update
        Calendar partialUpdatedCalendar = new Calendar();
        partialUpdatedCalendar.setId(calendar.getId());

        partialUpdatedCalendar.name(UPDATED_NAME);

        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendar))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calendar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeDelete = calendarRepository.findAll().size();

        // Delete the calendar
        restCalendarMockMvc
            .perform(delete(ENTITY_API_URL_ID, calendar.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
