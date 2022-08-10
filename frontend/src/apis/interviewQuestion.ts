import axios, { AxiosPromise } from 'axios';

import { InterviewQuestionApiType, InterviewQuestionType } from 'types/interviewQuestion';

export const requestGetInterviewQuestion = ({
  accessToken,
  levellogId,
}: Pick<InterviewQuestionApiType, 'accessToken' | 'levellogId'>): AxiosPromise<
  Record<'interviewQuestions', InterviewQuestionType[]>
> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/levellogs/${levellogId}/interview-questions`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestPostInterviewQuestion = ({
  accessToken,
  levellogId,
  interviewQuestion,
}: Omit<InterviewQuestionApiType, 'interviewQuestionId'>): AxiosPromise<void> => {
  return axios({
    method: 'post',
    url: `${process.env.API_URI}/levellogs/${levellogId}/interview-questions`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { interviewQuestion },
  });
};

export const requestDeleteInterviewQuestion = ({
  accessToken,
  levellogId,
  interviewQuestionId,
}: Omit<InterviewQuestionApiType, 'interviewQuestion'>): AxiosPromise<void> => {
  return axios({
    method: 'delete',
    url: `${process.env.API_URI}/levellogs/${levellogId}/interview-questions/${interviewQuestionId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestEditInterviewQuestion = ({
  accessToken,
  levellogId,
  interviewQuestionId,
  interviewQuestion,
}: InterviewQuestionApiType) => {
  return axios({
    method: 'put',
    url: `${process.env.API_URI}/levellogs/${levellogId}/interview-questions/${interviewQuestionId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { interviewQuestion },
  });
};
