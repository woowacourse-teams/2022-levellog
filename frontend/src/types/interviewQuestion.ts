export interface InterviewQuestionApiType {
  accessToken: string | null;
  levellogId: string;
  interviewQuestionId: string;
  interviewQuestion: string;
}

export interface InterviewQuestionInfoType {
  id: string;
  content: string;
}

export interface InterviewQuestionsInLevellogType {
  author: {
    id: string;
    nickname: string;
    profileUrl: string;
  };
  contents: InterviewQuestionInfoType[];
}
