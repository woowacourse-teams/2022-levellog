import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { Editor } from '@toast-ui/react-editor';
import { requestPostFeedback, requestGetFeedbacksInTeam, requestEditFeedback } from 'apis/feedback';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { FeedbackFormatType, FeedbackCustomHookType, FeedbackType } from 'types/feedback';

const useFeedback = () => {
  const [feedbacks, setFeedbacks] = useState<FeedbackType[]>([]);
  const feedbackRef = useRef<Editor[]>([]);
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const postFeedback = async ({
    levellogId,
    feedbackResult,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackResult'>) => {
    try {
      await requestPostFeedback({ accessToken, levellogId, feedbackResult });
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
      }
    }
  };

  const getFeedbacksInTeam = async ({ levellogId }: Pick<FeedbackCustomHookType, 'levellogId'>) => {
    try {
      const res = await requestGetFeedbacksInTeam({ accessToken, levellogId });
      const feedbacks = res.data.feedbacks;

      setFeedbacks(feedbacks);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
      }
    }
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

  //레벨로그id, 피드백id, FeedbackFormatType의 값 필요함
  const onClickFeedbackEditButton = () => {};

  return {
    feedbacks,
    feedbackRef,
    getFeedbacksInTeam,
    postFeedback,
    editFeedback,
    onClickFeedbackAddButton,
    onClickFeedbackEditButton,
  };
};

export default useFeedback;
