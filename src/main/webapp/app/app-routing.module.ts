import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TeamComponent } from './team/team.component';
import { UserCalendarComponent } from './user-calendar/user-calendar.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { AuthGuardService } from './services/auth-guard.service';

const routes: Routes = [
  { path: '', component: UserCalendarComponent, canActivate: [AuthGuardService] },
  { path: 'profile', component: UserProfileComponent, canActivate: [AuthGuardService] },
  { path: 'team', component: TeamComponent, canActivate: [AuthGuardService] },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      useHash: true,
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
