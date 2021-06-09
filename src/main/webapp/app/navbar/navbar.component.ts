import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  menuVisible = false;

  constructor(private keycloackService: KeycloakService) {}

  ngOnInit(): void {}

  showSidebar(): void {
    this.menuVisible = !this.menuVisible;
  }

  login() {
    this.keycloackService.logout();
  }
}
