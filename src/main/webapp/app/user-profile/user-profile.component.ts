import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  chartData: any;
  user: any;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().then((data: any) => {
      this.user = data;
    });
    this.chartData = {
      labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
      datasets: [
        {
          label: 'Your sales over months',
          data: [1523, 3214, 1223, 5467, 3423, 4562, 4001],
          fill: false,
          borderColor: '#42A5F5',
        },
        {
          label: 'Company sales avg. over months',
          data: [2134, 2564, 900, 6734, 2123, 2345, 3012],
          fill: false,
          borderColor: '#FFA726',
        },
      ],
    };
  }
}
