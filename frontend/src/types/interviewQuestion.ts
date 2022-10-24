import { UserType } from 'types';

export interface InterviewQuestionInfoType {
  id: string;
  content: string;
}

export interface InterviewQuestionsInLevellogInfoType {
  author: UserType;
  contents: InterviewQuestionInfoType[];
}

export interface InterviewQuestionSearchKeywordType {
  keyword: string | null;
}

export interface SearchedInterviewQuestionInfoType extends InterviewQuestionInfoType {
  like: boolean;
  likeCount: number;
}
export type InterviewQuestionSort = 'likes' | 'latest' | 'oldest';
