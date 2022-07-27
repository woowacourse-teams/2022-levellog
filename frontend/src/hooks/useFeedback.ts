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
  const accessToken = localStorage.getItem('accessToken') as string;
  const navigator = useNavigate();

  const postFeedback = async ({ levellogId, feedbackResult }: any) => {
    try {
      await requestPostFeedback({ accessToken, levellogId, feedbackResult });
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const getFeedbacksInTeam = async ({ levellogId }: any) => {
    try {
      const res = await requestGetFeedbacksInTeam({ accessToken, levellogId });
      const feedbacks = res.data.feedbacks;

      setFeedbacks(feedbacks);
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const editFeedback = async ({ levellogId, feedbackId, feedbackResult }: any) => {
    try {
      await requestEditFeedback({ accessToken, levellogId, feedbackId, feedbackResult });
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const deleteFeedback = async ({ levellogId, feedbackId }) => {
    try {
      await requestDeleteFeedback({ accessToken, levellogId, feedbackId });
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const onClickDeleteButton = async ({ feedbackInfo, levellogId }: any) => {
    const feedbackId = feedbackInfo.id;

    await deleteFeedback({ levellogId, feedbackId });
    await getFeedbacksInTeam({ levellogId });
  };

  const onSubmitFeedbackForm = async ({ teamId, levellogId }: any) => {
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackPostType = {
      feedback: {
        study: study.value,
        speak: speak.value,
        etc: etc.value,
      },
    };

    await postFeedback({ feedbackResult });
    navigator(`/teams/${teamId}/levellogs/${levellogId}/feedbacks`);
  };

  return {
    feedbacks,
    feedbackRef,
    getFeedbacksInTeam,
    postFeedback,
    editFeedback,
    onClickDeleteButton,
    onSubmitFeedbackForm,
  };
};

export default useFeedback;
