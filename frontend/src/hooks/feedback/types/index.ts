import { FeedbackFormatType, FeedbackType } from 'types/feedback';

export interface FeedbackCustomHookType {
  teamId: string | undefined;
  levellogId: string | undefined;
  feedbackId: string | undefined;
  feedbackInfo: FeedbackType;
  feedbackResult: FeedbackFormatType;
}
