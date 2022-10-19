import { fetcher } from 'apis';

import { InterviewQuestionSearchResultType } from './../types/interviewQuestion';

import { AuthorizationHeader } from 'apis/index';
import { InterviewQuestionSort } from 'types/interviewQuestion';

export const requestSearchedInterviewQuestion = async ({
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

export const requestLikeInterviewQuestion = async ({
  accessToken,
  interviewQuestionId,
}: QuestionLikeRequestType) => {
  const QuestionLikePostUri = `/interview-questions/${interviewQuestionId}/like`;

  await fetcher.post(QuestionLikePostUri, {}, AuthorizationHeader(accessToken));
};

export const requestLikeCancelInterviewQuestion = async ({
  accessToken,
  interviewQuestionId,
}: QuestionLikeRequestType) => {
  const QuestionLikeDeleteUri = `/interview-questions/${interviewQuestionId}/like`;

  await fetcher.delete(QuestionLikeDeleteUri, AuthorizationHeader(accessToken));
};

interface QuestionSearchRequestType {
  accessToken: string | null;
  keyword: string | number | null;
  page?: number;
  size?: number;
  sort: InterviewQuestionSort;
}

interface QuestionSearchResponseType {
  results: InterviewQuestionSearchResultType[];
  page: number;
}

interface QuestionLikeRequestType {
  accessToken: string | null;
  interviewQuestionId: string;
}
