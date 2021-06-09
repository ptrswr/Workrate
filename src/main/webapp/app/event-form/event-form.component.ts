import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventsService } from '../services/events.service';
import { CalendarService } from '../services/calendar.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.css'],
})
export class EventFormComponent implements OnInit {
  newEvent: any;

  addEventForm!: FormGroup;
  submitted = false;
  loading = false;

  constructor(private formbuilder: FormBuilder, private eventService: EventsService, private calendarService: CalendarService) {}

  ngOnInit(): void {
    this.addEventForm = this.formbuilder.group({
      eventName: ['', Validators.required],
      startDate: ['', Validators.required],
      isAllDay: false,
      backgroundColor: '#1979CE',
      startTime: '',
      endDate: '',
      endTime: '',
    });
  }

  get form() {
    return this.addEventForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.addEventForm.invalid) {
      return;
    }

    let calendar = JSON.parse(localStorage.getItem('calendar') || '{}');
    this.calendarService
      .addEvent({
        calendar: calendar,
        title: this.form.eventName.value,
        start_date: this.form.startDate.value,
        is_all_day: this.form.isAllDay.value,
        color: this.form.backgroundColor.value,
        end_date: this.form.endDate.value,
      })
      .pipe(first())
      .subscribe(
        () => {
          this.eventService.pushMessage('');
        },
        (err: any) => {
          console.log(err);
        }
      );
  }
}
