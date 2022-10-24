import { fetcher } from 'apis';

import { SearchedInterviewQuestionInfoType } from '../types/interviewQuestion';

import { AuthorizationHeader } from 'apis/index';
import { InterviewQuestionSort } from 'types/interviewQuestion';

export const requestSearchedInterviewQuestion = async ({
  keyword,
  accessToken,
  page = 0,
  size = 2000,
  sort = 'latest',
}: InterviewQuestionSearchRequestType): Promise<InterviewQuestionSearchResponseType> => {
  const searchInterviewQuestionGetUri = `/interview-questions?keyword=${keyword}&page=${page}&size=${size}&sort=${sort}`;

  const { data } = await fetcher.get(
    searchInterviewQuestionGetUri,
    accessToken ? AuthorizationHeader(accessToken) : {},
  );

  return data;
};

export const requestLikeInterviewQuestion = async ({
  accessToken,
  InterviewQuestionId,
}: InterviewQuestionLikeRequestType) => {
  const InterviewQuestionLikePostUri = `/interview-questions/${InterviewQuestionId}/like`;

  await fetcher.post(InterviewQuestionLikePostUri, {}, AuthorizationHeader(accessToken));
};

export const requestLikeCancelInterviewQuestion = async ({
  accessToken,
  InterviewQuestionId,
}: InterviewQuestionLikeRequestType) => {
  const InterviewQuestionLikeDeleteUri = `/interview-questions/${InterviewQuestionId}/like`;

  await fetcher.delete(InterviewQuestionLikeDeleteUri, AuthorizationHeader(accessToken));
};

interface InterviewQuestionSearchRequestType {
  accessToken: string | null;
  keyword: string | number | null;
  page?: number;
  size?: number;
  sort: InterviewQuestionSort;
}

interface InterviewQuestionSearchResponseType {
  results: SearchedInterviewQuestionInfoType[];
  page: number;
}

export interface InterviewQuestionLikeRequestType {
  accessToken: string | null;
  InterviewQuestionId: string;
}
