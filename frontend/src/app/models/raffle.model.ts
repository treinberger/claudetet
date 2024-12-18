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
  answerOptions: AnswerOption[];
  prizeTiers: PrizeTier[];
  apointsConfig?: ApointsConfig;
  status: RaffleStatus;
}

export interface AnswerOption {
  text: string;
  isCorrect: boolean;
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