import { ParticipantType } from 'types/team';

export interface PreQuestionType {
  content: string;
}

export interface PreQuestionInfoType extends PreQuestionType {
  author: {
    id: string;
    nickname: string;
    profileUrl: string;
  };
}
export interface PreQuestionParticipantType {
  participant: ParticipantType;
}
