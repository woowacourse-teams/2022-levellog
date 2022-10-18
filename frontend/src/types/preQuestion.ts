import { ParticipantType } from 'types/team';

export interface PreQuestionResultType {
  content: string;
}

export interface PreQuestionFormatType {
  author: {
    id: string;
    nickname: string;
    profileUrl: string;
  };
  content: string;
}
export interface PreQuestionParticipantType {
  participant: ParticipantType;
}
