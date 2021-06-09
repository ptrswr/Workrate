import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuardService implements CanActivate {
  constructor(private keycloakService: KeycloakService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!this.keycloakService.isLoggedIn()) {
      alert('you are not logged in');
      this.router.navigate(['']);
      return false;
    }

    return true;
  }
}
