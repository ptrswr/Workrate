import * as dayjs from 'dayjs';
import { ICalendar } from 'app/entities/calendar/calendar.model';

export interface IEvent {
  id?: number;
  title?: string;
  start?: dayjs.Dayjs;
  end?: dayjs.Dayjs | null;
  calendar?: ICalendar | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public title?: string,
    public start?: dayjs.Dayjs,
    public end?: dayjs.Dayjs | null,
    public calendar?: ICalendar | null
  ) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
