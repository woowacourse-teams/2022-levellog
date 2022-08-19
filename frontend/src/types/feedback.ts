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
  feedbackInfo: FeedbackType;
  feedbackResult: FeedbackFormatType;
}

export interface FeedbackType {
  id: number;
  updatedAt: string;
  from: {
    id: string;
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
