import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CalendarOptions, FullCalendarComponent } from '@fullcalendar/angular';
import { EventsService } from '../services/events.service';
import { CalendarService } from '../services/calendar.service';
import { UserService } from '../services/user.service';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css'],
})
export class CalendarComponent implements OnInit, OnDestroy {
  subscription?: Subscription;
  newEvent: any;
  calendarId = '';
  static staticCalendarService: any;

  @ViewChild('calendar') calendar?: FullCalendarComponent;
  calendarOptions: CalendarOptions = {
    editable: true,
    selectable: true,
    eventClick: function (info) {
      CalendarComponent.eventOnRemove(info.event);
    },
    eventChange: function (info) {
      CalendarComponent.eventOnChange(info.event);
    },
    initialView: 'dayGridMonth',
    headerToolbar: {
      left: 'prev,next',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay',
    },
  };

  constructor(private eventService: EventsService, private calendarService: CalendarService, private userService: UserService) {
    CalendarComponent.staticCalendarService = calendarService;
    this.userService.getCurrentUser().then((data: any) => {
      localStorage.setItem('user', JSON.stringify(data));
      if (data.calendar == null) {
        this.addCalendar();
      }
      localStorage.setItem('calendar', JSON.stringify(data.calendar));
      this.calendarId = data.calendar.id;
    });
  }

  ngOnInit(): void {
    setTimeout(() => {
      this.fetchEvents();
    }, 500);
    this.subscription = this.eventService.messageReceived$.subscribe(() => {
      this.fetchEvents();
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  fetchEvents() {
    let tempEvents: any = [];
    this.calendarService.getCalendarForUser().then((data: any) => {
      data.forEach((element: any) => {
        tempEvents.push({
          id: element.id,
          title: element.title,
          start: element.start_date,
          allDay: element.is_all_day,
          end: element.end_date,
          backgroundColor: element.color,
          borderColor: element.color,
        });
      });
      this.calendarOptions.events = tempEvents;
    });
  }

  static eventOnChange(event: any) {
    let calendar = JSON.parse(localStorage.getItem('calendar') || '{}');
    CalendarComponent.staticCalendarService
      .updateEvent({
        calendar: calendar,
        color: event.backgroundColor,
        end_date: event.end,
        id: event.id,
        is_all_day: event.allDay,
        start_date: event.start,
        title: event.title,
      })
      .pipe(first())
      .subscribe(
        (data: any) => {
          console.log(data);
        },
        (err: any) => {
          console.log(err);
        }
      );
  }

  static eventOnRemove(event: any) {
    if (confirm('Are u sure you want to remove: ' + event.title)) {
      event.remove();
      this.staticCalendarService
        .removeEvent(event.id)
        .pipe(first())
        .subscribe(
          () => {
            console.log('Event removed');
          },
          (err: any) => {
            console.log(err);
          }
        );
    }
  }

  addCalendar() {
    let user = JSON.parse(localStorage.getItem('user') || '{}');
    this.calendarService
      .createCalendar('Default name', user.login)
      .pipe(first())
      .subscribe(
        () => {
          console.log('Calendar created');
        },
        (err: any) => {
          console.log(err);
        }
      );
  }
}
