import { ParticipantType } from 'types/team';

export interface PreQuestionCustomHookType {
  teamId: string;
  levellogId: string;
  preQuestionId: string;
  preQuestion: PreQuestionFormatType;
  preQuestionContent: string;
}

export interface PreQuestionApiType {
  accessToken: string | null;
  levellogId: string;
  preQuestionId: string;
  preQuestion: PreQuestionFormatType;
  preQuestionContent: string;
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
