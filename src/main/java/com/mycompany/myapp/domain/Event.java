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
            "}";
    }
}
