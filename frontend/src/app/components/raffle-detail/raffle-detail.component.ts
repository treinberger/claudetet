// File: /frontend/src/app/components/raffle-detail/raffle-detail.component.ts

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RaffleService } from '../../services/raffle.service';
import { Raffle } from '../../models/raffle.model';

@Component({
  selector: 'app-raffle-detail',
  templateUrl: './raffle-detail.component.html',
  styleUrls: ['./raffle-detail.component.scss']
})
export class RaffleDetailComponent implements OnInit {
  raffle: Raffle | null = null;
  loading = false;
  error: string | null = null;
  answerForm: FormGroup;
  chancesForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private raffleService: RaffleService,
    private formBuilder: FormBuilder
  ) {
    this.answerForm = this.formBuilder.group({
      selectedAnswer: ['', Validators.required]
    });

    this.chancesForm = this.formBuilder.group({
      additionalChances: [0, [
        Validators.required,
        Validators.min(0),
        Validators.max(100) // This will be updated when raffle is loaded
      ]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadRaffle(Number(id));
    }
  }

  loadRaffle(id: number): void {
    this.loading = true;
    this.raffleService.getRaffleById(id)
      .subscribe({
        next: (raffle) => {
          this.raffle = raffle;
          if (raffle.apointsConfig) {
            this.chancesForm.get('additionalChances')?.setValidators([
              Validators.required,
              Validators.min(0),
              Validators.max(raffle.apointsConfig.maxPurchases)
            ]);
          }
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load raffle details';
          this.loading = false;
        }
      });
  }

  submitAnswer(): void {
    if (this.answerForm.valid && this.raffle) {
      const answer = this.answerForm.get('selectedAnswer')?.value;
      // TODO: Implement answer submission
      console.log('Submitting answer:', answer);
    }
  }

  purchaseChances(): void {
    if (this.chancesForm.valid && this.raffle) {
      const amount = this.chancesForm.get('additionalChances')?.value;
      // TODO: Implement chance purchase
      console.log('Purchasing chances:', amount);
    }
  }
}