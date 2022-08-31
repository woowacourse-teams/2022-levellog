import axios, { AxiosPromise } from 'axios';

import { FeedbackApiType, FeedbackType } from 'types/feedback';

export const requestPostFeedback = ({
  accessToken,
  levellogId,
  feedbackResult,
}: Omit<FeedbackApiType, 'feedbackId'>): AxiosPromise<void> => {
  return axios({
    method: 'post',
    url: `${process.env.API_URI}/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { ...feedbackResult },
  });
};

export const requestGetFeedbacksInTeam = ({
  accessToken,
  levellogId,
}: Pick<FeedbackApiType, 'accessToken' | 'levellogId'>): AxiosPromise<
  Record<'feedbacks', FeedbackType[]>
> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestGetFeedback = ({
  accessToken,
  levellogId,
  feedbackId,
}: Omit<FeedbackApiType, 'feedbackResult'>) => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestEditFeedback = ({
  accessToken,
  levellogId,
  feedbackId,
  feedbackResult,
}: FeedbackApiType): AxiosPromise<void> => {
  return axios({
    method: 'put',
    url: `${process.env.API_URI}/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { ...feedbackResult },
  });
};
