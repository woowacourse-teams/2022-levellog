import { useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import {
  requestPostFeedback,
  requestGetFeedbacksInTeam,
  requestEditFeedback,
  requestDeleteFeedback,
} from 'apis/feedback';
import { FeedbackFormatType, FeedbackCustomHookType, FeedbackInfoType } from 'types/feedback';

const useFeedback = (): {
  feedbacks: FeedbackInfoType[];
  feedbackRef: React.MutableRefObject<HTMLInputElement[]>;
  getFeedbacksInTeam: ({ levellogId }: Pick<FeedbackCustomHookType, 'levellogId'>) => Promise<void>;
  postFeedback: ({
    levellogId,
    feedbackResult,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackResult'>) => void;
  editFeedback: ({
    levellogId,
    feedbackId,
    feedbackResult,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackId' | 'feedbackResult'>) => Promise<void>;
  onClickDeleteButton: ({
    feedbackInfo,
    levellogId,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackInfo'>) => Promise<void>;
  onSubmitFeedbackForm: ({
    teamId,
    levellogId,
  }: Pick<FeedbackCustomHookType, 'teamId' | 'levellogId'>) => Promise<void>;
} => {
  const [feedbacks, setFeedbacks] = useState<FeedbackInfoType[]>([]);
  const feedbackRef = useRef<Array<HTMLInputElement>>([]);
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  const postFeedback = async ({
    levellogId,
    feedbackResult,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackResult'>): Promise<void> => {
    try {
      await requestPostFeedback({ accessToken, levellogId, feedbackResult });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

  const getFeedbacksInTeam = async ({
    levellogId,
  }: Pick<FeedbackCustomHookType, 'levellogId'>): Promise<void> => {
    try {
      const res = await requestGetFeedbacksInTeam({ accessToken, levellogId });
      const feedbacks = res.data.feedbacks;

      setFeedbacks(feedbacks);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
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
        navigate(ROUTES_PATH.NOT_FOUND);
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
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

  const onClickDeleteButton = async ({
    feedbackInfo,
    levellogId,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackInfo'>): Promise<void> => {
    const feedbackId = String(feedbackInfo.id);

    await deleteFeedback({ levellogId, feedbackId });
    await getFeedbacksInTeam({ levellogId });
  };

  const onSubmitFeedbackForm = async ({
    teamId,
    levellogId,
  }: Pick<FeedbackCustomHookType, 'teamId' | 'levellogId'>): Promise<void> => {
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackFormatType = {
      feedback: {
        study: study.value,
        speak: speak.value,
        etc: etc.value,
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
    onSubmitFeedbackForm,
  };
};

export default useFeedback;
