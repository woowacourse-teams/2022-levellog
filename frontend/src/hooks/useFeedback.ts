import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import {
  requestPostFeedback,
  requestGetFeedbacksInTeam,
  requestEditFeedback,
  requestDeleteFeedback,
} from 'apis/feedback';
import { FeedbackFormatType, FeedbackCustomHookType, FeedbackType } from 'types/feedback';

const useFeedback = () => {
  const [feedbacks, setFeedbacks] = useState<FeedbackType[]>([]);
  const feedbackRef = useRef<Editor[]>([]);
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  const postFeedback = async ({
    levellogId,
    feedbackResult,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackResult'>) => {
    try {
      await requestPostFeedback({ accessToken, levellogId, feedbackResult });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const getFeedbacksInTeam = async ({ levellogId }: Pick<FeedbackCustomHookType, 'levellogId'>) => {
    try {
      const res = await requestGetFeedbacksInTeam({ accessToken, levellogId });
      const feedbacks = res.data.feedbacks;

      setFeedbacks(feedbacks);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const editFeedback = async ({
    levellogId,
    feedbackId,
    feedbackResult,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackId' | 'feedbackResult'>) => {
    try {
      await requestEditFeedback({ accessToken, levellogId, feedbackId, feedbackResult });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const deleteFeedback = async ({
    levellogId,
    feedbackId,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackId'>) => {
    try {
      await requestDeleteFeedback({ accessToken, levellogId, feedbackId });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const onClickDeleteButton = async ({
    feedbackInfo,
    levellogId,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackInfo'>) => {
    const feedbackId = String(feedbackInfo.id);

    await deleteFeedback({ levellogId, feedbackId });
    await getFeedbacksInTeam({ levellogId });
  };

  const onClickFeedbackAddButton = async ({
    teamId,
    levellogId,
  }: Pick<FeedbackCustomHookType, 'teamId' | 'levellogId'>) => {
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackFormatType = {
      feedback: {
        study: study.getInstance().getEditorElements().mdEditor.innerText,
        speak: speak.getInstance().getEditorElements().mdEditor.innerText,
        etc: etc.getInstance().getEditorElements().mdEditor.innerText,
      },
    };

    await postFeedback({ levellogId, feedbackResult });
    navigate(`/teams/${teamId}/levellogs/${levellogId}/feedbacks`);
  };

  return {
    feedbacks,
    feedbackRef,
    getFeedbacksInTeam,
    postFeedback,
    editFeedback,
    onClickDeleteButton,
    onClickFeedbackAddButton,
  };
};

export default useFeedback;
