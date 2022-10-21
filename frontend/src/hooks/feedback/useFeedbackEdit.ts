import { useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, QUERY_KEY } from 'constants/constants';

import { FeedbackEditRequestType, requestEditFeedback, requestGetFeedback } from 'apis/feedback';
import { FeedbackType } from 'types/feedback';
import { feedbacksGetUriBuilder } from 'utils/uri';

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
    ({ levellogId, feedbackId, feedback }: Omit<FeedbackEditRequestType, 'accessToken'>) => {
      return requestEditFeedback({ accessToken, levellogId, feedbackId, feedback });
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
    const feedback: FeedbackType = {
      feedback: {
        study: study.getInstance().getEditorElements().mdEditor.innerText,
        speak: speak.getInstance().getEditorElements().mdEditor.innerText,
        etc: etc.getInstance().getEditorElements().mdEditor.innerText,
      },
    };

    editFeedback.mutate({
      levellogId,
      feedbackId,
      feedback,
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
