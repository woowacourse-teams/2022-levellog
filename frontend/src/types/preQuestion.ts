import { UserType } from 'types/index';
import { ParticipantType } from 'types/index';

export interface PreQuestionType {
  content: string;
}

export interface PreQuestionInfoType extends PreQuestionType {
  author: UserType;
}
export interface PreQuestionParticipantType {
  participant: ParticipantType;
}
