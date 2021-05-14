import * as dayjs from 'dayjs';
import { ICalendar } from 'app/entities/calendar/calendar.model';

export interface IEvent {
  id?: number;
  title?: string;
  start_date?: dayjs.Dayjs;
  end_date?: dayjs.Dayjs | null;
  calendar?: ICalendar | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public title?: string,
    public start_date?: dayjs.Dayjs,
    public end_date?: dayjs.Dayjs | null,
    public calendar?: ICalendar | null
  ) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
