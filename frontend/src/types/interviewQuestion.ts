import { UserType } from 'types';

export interface InterviewQuestionType {
  content: string;
}

export interface InterviewQuestionInfoType extends InterviewQuestionType {
  id: string;
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
