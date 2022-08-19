import axios, { AxiosPromise } from 'axios';

import { PreQuestionApiType, PreQuestionFormatType } from 'types/preQuestion';

export const requestGetPreQuestion = ({
  accessToken,
  levellogId,
}: Pick<PreQuestionApiType, 'accessToken' | 'levellogId'>): AxiosPromise<PreQuestionFormatType> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/levellogs/${levellogId}/pre-questions/my`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestPostPreQuestion = ({
  accessToken,
  levellogId,
  preQuestionContent,
}: Pick<
  PreQuestionApiType,
  'accessToken' | 'levellogId' | 'preQuestionContent'
>): AxiosPromise<void> => {
  return axios({
    method: 'post',
    url: `${process.env.API_URI}/levellogs/${levellogId}/pre-questions`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: preQuestionContent,
  });
};

export const requestDeletePreQuestion = ({
  accessToken,
  levellogId,
  preQuestionId,
}: Pick<PreQuestionApiType, 'accessToken' | 'levellogId' | 'preQuestionId'>) => {
  return axios({
    method: 'delete',
    url: `${process.env.API_URI}/levellogs/${levellogId}/pre-questions/${preQuestionId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestEditPreQuestion = ({
  accessToken,
  levellogId,
  preQuestionId,
  preQuestionContent,
}: Omit<PreQuestionApiType, 'preQuestion'>) => {
  return axios({
    method: 'put',
    url: `${process.env.API_URI}/levellogs/${levellogId}/pre-questions/${preQuestionId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: preQuestionContent,
  });
};
