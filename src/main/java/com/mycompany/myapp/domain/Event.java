package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant start_date;

    @Column(name = "end_date")
    private Instant end_date;

    @Column(name = "start_time")
    private String start_time;

    @Column(name = "end_time")
    private String end_time;

    @Column(name = "color")
    private String color;

    @Column(name = "is_all_day")
    private Boolean is_all_day;

    @Column(name = "recurring_day")
    private String recurring_day;

    @ManyToOne
    private Calendar calendar;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Event title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getStart_date() {
        return this.start_date;
    }

    public Event start_date(Instant start_date) {
        this.start_date = start_date;
        return this;
    }

    public void setStart_date(Instant start_date) {
        this.start_date = start_date;
    }

    public Instant getEnd_date() {
        return this.end_date;
    }

    public Event end_date(Instant end_date) {
        this.end_date = end_date;
        return this;
    }

    public void setEnd_date(Instant end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return this.start_time;
    }

    public Event start_time(String start_time) {
        this.start_time = start_time;
        return this;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return this.end_time;
    }

    public Event end_time(String end_time) {
        this.end_time = end_time;
        return this;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getColor() {
        return this.color;
    }

    public Event color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIs_all_day() {
        return this.is_all_day;
    }

    public Event is_all_day(Boolean is_all_day) {
        this.is_all_day = is_all_day;
        return this;
    }

    public void setIs_all_day(Boolean is_all_day) {
        this.is_all_day = is_all_day;
    }

    public String getRecurring_day() {
        return this.recurring_day;
    }

    public Event recurring_day(String recurring_day) {
        this.recurring_day = recurring_day;
        return this;
    }

    public void setRecurring_day(String recurring_day) {
        this.recurring_day = recurring_day;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public Event calendar(Calendar calendar) {
        this.setCalendar(calendar);
        return this;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", start_date='" + getStart_date() + "'" +
            ", end_date='" + getEnd_date() + "'" +
            ", start_time='" + getStart_time() + "'" +
            ", end_time='" + getEnd_time() + "'" +
            ", color='" + getColor() + "'" +
            ", is_all_day='" + getIs_all_day() + "'" +
            ", recurring_day='" + getRecurring_day() + "'" +
            "}";
    }
}
