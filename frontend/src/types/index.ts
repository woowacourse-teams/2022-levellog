export interface FeedbackType {
  id: number;
  name: string;
  feedback: {
    study: string;
    speak: string;
    etc: string;
  };
}
