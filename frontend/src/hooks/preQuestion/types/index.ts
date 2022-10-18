import { PreQuestionFormatType } from 'types/preQuestion';

export interface PreQuestionCustomHookType {
  teamId: string;
  levellogId: string | undefined;
  preQuestionId: string | undefined;
  preQuestion: PreQuestionFormatType;
  preQuestionContent: string;
}
