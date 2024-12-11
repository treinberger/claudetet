import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NxButtonModule } from '@aposin/ng-aquila/button';
import { NxFormfieldModule } from '@aposin/ng-aquila/formfield';
import { NxInputModule } from '@aposin/ng-aquila/input';
import { NxIconModule } from '@aposin/ng-aquila/icon';
import { NxMessageModule } from '@aposin/ng-aquila/message';
import { NxSpinnerModule } from '@aposin/ng-aquila/spinner';
import { NxCardModule } from '@aposin/ng-aquila/card';
import { NxDatefieldModule } from '@aposin/ng-aquila/datefield';
import { NxCheckboxModule } from '@aposin/ng-aquila/checkbox';
import { RaffleService } from '../../services/raffle.service';

@Component({
  selector: 'app-create-raffle',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NxButtonModule,
    NxFormfieldModule,
    NxInputModule,
    NxIconModule,
    NxMessageModule,
    NxSpinnerModule,
    NxCardModule,
    NxDatefieldModule,
    NxCheckboxModule
  ],
  template: `
    <div nxCard>
      <header nxCardHeader>
        <h2>Create New Raffle</h2>
      </header>
      <form [formGroup]="raffleForm" (ngSubmit)="onSubmit()">
        <div nxCardContent>
          <!-- Basic Information -->
          <div class="nx-margin-bottom-m">
            <nx-formfield>
              <label nxLabel>Name</label>
              <input nxInput formControlName="name" required>
            </nx-formfield>
            <nx-message *ngIf="raffleForm.get('name')?.errors?.['required'] && raffleForm.get('name')?.touched" context="error">
              Name is required
            </nx-message>
          </div>

          <div class="nx-margin-bottom-m">
            <nx-formfield>
              <label nxLabel>Description</label>
              <textarea nxInput formControlName="description" required rows="3"></textarea>
            </nx-formfield>
            <nx-message *ngIf="raffleForm.get('description')?.errors?.['required'] && raffleForm.get('description')?.touched" context="error">
              Description is required
            </nx-message>
          </div>

          <!-- Dates -->
          <div class="grid-layout nx-margin-bottom-m">
            <nx-formfield>
              <label nxLabel>Preview Date</label>
              <input nxInput type="datetime-local" formControlName="previewDate" required>
            </nx-formfield>

            <nx-formfield>
              <label nxLabel>Start Date</label>
              <input nxInput type="datetime-local" formControlName="startDate" required>
            </nx-formfield>

            <nx-formfield>
              <label nxLabel>End Date</label>
              <input nxInput type="datetime-local" formControlName="endDate" required>
            </nx-formfield>
          </div>
          <nx-message *ngIf="raffleForm.errors?.['dateOrder']" context="error">
            End date must be after start date, and start date must be after preview date
          </nx-message>

          <!-- Question -->
          <div class="nx-margin-bottom-m">
            <nx-formfield>
              <label nxLabel>Question</label>
              <input nxInput formControlName="question" required>
            </nx-formfield>
            <nx-message *ngIf="raffleForm.get('question')?.errors?.['required'] && raffleForm.get('question')?.touched" context="error">
              Question is required
            </nx-message>
          </div>

          <!-- Answer Options -->
          <div formArrayName="answerOptions" class="nx-margin-bottom-m">
            <h3>Answer Options</h3>
            <nx-message *ngIf="answerOptions.length < 2" context="error">
              At least two answer options are required
            </nx-message>
            <nx-message *ngIf="!hasCorrectAnswer()" context="error">
              At least one answer must be marked as correct
            </nx-message>
            
            <div *ngFor="let option of answerOptions.controls; let i = index" [formGroupName]="i" class="option-row">
              <nx-formfield class="option-input">
                <label nxLabel>Option {{i + 1}}</label>
                <input nxInput formControlName="text">
              </nx-formfield>
              
              <nx-checkbox formControlName="correct">
                Correct Answer
              </nx-checkbox>
              
              <button type="button" nxButton="tertiary" (click)="removeAnswerOption(i)" [disabled]="answerOptions.length <= 2">
                <nx-icon name="trash"></nx-icon>
              </button>
            </div>
            
            <button type="button" nxButton="secondary" (click)="addAnswerOption()" class="nx-margin-top-s">
              Add Option
            </button>
          </div>

          <!-- Prize Tiers -->
          <div formArrayName="prizeTiers" class="nx-margin-bottom-m">
            <h3>Prize Tiers</h3>
            <nx-message *ngIf="prizeTiers.length === 0" context="error">
              At least one prize tier is required
            </nx-message>
            
            <div *ngFor="let tier of prizeTiers.controls; let i = index" [formGroupName]="i" class="prize-tier-row">
              <nx-formfield>
                <label nxLabel>Tier</label>
                <input nxInput type="number" formControlName="tier" required min="0">
              </nx-formfield>
              
              <nx-formfield>
                <label nxLabel>Description</label>
                <input nxInput formControlName="description" required>
              </nx-formfield>
              
              <nx-formfield>
                <label nxLabel>Quantity</label>
                <input nxInput type="number" formControlName="quantity" required min="1">
              </nx-formfield>
              
              <button type="button" nxButton="tertiary" (click)="removePrizeTier(i)">
                <nx-icon name="trash"></nx-icon>
              </button>
            </div>
            
            <button type="button" nxButton="secondary" (click)="addPrizeTier()" class="nx-margin-top-s">
              Add Prize Tier
            </button>
          </div>

          <!-- APoints Config -->
          <div formGroupName="apointsConfig" class="nx-margin-bottom-m">
            <h3>APoints Configuration</h3>
            
            <div class="grid-layout">
              <nx-formfield>
                <label nxLabel>Cost Per Chance</label>
                <input nxInput type="number" formControlName="costPerChance" required min="0">
              </nx-formfield>
              
              <nx-formfield>
                <label nxLabel>Maximum Purchases</label>
                <input nxInput type="number" formControlName="maxPurchases" required min="0">
              </nx-formfield>
            </div>
          </div>
        </div>

        <footer nxCardFooter>
          <div class="button-container">
            <button type="button" nxButton="secondary" (click)="cancel()">Cancel</button>
            <button type="submit" nxButton="primary" [disabled]="!raffleForm.valid || !hasCorrectAnswer() || loading">
              <nx-spinner *ngIf="loading"></nx-spinner>
              Create Raffle
            </button>
          </div>
        </footer>
      </form>
    </div>
  `,
  styles: [`
    .option-row, .prize-tier-row {
      display: flex;
      gap: 1rem;
      align-items: center;
      margin-bottom: 1rem;
    }
    
    .prize-tier-row nx-formfield {
      flex: 1;
    }
    
    .option-input {
      flex: 1;
    }
    
    .grid-layout {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;
    }
    
    .button-container {
      display: flex;
      gap: 1rem;
      justify-content: flex-end;
    }
    
    nx-formfield {
      width: 100%;
    }
  `]
})
export class CreateRaffleComponent implements OnInit {
  raffleForm!: FormGroup;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private raffleService: RaffleService,
    private router: Router
  ) {
    this.createForm();
  }

  ngOnInit(): void {}

  get answerOptions() {
    return this.raffleForm.get('answerOptions') as FormArray;
  }

  get prizeTiers() {
    return this.raffleForm.get('prizeTiers') as FormArray;
  }

  private createForm() {
    this.raffleForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      previewDate: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      question: ['', Validators.required],
      answerOptions: this.fb.array([
        this.createAnswerOptionGroup(),
        this.createAnswerOptionGroup()
      ]),
      prizeTiers: this.fb.array([]),
      apointsConfig: this.fb.group({
        costPerChance: [0, [Validators.required, Validators.min(0)]],
        maxPurchases: [0, [Validators.required, Validators.min(0)]]
      })
    }, { validators: this.dateOrderValidator });

    // Add initial prize tier
    this.addPrizeTier();
  }

  private createAnswerOptionGroup(): FormGroup {
    return this.fb.group({
      text: ['', Validators.required],
      correct: [false]
    });
  }

  dateOrderValidator(group: FormGroup) {
    const preview = group.get('previewDate')?.value;
    const start = group.get('startDate')?.value;
    const end = group.get('endDate')?.value;

    if (preview && start && end) {
      const previewDate = new Date(preview);
      const startDate = new Date(start);
      const endDate = new Date(end);

      if (endDate <= startDate || startDate <= previewDate) {
        return { dateOrder: true };
      }
    }
    return null;
  }

  hasCorrectAnswer(): boolean {
    return this.answerOptions.controls.some(control => control.get('correct')?.value === true);
  }

  addAnswerOption() {
    this.answerOptions.push(this.createAnswerOptionGroup());
  }

  removeAnswerOption(index: number) {
    if (this.answerOptions.length > 2) {
      this.answerOptions.removeAt(index);
    }
  }

  addPrizeTier() {
    const prizeTier = this.fb.group({
      tier: [this.prizeTiers.length, [Validators.required, Validators.min(0)]],
      description: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });

    this.prizeTiers.push(prizeTier);
  }

  removePrizeTier(index: number) {
    this.prizeTiers.removeAt(index);
  }

  cancel() {
    this.router.navigate(['/raffles']);
  }

  onSubmit() {
    if (this.raffleForm.valid && this.hasCorrectAnswer()) {
      this.loading = true;
      
      const formValue = this.raffleForm.value;
      // Convert date strings to ISO format
      formValue.previewDate = new Date(formValue.previewDate).toISOString();
      formValue.startDate = new Date(formValue.startDate).toISOString();
      formValue.endDate = new Date(formValue.endDate).toISOString();

      this.raffleService.createRaffle(formValue).subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/raffles']);
        },
        error: (error) => {
          this.loading = false;
          this.error = error;
        }
      });
    }
  }
}