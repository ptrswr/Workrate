import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  async getCurrentUser() {
    return await this.http.get(`${environment.apiUrl}/account`).toPromise();
  }
}
