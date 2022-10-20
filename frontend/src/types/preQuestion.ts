import { UserType } from 'types/index';

export interface PreQuestionType {
  content: string;
}

export interface PreQuestionInfoType extends PreQuestionType {
  author: UserType;
}
