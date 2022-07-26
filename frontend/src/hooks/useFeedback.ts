import { useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { FeedbackPostType } from 'types';

import {
  requestPostFeedback,
  requestGetFeedbacksInTeam,
  requestEditFeedback,
  requestDeleteFeedback,
} from 'apis/feedback';

const useFeedback = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const feedbackRef = useRef([]);
  const { levellogId, teamId, feedbackId } = useParams();
  const accessToken = localStorage.getItem('accessToken') as string;
  const navigator = useNavigate();

  const postFeedback = async ({ feedbackResult }: any) => {
    try {
      await requestPostFeedback({ accessToken, levellogId, feedbackResult });
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const getFeedbacksInTeam = async () => {
    try {
      const res = await requestGetFeedbacksInTeam({ accessToken, levellogId });
      const feedbacks = res.data.feedbacks;

      setFeedbacks(feedbacks);
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const editFeedback = async ({ feedbackResult }: any) => {
    try {
      await requestEditFeedback({ accessToken, levellogId, feedbackId, feedbackResult });
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const deleteFeedback = async ({ feedbackId }) => {
    try {
      await requestDeleteFeedback({ accessToken, levellogId, feedbackId });
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const onClickDeleteButton = async (feedbackInfo: any) => {
    await deleteFeedback({ feedbackId: feedbackInfo.id });
    await getFeedbacksInTeam();
  };

  const handleSubmitFeedbackForm = async (e: any) => {
    e.preventDefault();
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackPostType = {
      feedback: {
        study: study.value,
        speak: speak.value,
        etc: etc.value,
      },
    };

    postFeedback({ feedbackResult });
    navigator(`/teams/${teamId}/levellogs/${levellogId}/feedbacks`);
  };

  return {
    feedbacks,
    feedbackRef,
    levellogId,
    teamId,
    getFeedbacksInTeam,
    postFeedback,
    editFeedback,
    onClickDeleteButton,
    handleSubmitFeedbackForm,
  };
};

export default useFeedback;
