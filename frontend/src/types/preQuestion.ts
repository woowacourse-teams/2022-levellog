import { ParticipantType } from 'types/team';

export interface PreQuestionCustomHookType {
  teamId: string;
  levellogId: string;
  preQuestionId: string;
  preQuestion: string;
}

export interface PreQuestionApiType {
  accessToken: string | null;
  levellogId: string;
  preQuestionId: string;
  preQuestion: string;
}

export interface PreQuestionFormatType {
  preQuestion: string;
}
export interface PreQuestionParticipantType {
  participant: ParticipantType;
}
