import { deleteFeedbacks, getFeedbacks, postFeedback } from '../api/feedback';
import { FeedbackType } from '../types';

export const useFeedback = () => {
  const useFeedbackAdd = async (feedbackResult: FeedbackType) => {
    try {
      await postFeedback(feedbackResult);
    } catch (err) {
      console.log(err);
    }
  };

  const useFeedbackLookup = async () => {
    try {
      const res = await getFeedbacks();
      const feedbacks = res;
      return res;
    } catch (err) {
      console.log(err);
    }
  };

  const useFeedbackDelete = async (id: number) => {
    try {
      await deleteFeedbacks(id);
    } catch (err) {
      console.log(err);
    }
  };

  return { useFeedbackAdd, useFeedbackLookup, useFeedbackDelete };
};
