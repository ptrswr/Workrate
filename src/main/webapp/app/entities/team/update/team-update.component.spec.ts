jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TeamService } from '../service/team.service';
import { ITeam, Team } from '../team.model';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TeamUpdateComponent } from './team-update.component';

describe('Component Tests', () => {
  describe('Team Management Update Component', () => {
    let comp: TeamUpdateComponent;
    let fixture: ComponentFixture<TeamUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let teamService: TeamService;
    let calendarService: CalendarService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeamUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TeamUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      teamService = TestBed.inject(TeamService);
      calendarService = TestBed.inject(CalendarService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call calendar query and add missing value', () => {
        const team: ITeam = { id: 456 };
        const calendar: ICalendar = { id: 65044 };
        team.calendar = calendar;

        const calendarCollection: ICalendar[] = [{ id: 41436 }];
        spyOn(calendarService, 'query').and.returnValue(of(new HttpResponse({ body: calendarCollection })));
        const expectedCollection: ICalendar[] = [calendar, ...calendarCollection];
        spyOn(calendarService, 'addCalendarToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ team });
        comp.ngOnInit();

        expect(calendarService.query).toHaveBeenCalled();
        expect(calendarService.addCalendarToCollectionIfMissing).toHaveBeenCalledWith(calendarCollection, calendar);
        expect(comp.calendarsCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const team: ITeam = { id: 456 };
        const leader: IUser = { id: 'Analyst' };
        team.leader = leader;

        const userCollection: IUser[] = [{ id: 'Gabon Orchestrator' }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [leader];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ team });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const team: ITeam = { id: 456 };
        const calendar: ICalendar = { id: 53669 };
        team.calendar = calendar;
        const leader: IUser = { id: 'bottom-line' };
        team.leader = leader;

        activatedRoute.data = of({ team });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(team));
        expect(comp.calendarsCollection).toContain(calendar);
        expect(comp.usersSharedCollection).toContain(leader);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const team = { id: 123 };
        spyOn(teamService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ team });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: team }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(teamService.update).toHaveBeenCalledWith(team);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const team = new Team();
        spyOn(teamService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ team });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: team }));
        saveSubject.complete();

        // THEN
        expect(teamService.create).toHaveBeenCalledWith(team);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const team = { id: 123 };
        spyOn(teamService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ team });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(teamService.update).toHaveBeenCalledWith(team);
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

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
