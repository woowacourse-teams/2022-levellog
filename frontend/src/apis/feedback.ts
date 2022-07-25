import axios from 'axios';

import { API_URL } from 'constants/constants';

export const postFeedback = (accessToken: string, { feedbackResult, levellogId }: any) =>
  axios({
    method: 'post',
    url: `${API_URL}/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { ...feedbackResult },
  });

export const getFeedbacks = (accessToken: string, levellogId: string) =>
  axios({
    method: 'get',
    url: `${API_URL}/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });

export const deleteFeedbacks = (accessToken: string, levellogId: string, feedbackId: string) =>
  axios({
    method: 'delete',
    url: `${API_URL}/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
