import axios from 'axios';

import { API_URL } from 'constants/constants';

export const requestPostFeedback = ({ accessToken, feedbackResult, levellogId }: any) => {
  return axios({
    method: 'post',
    url: `${API_URL}/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { ...feedbackResult },
  });
};

export const requestGetFeedbacksInTeam = ({ accessToken, levellogId }: any) => {
  return axios({
    method: 'get',
    url: `${API_URL}/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestEditFeedback = ({
  accessToken,
  levellogId,
  feedbackId,
  feedbackResult,
}: any) => {
  return axios({
    method: 'put',
    url: `${API_URL}/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { ...feedbackResult },
  });
};

export const requestDeleteFeedback = ({ accessToken, levellogId, feedbackId }: any) => {
  return axios({
    method: 'delete',
    url: `${API_URL}/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
