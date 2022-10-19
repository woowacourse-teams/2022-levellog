export interface QuestionType {
  id: string;
  content: string;
}

export interface QuestionsInLevellogType {
  author: {
    id: string;
    nickname: string;
    profileUrl: string;
  };
  contents: QuestionType[];
}

export interface QuestionSearchKeywordType {
  keyword: string | null;
}

export interface SearchedQuestionType extends QuestionType {
  like: boolean;
  likeCount: number;
}
export type QuestionSort = 'likes' | 'latest' | 'oldest';
