import { UserType } from 'types';

export interface QuestionInfoType {
  id: string;
  content: string;
}

export interface QuestionsInLevellogInfoType {
  author: UserType;
  contents: QuestionInfoType[];
}

export interface QuestionSearchKeywordType {
  keyword: string | null;
}

export interface SearchedQuestionInfoType extends QuestionInfoType {
  like: boolean;
  likeCount: number;
}
export type QuestionSort = 'likes' | 'latest' | 'oldest';
