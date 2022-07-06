import axios from 'axios';
import { FeedbackType } from '../types';

export const postFeedback = (feedbackResult: FeedbackType) => {
  axios({
    method: 'post',
    url: '/api/feedbacks',
    data: { feedbackResult },
  });
};

export const getFeedbacks = () => {
  axios({
    method: 'get',
    url: '/api/feedbacks',
  });
};

export const deleteFeedbacks = (id: number) => {
  axios({
    method: 'delete',
    url: `/api/feedbacks/${id}`,
  });
};
