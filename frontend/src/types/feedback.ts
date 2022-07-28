export interface FeedbackApiType {
  accessToken: string | null;
  levellogId: string;
  feedbackId: string;
  feedbackResult: FeedbackFormatType;
}

export interface FeedbackCustomHookType {
  teamId: string;
  levellogId: string;
  feedbackId: string;
  feedbackInfo: FeedbackInfoType;
  feedbackResult: FeedbackFormatType;
}

export interface FeedbackInfoType {
  id: number;
  updatedAt: string;
  from: {
    id: number;
    nickname: string;
    profileUrl: string;
  };
  feedback: {
    study: string;
    speak: string;
    etc: string;
  };
}

export interface FeedbackFormatType {
  feedback: {
    study: string;
    speak: string;
    etc: string;
  };
}
