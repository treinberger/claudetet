import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { NxTableModule } from '@aposin/ng-aquila/table';
import { CommonModule } from '@angular/common';
import { NxIconModule } from '@aposin/ng-aquila/icon';
import { NxSpinnerModule } from '@aposin/ng-aquila/spinner';
import { NxMessageModule } from '@aposin/ng-aquila/message';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    CommonModule,
    NxTableModule,
    NxIconModule,
    NxSpinnerModule,
    NxMessageModule
  ],
  template: `
    <div class="nx-margin-bottom-m">
      <h1>User Management</h1>
    </div>

    <nx-message *ngIf="error" type="error" class="nx-margin-bottom-m">
      {{ error }}
    </nx-message>

    <nx-spinner *ngIf="loading"></nx-spinner>

    <table *ngIf="!loading && !error" nxTable>
      <thead>
        <tr>
          <th nxTableHeader>ID</th>
          <th nxTableHeader>Email</th>
          <th nxTableHeader>First Name</th>
          <th nxTableHeader>Last Name</th>
          <th nxTableHeader>APoints Balance</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let user of users" nxTableRow>
          <td nxTableCell>{{ user.id }}</td>
          <td nxTableCell>{{ user.email }}</td>
          <td nxTableCell>{{ user.firstname }}</td>
          <td nxTableCell>{{ user.lastname }}</td>
          <td nxTableCell>{{ user.apointsBalance }}</td>
        </tr>
      </tbody>
    </table>
  `,
  styles: [`
    :host {
      display: block;
      padding: 2rem;
    }
  `]
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  loading = true;
  error: string | null = null;

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadUsers();
  }

  private loadUsers() {
    this.loading = true;
    this.error = null;
    
    this.userService.getAllUsers()
      .subscribe({
        next: (users) => {
          this.users = users;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load users. Please try again later.';
          this.loading = false;
          console.error('Error loading users:', err);
        }
      });
  }
}