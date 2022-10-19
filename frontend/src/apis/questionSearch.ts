import { fetcher } from 'apis';

import { SearchedQuestionInfoType } from '../types/question';

import { AuthorizationHeader } from 'apis/index';
import { QuestionSort } from 'types/question';

export const requestSearchedQuestion = async ({
  keyword,
  accessToken,
  page = 0,
  size = 2000,
  sort = 'latest',
}: QuestionSearchRequestType): Promise<QuestionSearchResponseType> => {
  const searchQuestionGetUri = `/interview-questions?keyword=${keyword}&page=${page}&size=${size}&sort=${sort}`;

  const { data } = await fetcher.get(
    searchQuestionGetUri,
    accessToken ? AuthorizationHeader(accessToken) : {},
  );

  return data;
};

export const requestLikeQuestion = async ({ accessToken, QuestionId }: QuestionLikeRequestType) => {
  const QuestionLikePostUri = `/interview-questions/${QuestionId}/like`;

  await fetcher.post(QuestionLikePostUri, {}, AuthorizationHeader(accessToken));
};

export const requestLikeCancelQuestion = async ({
  accessToken,
  QuestionId,
}: QuestionLikeRequestType) => {
  const QuestionLikeDeleteUri = `/interview-questions/${QuestionId}/like`;

  await fetcher.delete(QuestionLikeDeleteUri, AuthorizationHeader(accessToken));
};

interface QuestionSearchRequestType {
  accessToken: string | null;
  keyword: string | number | null;
  page?: number;
  size?: number;
  sort: QuestionSort;
}

interface QuestionSearchResponseType {
  results: SearchedQuestionInfoType[];
  page: number;
}

export interface QuestionLikeRequestType {
  accessToken: string | null;
  QuestionId: string;
}
