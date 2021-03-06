import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEvent, Event } from '../event.model';
import { EventService } from '../service/event.service';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html',
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;

  calendarsSharedCollection: ICalendar[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    start_date: [null, [Validators.required]],
    end_date: [],
    start_time: [],
    end_time: [],
    color: [],
    is_all_day: [],
    recurring_day: [],
    calendar: [],
  });

  constructor(
    protected eventService: EventService,
    protected calendarService: CalendarService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      if (event.id === undefined) {
        const today = dayjs().startOf('day');
        event.start_date = today;
        event.end_date = today;
      }

      this.updateForm(event);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.createFromForm();
    if (event.id !== undefined) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  trackCalendarById(index: number, item: ICalendar): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(event: IEvent): void {
    this.editForm.patchValue({
      id: event.id,
      title: event.title,
      start_date: event.start_date ? event.start_date.format(DATE_TIME_FORMAT) : null,
      end_date: event.end_date ? event.end_date.format(DATE_TIME_FORMAT) : null,
      start_time: event.start_time,
      end_time: event.end_time,
      color: event.color,
      is_all_day: event.is_all_day,
      recurring_day: event.recurring_day,
      calendar: event.calendar,
    });

    this.calendarsSharedCollection = this.calendarService.addCalendarToCollectionIfMissing(this.calendarsSharedCollection, event.calendar);
  }

  protected loadRelationshipsOptions(): void {
    this.calendarService
      .query()
      .pipe(map((res: HttpResponse<ICalendar[]>) => res.body ?? []))
      .pipe(
        map((calendars: ICalendar[]) =>
          this.calendarService.addCalendarToCollectionIfMissing(calendars, this.editForm.get('calendar')!.value)
        )
      )
      .subscribe((calendars: ICalendar[]) => (this.calendarsSharedCollection = calendars));
  }

  protected createFromForm(): IEvent {
    return {
      ...new Event(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      start_date: this.editForm.get(['start_date'])!.value ? dayjs(this.editForm.get(['start_date'])!.value, DATE_TIME_FORMAT) : undefined,
      end_date: this.editForm.get(['end_date'])!.value ? dayjs(this.editForm.get(['end_date'])!.value, DATE_TIME_FORMAT) : undefined,
      start_time: this.editForm.get(['start_time'])!.value,
      end_time: this.editForm.get(['end_time'])!.value,
      color: this.editForm.get(['color'])!.value,
      is_all_day: this.editForm.get(['is_all_day'])!.value,
      recurring_day: this.editForm.get(['recurring_day'])!.value,
      calendar: this.editForm.get(['calendar'])!.value,
    };
  }
}
