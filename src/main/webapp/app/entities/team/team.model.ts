import { ICalendar } from 'app/entities/calendar/calendar.model';
import { IUser } from 'app/entities/user/user.model';

export interface ITeam {
  id?: number;
  name?: string;
  calendar?: ICalendar | null;
  leader?: IUser | null;
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string, public calendar?: ICalendar | null, public leader?: IUser | null) {}
}

export function getTeamIdentifier(team: ITeam): number | undefined {
  return team.id;
}
