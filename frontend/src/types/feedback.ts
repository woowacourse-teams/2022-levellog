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
