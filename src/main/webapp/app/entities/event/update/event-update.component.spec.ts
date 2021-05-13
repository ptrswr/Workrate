jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EventService } from '../service/event.service';
import { IEvent, Event } from '../event.model';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';

import { EventUpdateComponent } from './event-update.component';

describe('Component Tests', () => {
  describe('Event Management Update Component', () => {
    let comp: EventUpdateComponent;
    let fixture: ComponentFixture<EventUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let eventService: EventService;
    let calendarService: CalendarService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EventUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EventUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      eventService = TestBed.inject(EventService);
      calendarService = TestBed.inject(CalendarService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Calendar query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const calendar: ICalendar = { id: 66411 };
        event.calendar = calendar;

        const calendarCollection: ICalendar[] = [{ id: 38093 }];
        spyOn(calendarService, 'query').and.returnValue(of(new HttpResponse({ body: calendarCollection })));
        const additionalCalendars = [calendar];
        const expectedCollection: ICalendar[] = [...additionalCalendars, ...calendarCollection];
        spyOn(calendarService, 'addCalendarToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(calendarService.query).toHaveBeenCalled();
        expect(calendarService.addCalendarToCollectionIfMissing).toHaveBeenCalledWith(calendarCollection, ...additionalCalendars);
        expect(comp.calendarsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const event: IEvent = { id: 456 };
        const calendar: ICalendar = { id: 82138 };
        event.calendar = calendar;

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(event));
        expect(comp.calendarsSharedCollection).toContain(calendar);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const event = { id: 123 };
        spyOn(eventService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: event }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(eventService.update).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const event = new Event();
        spyOn(eventService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: event }));
        saveSubject.complete();

        // THEN
        expect(eventService.create).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const event = { id: 123 };
        spyOn(eventService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(eventService.update).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCalendarById', () => {
        it('Should return tracked Calendar primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCalendarById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
