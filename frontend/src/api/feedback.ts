import axios from 'axios';
import { API_URL } from '../constants/constants';
import { FeedbackType } from '../types';

export const postFeedback = (feedbackResult: Omit<FeedbackType, 'id'>) =>
  axios({
    method: 'post',
    url: `${API_URL}/api/feedbacks`,
    data: { ...feedbackResult },
  });

export const getFeedbacks = () =>
  axios({
    method: 'get',
    url: `${API_URL}/api/feedbacks`,
  });

export const deleteFeedbacks = (id: number) =>
  axios({
    method: 'delete',
    url: `${API_URL}/api/feedbacks/${id}`,
  });
