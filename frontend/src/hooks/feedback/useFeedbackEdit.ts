import { useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

import { useMutation, useQuery } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useSnackbar from 'hooks/useSnackbar';

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
  const { levellogId, feedbackId } = useParams();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const { isError: feedbackError, data: feedback } = useQuery(
    [QUERY_KEY.FEEDBACK, accessToken, levellogId],
    () => {
      return requestGetFeedback({ accessToken, levellogId, feedbackId });
    },
    {
      onSuccess: () => {
        if (!feedback) return;
        if (!feedbackRef.current[0]) return;

        feedbackRef.current[0].getInstance().setMarkdown(feedback.feedback.study);
        feedbackRef.current[1].getInstance().setMarkdown(feedback.feedback.speak);
        feedbackRef.current[2].getInstance().setMarkdown(feedback.feedback.etc);
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
  );

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

    editFeedback.mutate({ levellogId, feedbackId, feedbackResult });

    if (editFeedback.isError) {
      showSnackbar({ message: 'editFeedback.error.message' });
      return;
    }

    showSnackbar({ message: MESSAGE.FEEDBACK_EDIT });
    navigate(feedbacksGetUriBuilder({ teamId, levellogId }));
  };

  return {
    feedbackError,
    feedbackRef,
    onClickFeedbackEditButton,
  };
};

export default useFeedbackEdit;
