import { ParticipantType } from 'types/team';

export interface PreQuestionCustomHookType {
  teamId: string;
  levellogId: string | undefined;
  preQuestionId: string | undefined;
  preQuestion: PreQuestionFormatType;
  preQuestionContent: string;
}

export interface PreQuestionApiType {
  accessToken: string | null;
  levellogId: string | undefined;
  preQuestionId: string | undefined;
  preQuestion: PreQuestionFormatType;
  preQuestionContent: string;
}

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
