import { useNavigate } from 'react-router-dom';

import { deleteFeedbacks, getFeedbacks, postFeedback } from '../apis/feedback';
import { FeedbackType } from '../types';

const useFeedback = () => {
  const navigate = useNavigate();

  const feedbackAdd = async (feedbackResult: Omit<FeedbackType, 'id'>) => {
    try {
      await postFeedback(feedbackResult);
      navigate('/');
    } catch (err) {
      console.log(err);
    }
  };

  const feedbackLookup = async () => {
    try {
      const res = await getFeedbacks();
      const feedbacks = res.data;

      return feedbacks;
    } catch (err) {
      console.log(err);
    }
  };

  const feedbackDelete = async (id: number) => {
    try {
      await deleteFeedbacks(id);
    } catch (err) {
      console.log(err);
    }
  };

  return { feedbackAdd, feedbackLookup, feedbackDelete };
};

export default useFeedback;
