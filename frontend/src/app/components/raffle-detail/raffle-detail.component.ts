import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RaffleService } from '../../services/raffle.service';
import { Raffle } from '../../models/raffle.model';
import { NxIconModule } from '@aposin/ng-aquila/icon';
import { NxSpinnerModule } from '@aposin/ng-aquila/spinner';
import { NxMessageModule } from '@aposin/ng-aquila/message';
import { NxCardModule } from '@aposin/ng-aquila/card';
import { NxFormfieldModule } from '@aposin/ng-aquila/formfield';
import { NxDropdownModule } from '@aposin/ng-aquila/dropdown';
import { NxButtonModule } from '@aposin/ng-aquila/button';
import { NxInputModule } from '@aposin/ng-aquila/input';
import { NxTableModule } from '@aposin/ng-aquila/table';

@Component({
  selector: 'app-raffle-detail',
  templateUrl: './raffle-detail.component.html',
  styleUrls: ['./raffle-detail.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NxIconModule,
    NxSpinnerModule,
    NxMessageModule,
    NxCardModule,
    NxFormfieldModule,
    NxDropdownModule,
    NxButtonModule,
    NxInputModule,
    NxTableModule
  ]
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
        Validators.max(100)
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
      console.log('Submitting answer:', answer);
    }
  }

  purchaseChances(): void {
    if (this.chancesForm.valid && this.raffle) {
      const amount = this.chancesForm.get('additionalChances')?.value;
      console.log('Purchasing chances:', amount);
    }
  }
}