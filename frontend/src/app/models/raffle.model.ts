// File: /frontend/src/app/models/raffle.model.ts

export interface Raffle {
  id: number;
  name: string;
  description: string;
  teaserImage: string;
  detailImage: string;
  startDate: Date;
  endDate: Date;
  previewDate: Date;
  question: string;
  answerOptions: string[];
  prizeTiers: PrizeTier[];
  apointsConfig?: ApointsConfig;
  status: RaffleStatus;
}

export interface PrizeTier {
  tier: number;
  description: string;
  quantity: number;
}

export interface ApointsConfig {
  costPerChance: number;
  maxPurchases: number;
}

export enum RaffleStatus {
  PREVIEW = 'PREVIEW',
  ACTIVE = 'ACTIVE',
  ENDED = 'ENDED',
  DRAWN = 'DRAWN'
}