export interface ICalendar {
  id?: number;
  name?: string;
}

export class Calendar implements ICalendar {
  constructor(public id?: number, public name?: string) {}
}

export function getCalendarIdentifier(calendar: ICalendar): number | undefined {
  return calendar.id;
}
