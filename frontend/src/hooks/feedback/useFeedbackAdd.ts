import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { requestPostFeedback, FeedbackApiType } from 'apis/feedback';
import { FeedbackFormatType } from 'types/feedback';
import { feedbacksGetUriBuilder } from 'utils/util';

const useFeedbackAdd = () => {
  const { showSnackbar } = useSnackbar();
  const feedbackRef = useRef<Editor[]>([]);
  const navigate = useNavigate();
  const { teamId, levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: postFeedback } = useMutation(
    ({ levellogId, feedbackResult }: Pick<FeedbackApiType, 'levellogId' | 'feedbackResult'>) => {
      return requestPostFeedback({ accessToken, levellogId, feedbackResult });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.FEEDBACK_CREATE });
        navigate(feedbacksGetUriBuilder({ teamId, levellogId }));
      },
    },
  );

  const handleClickFeedbackAddButton = () => {
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackFormatType = {
      feedback: {
        study: study.getInstance().getMarkdown(),
        speak: speak.getInstance().getMarkdown(),
        etc: etc.getInstance().getMarkdown(),
      },
    };

    postFeedback({ levellogId, feedbackResult });
  };

  return {
    feedbackRef,
    handleClickFeedbackAddButton,
  };
};

export default useFeedbackAdd;
