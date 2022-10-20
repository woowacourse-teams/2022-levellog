import { useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { requestEditFeedback, requestGetFeedback } from 'apis/feedback';
import { FeedbackCustomHookType, FeedbackFormatType } from 'types/feedback';
import { feedbacksGetUriBuilder } from 'utils/util';

const QUERY_KEY = {
  FEEDBACK: 'feedback',
};

const useFeedbackEdit = () => {
  const { showSnackbar } = useSnackbar();
  const feedbackRef = useRef<Editor[]>([]);
  const { teamId, levellogId, feedbackId } = useParams();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const { data: feedback, mutate: getFeedback } = useMutation(
    [QUERY_KEY.FEEDBACK, accessToken, levellogId],
    () => {
      return requestGetFeedback({ accessToken, levellogId, feedbackId });
    },
    {
      onSuccess: (data) => {
        if (!data.feedback) return;

        feedbackRef.current[0].getInstance().setMarkdown(data.feedback.study);
        feedbackRef.current[1].getInstance().setMarkdown(data.feedback.speak);
        feedbackRef.current[2].getInstance().setMarkdown(data.feedback.etc);
      },
      onError: (err: unknown) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const editFeedback = useMutation(
    ({
      levellogId,
      feedbackId,
      feedbackResult,
    }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackId' | 'feedbackResult'>) => {
      return requestEditFeedback({ accessToken, levellogId, feedbackId, feedbackResult });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.FEEDBACK_EDIT });
        navigate(feedbacksGetUriBuilder({ teamId, levellogId }));
      },
      onError: (err: unknown) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const handleClickFeedbackEditButton = () => {
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackFormatType = {
      feedback: {
        study: study.getInstance().getEditorElements().mdEditor.innerText,
        speak: speak.getInstance().getEditorElements().mdEditor.innerText,
        etc: etc.getInstance().getEditorElements().mdEditor.innerText,
      },
    };

    editFeedback.mutate({
      levellogId,
      feedbackId,
      feedbackResult,
    });
  };

  useEffect(() => {
    getFeedback();
  }, [feedbackRef.current]);

  return {
    feedback,
    feedbackRef,
    handleClickFeedbackEditButton,
  };
};

export default useFeedbackEdit;
