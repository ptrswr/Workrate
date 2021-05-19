package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.repository.EventRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ALL_DAY = false;
    private static final Boolean UPDATED_IS_ALL_DAY = true;

    private static final String DEFAULT_RECURRING_DAY = "AAAAAAAAAA";
    private static final String UPDATED_RECURRING_DAY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .title(DEFAULT_TITLE)
            .start_date(DEFAULT_START_DATE)
            .end_date(DEFAULT_END_DATE)
            .start_time(DEFAULT_START_TIME)
            .end_time(DEFAULT_END_TIME)
            .color(DEFAULT_COLOR)
            .is_all_day(DEFAULT_IS_ALL_DAY)
            .recurring_day(DEFAULT_RECURRING_DAY);
        return event;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event()
            .title(UPDATED_TITLE)
            .start_date(UPDATED_START_DATE)
            .end_date(UPDATED_END_DATE)
            .start_time(UPDATED_START_TIME)
            .end_time(UPDATED_END_TIME)
            .color(UPDATED_COLOR)
            .is_all_day(UPDATED_IS_ALL_DAY)
            .recurring_day(UPDATED_RECURRING_DAY);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();
        // Create the Event
        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEvent.getStart_date()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEvent.getEnd_date()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEvent.getStart_time()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testEvent.getEnd_time()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testEvent.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testEvent.getIs_all_day()).isEqualTo(DEFAULT_IS_ALL_DAY);
        assertThat(testEvent.getRecurring_day()).isEqualTo(DEFAULT_RECURRING_DAY);
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);

        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setTitle(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStart_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setStart_date(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].start_date").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].end_date").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].start_time").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].end_time").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].is_all_day").value(hasItem(DEFAULT_IS_ALL_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].recurring_day").value(hasItem(DEFAULT_RECURRING_DAY)));
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.start_date").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.end_date").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.start_time").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.end_time").value(DEFAULT_END_TIME))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.is_all_day").value(DEFAULT_IS_ALL_DAY.booleanValue()))
            .andExpect(jsonPath("$.recurring_day").value(DEFAULT_RECURRING_DAY));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .title(UPDATED_TITLE)
            .start_date(UPDATED_START_DATE)
            .end_date(UPDATED_END_DATE)
            .start_time(UPDATED_START_TIME)
            .end_time(UPDATED_END_TIME)
            .color(UPDATED_COLOR)
            .is_all_day(UPDATED_IS_ALL_DAY)
            .recurring_day(UPDATED_RECURRING_DAY);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getStart_date()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEvent.getEnd_date()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getStart_time()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEvent.getEnd_time()).isEqualTo(UPDATED_END_TIME);
        assertThat(testEvent.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testEvent.getIs_all_day()).isEqualTo(UPDATED_IS_ALL_DAY);
        assertThat(testEvent.getRecurring_day()).isEqualTo(UPDATED_RECURRING_DAY);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, event.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .title(UPDATED_TITLE)
            .end_date(UPDATED_END_DATE)
            .end_time(UPDATED_END_TIME)
            .color(UPDATED_COLOR)
            .recurring_day(UPDATED_RECURRING_DAY);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getStart_date()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEvent.getEnd_date()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getStart_time()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testEvent.getEnd_time()).isEqualTo(UPDATED_END_TIME);
        assertThat(testEvent.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testEvent.getIs_all_day()).isEqualTo(DEFAULT_IS_ALL_DAY);
        assertThat(testEvent.getRecurring_day()).isEqualTo(UPDATED_RECURRING_DAY);
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .title(UPDATED_TITLE)
            .start_date(UPDATED_START_DATE)
            .end_date(UPDATED_END_DATE)
            .start_time(UPDATED_START_TIME)
            .end_time(UPDATED_END_TIME)
            .color(UPDATED_COLOR)
            .is_all_day(UPDATED_IS_ALL_DAY)
            .recurring_day(UPDATED_RECURRING_DAY);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getStart_date()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEvent.getEnd_date()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getStart_time()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEvent.getEnd_time()).isEqualTo(UPDATED_END_TIME);
        assertThat(testEvent.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testEvent.getIs_all_day()).isEqualTo(UPDATED_IS_ALL_DAY);
        assertThat(testEvent.getRecurring_day()).isEqualTo(UPDATED_RECURRING_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, event.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
