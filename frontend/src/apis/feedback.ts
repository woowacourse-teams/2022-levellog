import axios from 'axios';
import { API_URL, SERVER_PATH } from '../constants/constants';
import { FeedbackType } from '../types';

export const postFeedback = (feedbackResult: Omit<FeedbackType, 'id'>) =>
  axios({
    method: 'post',
    url: SERVER_PATH.FEEDBACKS,
    data: { ...feedbackResult },
  });

export const getFeedbacks = () =>
  axios({
    method: 'get',
    url: SERVER_PATH.FEEDBACKS,
  });

export const deleteFeedbacks = (id: number) =>
  axios({
    method: 'delete',
    url: `${SERVER_PATH.FEEDBACKS}/${id}`,
  });
