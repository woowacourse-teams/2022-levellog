import axios from 'axios';

export const requestPostFeedback = ({ accessToken, feedbackResult, levellogId }: any) => {
  return axios({
    method: 'post',
    url: `${process.env.API_URI}/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { ...feedbackResult },
  });
};

export const requestGetFeedbacksInTeam = ({ accessToken, levellogId }: any) => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/levellogs/${levellogId}/feedbacks`,
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
    url: `${process.env.API_URI}/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { ...feedbackResult },
  });
};

export const requestDeleteFeedback = ({ accessToken, levellogId, feedbackId }: any) => {
  return axios({
    method: 'delete',
    url: `${process.env.API_URI}/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
