import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FullCalendarModule } from '@fullcalendar/angular'; // the main connector. must go first
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import timeGridPlugin from '@fullcalendar/timegrid';
import { IconsModule } from './icons/icons.module';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputSwitchModule } from 'primeng/inputswitch';
import { ColorPickerModule } from 'primeng/colorpicker';
import { HttpClientModule } from '@angular/common/http';
import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { initializer } from '../utils/keycloak-init';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { UserCalendarComponent } from './user-calendar/user-calendar.component';
import { CardComponent } from './card/card.component';
import { CalendarComponent } from './calendar/calendar.component';
import { EventFormComponent } from './event-form/event-form.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { TeamComponent } from './team/team.component';
import { ChartModule } from 'primeng/chart';

FullCalendarModule.registerPlugins([dayGridPlugin, interactionPlugin, timeGridPlugin]);

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    UserCalendarComponent,
    CardComponent,
    CalendarComponent,
    EventFormComponent,
    UserProfileComponent,
    TeamComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FullCalendarModule,
    IconsModule,
    InputTextModule,
    CalendarModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    InputSwitchModule,
    ColorPickerModule,
    HttpClientModule,
    KeycloakAngularModule,
    ChartModule,
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializer,
      deps: [KeycloakService],
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
