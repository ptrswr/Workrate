import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EventsService {
  private messageSource = new Subject();
  messageReceived$ = this.messageSource.asObservable();

  constructor() {}

  pushMessage(message: any) {
    this.messageSource.next(message);
  }
}
