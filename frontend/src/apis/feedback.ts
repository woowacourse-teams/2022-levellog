import axios from 'axios';
import { FeedbackPostType } from 'types';

import { SERVER_PATH } from 'constants/constants';

export const postFeedback = (accessToken: string, { feedbackResult, levellogId }: any) =>
  axios({
    method: 'post',
    url: `/api/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: { ...feedbackResult },
  });

export const getFeedbacks = (accessToken: string, levellogId: string) =>
  axios({
    method: 'get',
    url: `/api/levellogs/${levellogId}/feedbacks`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });

export const deleteFeedbacks = (accessToken: string, levellogId: string, feedbackId: string) =>
  axios({
    method: 'delete',
    url: `/api/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
