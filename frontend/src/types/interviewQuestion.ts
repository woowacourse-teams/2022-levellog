export interface InterviewQuestionApiType {
  accessToken: string | null;
  levellogId: string | undefined;
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

export interface InterviewQuestionSearchType {
  keyword: string | null;
}

export interface InterviewQuestionSearchResultType {
  id: number;
  content: string;
  like: boolean;
  likeCount: number;
}
export interface InterviewQuestionSearchApiType {
  results: InterviewQuestionSearchResultType[];
  page: number;
}

export type InterviewQuestionSort = 'likes' | 'latest' | 'oldest';
