import { UserType } from 'types/index';

export interface FeedbackType {
  feedback: {
    study: string;
    speak: string;
    etc: string;
  };
}

export interface FeedbackInfoType extends FeedbackType {
  id: number;
  updatedAt: string;
  from: UserType;
}
