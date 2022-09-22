import axios, { AxiosPromise } from 'axios';

import { 엑세스토큰이없는경우헤더제거 } from 'apis/utils';
import { InterviewQuestionApiType, InterviewQuestionSearchApiType } from 'types/interviewQuestion';

type InterviewQuestionSort = 'likes' | 'latest' | 'oldest';

interface InterviewQuestionSearchSort extends InterviewQuestionSearchPage {
  accessToken: string | null;
  sort: InterviewQuestionSort;
}

interface InterviewQuestionSearchPage extends InterviewQuestionSearch {
  page: number;
  size: number;
}
interface InterviewQuestionSearch {
  accessToken: string | null;
  keyword: string | number;
}

export const requestInterviewQuestionSearch = ({
  accessToken,
  keyword,
}: InterviewQuestionSearch): AxiosPromise<InterviewQuestionSearchApiType> => {
  return axios(
    엑세스토큰이없는경우헤더제거({
      accessToken,
      method: 'get',
      url: `${process.env.API_URI}/interview-questions?keyword=${keyword}&sort=latest`,
      headers: { Authorization: `Bearer ${accessToken}` },
    }),
  );
};

export const requestInterviewQuestionSearchPage = ({
  keyword,
  page = 0,
  size = 10,
}: InterviewQuestionSearchPage): AxiosPromise<void> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/interview-questions?keyword=${keyword}&page=${page}&size=${size}`,
  });
};

export const requestInterviewQuestionSearchSort = ({
  accessToken,
  keyword,
  page = 0,
  size = 200,
  sort = 'latest',
}: InterviewQuestionSearchSort): AxiosPromise<void> => {
  return axios(
    엑세스토큰이없는경우헤더제거({
      accessToken,
      method: 'get',
      url: `${process.env.API_URI}/interview-questions?keyword=${keyword}&page=${page}&size=${size}&sort=${sort}`,
      headers: { Authorization: `Bearer ${accessToken}` },
    }),
  );
};

export const requestInterviewQuestionSearchGood = ({
  accessToken,
  interviewQuestionId,
}: Pick<InterviewQuestionApiType, 'accessToken' | 'interviewQuestionId'>): AxiosPromise<void> => {
  return axios({
    method: 'post',
    url: `${process.env.API_URI}/interview-questions/${interviewQuestionId}/like`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestInterviewQuestionSearchCancelGood = ({
  accessToken,
  interviewQuestionId,
}: Pick<InterviewQuestionApiType, 'accessToken' | 'interviewQuestionId'>): AxiosPromise<void> => {
  return axios({
    method: 'delete',
    url: `${process.env.API_URI}/interview-questions/${interviewQuestionId}/like`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
