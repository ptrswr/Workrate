import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeatherModule } from 'angular-feather';
import { Calendar, User, Users, Power, AlignJustify } from 'angular-feather/icons';

const icons = {
  Calendar,
  User,
  Users,
  Power,
  AlignJustify,
};

@NgModule({
  declarations: [],
  imports: [CommonModule, FeatherModule.pick(icons)],
  exports: [FeatherModule],
})
export class IconsModule {}
