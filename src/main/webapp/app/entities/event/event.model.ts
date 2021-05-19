import * as dayjs from 'dayjs';
import { ICalendar } from 'app/entities/calendar/calendar.model';

export interface IEvent {
  id?: number;
  title?: string;
  start_date?: dayjs.Dayjs;
  end_date?: dayjs.Dayjs | null;
  start_time?: string | null;
  end_time?: string | null;
  color?: string | null;
  is_all_day?: boolean | null;
  recurring_day?: string | null;
  calendar?: ICalendar | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public title?: string,
    public start_date?: dayjs.Dayjs,
    public end_date?: dayjs.Dayjs | null,
    public start_time?: string | null,
    public end_time?: string | null,
    public color?: string | null,
    public is_all_day?: boolean | null,
    public recurring_day?: string | null,
    public calendar?: ICalendar | null
  ) {
    this.is_all_day = this.is_all_day ?? false;
  }
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
