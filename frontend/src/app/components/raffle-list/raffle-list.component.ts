import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RaffleService } from '../../services/raffle.service';
import { Raffle } from '../../models/raffle.model';
import { NxTableModule } from '@aposin/ng-aquila/table';
import { CommonModule } from '@angular/common';
import { NxIconModule } from '@aposin/ng-aquila/icon';
import { NxSpinnerModule } from '@aposin/ng-aquila/spinner';
import { NxMessageModule } from '@aposin/ng-aquila/message';
import { NxButtonModule } from '@aposin/ng-aquila/button';

@Component({
  selector: 'app-raffle-list',
  templateUrl: './raffle-list.component.html',
  styleUrls: ['./raffle-list.component.scss'],
  imports: [
    CommonModule,
    NxTableModule,
    NxIconModule,
    NxSpinnerModule,
    NxMessageModule,
    NxButtonModule
  ],
  standalone: true
})
export class RaffleListComponent implements OnInit {
  raffles: Raffle[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private router: Router,
    private raffleService: RaffleService
  ) {}

  ngOnInit(): void {
    this.loadRaffles();
  }

  loadRaffles(): void {
    this.loading = true;
    this.raffleService.getAllRaffles()
      .subscribe({
        next: (raffles) => {
          this.raffles = raffles;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load raffles';
          this.loading = false;
        }
      });
  }

  viewRaffleDetails(id: number): void {
    this.router.navigate(['/raffles', id]);
  }

  createNewRaffle(): void {
    this.router.navigate(['/raffles/create']);
  }
}