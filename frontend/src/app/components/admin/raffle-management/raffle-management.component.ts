// File: /frontend/src/app/components/admin/raffle-management/raffle-management.component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RaffleService } from '../../../services/raffle.service';
import { Raffle } from '../../../models/raffle.model';

@Component({
  selector: 'app-raffle-management',
  templateUrl: './raffle-management.component.html',
  styleUrls: ['./raffle-management.component.scss']
})
export class RaffleManagementComponent implements OnInit {
  raffles: Raffle[] = [];
  loading = false;
  error: string | null = null;

  constructor(
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

  openCreateDialog(): void {
    // TODO: Implement create dialog
    console.log('Opening create dialog');
  }

  editRaffle(raffle: Raffle): void {
    // TODO: Implement edit functionality
    console.log('Editing raffle:', raffle);
  }

  conductDraw(raffleId: number): void {
    this.loading = true;
    this.raffleService.conductDraw(raffleId)
      .subscribe({
        next: () => {
          this.loadRaffles();
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to conduct draw';
          this.loading = false;
        }
      });
  }
}