import axios, { AxiosPromise } from 'axios';

import { PreQuestionApiType } from 'types/preQuestion';

export const requestPostPreQuestion = ({
  accessToken,
  levellogId,
  preQuestion,
}: PreQuestionApiType): AxiosPromise<void> => {
  return axios({
    method: 'post',
    url: `${process.env.API_URI}/levellogs/${levellogId}/pre-questions`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { preQuestion },
  });
};
