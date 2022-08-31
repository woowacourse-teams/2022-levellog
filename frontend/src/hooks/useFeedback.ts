import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useSnackbar from 'hooks/useSnackbar';
import useUriBuilder from 'hooks/useUriBuilder';

import { MESSAGE } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import {
  requestPostFeedback,
  requestGetFeedbacksInTeam,
  requestEditFeedback,
  requestGetFeedback,
} from 'apis/feedback';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { FeedbackFormatType, FeedbackCustomHookType, FeedbackType } from 'types/feedback';

const useFeedback = () => {
  const { feedbacksGetUriBuilder } = useUriBuilder();
  const { showSnackbar } = useSnackbar();
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
      showSnackbar({ message: MESSAGE.FEEDBACK_CREATE });
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
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
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const getFeedback = async ({
    levellogId,
    feedbackId,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackId'>) => {
    try {
      const res = await requestGetFeedback({ accessToken, levellogId, feedbackId });

      return res.data.feedback;
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
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
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
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
    navigate(feedbacksGetUriBuilder({ teamId, levellogId }));
  };

  const onClickFeedbackEditButton = async ({
    teamId,
    levellogId,
    feedbackId,
  }: Pick<FeedbackCustomHookType, 'teamId' | 'levellogId' | 'feedbackId'>) => {
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackFormatType = {
      feedback: {
        study: study.getInstance().getEditorElements().mdEditor.innerText,
        speak: speak.getInstance().getEditorElements().mdEditor.innerText,
        etc: etc.getInstance().getEditorElements().mdEditor.innerText,
      },
    };

    await editFeedback({ levellogId, feedbackId, feedbackResult });
    showSnackbar({ message: MESSAGE.FEEDBACK_EDIT_CONFIRM });
    navigate(feedbacksGetUriBuilder({ teamId, levellogId }));
  };

  const onClickFeedbackSaveButton = async ({
    levellogId,
    feedbackId,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackId'>) => {
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackFormatType = {
      feedback: {
        study: study.getInstance().getEditorElements().mdEditor.innerText,
        speak: speak.getInstance().getEditorElements().mdEditor.innerText,
        etc: etc.getInstance().getEditorElements().mdEditor.innerText,
      },
    };

    await editFeedback({ levellogId, feedbackId, feedbackResult });
    showSnackbar({ message: MESSAGE.FEEDBACK_SAVE_CONFIRM });
  };

  const getFeedbackOnRef = async ({
    levellogId,
    feedbackId,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackId'>) => {
    const feedback = await getFeedback({ levellogId, feedbackId });

    if (!feedback) return;
    if (!feedbackRef.current[0]) return;

    feedbackRef.current[0].getInstance().setMarkdown(feedback.study);
    feedbackRef.current[1].getInstance().setMarkdown(feedback.speak);
    feedbackRef.current[2].getInstance().setMarkdown(feedback.etc);
  };

  return {
    feedbacks,
    feedbackRef,
    getFeedbacksInTeam,
    postFeedback,
    editFeedback,
    getFeedbackOnRef,
    onClickFeedbackAddButton,
    onClickFeedbackEditButton,
    onClickFeedbackSaveButton,
  };
};

export default useFeedback;
