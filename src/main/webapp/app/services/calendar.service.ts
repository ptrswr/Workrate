import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CalendarService {
  constructor(private http: HttpClient) {}

  async getAllEvents() {
    return await this.http.get(`${environment.apiUrl}/events`).toPromise();
  }

  async getCalendarForUser() {
    let calendar = JSON.parse(localStorage.getItem('calendar') || '{}');
    return await this.http.get(`${environment.apiUrl}/calendars/all_events/${calendar.id}`).toPromise();
  }

  updateEvent(body: any) {
    return this.http.put(`${environment.apiUrl}/events/${body.id}`, body);
  }

  addEvent(body: any) {
    return this.http.post(`${environment.apiUrl}/events`, body);
  }

  removeEvent(id: string) {
    return this.http.delete(`${environment.apiUrl}/events/${id}`);
  }

  createCalendar(name: string, login: string) {
    return this.http.post(`${environment.apiUrl}/calendars/user/${login}`, { name: name });
  }
}
