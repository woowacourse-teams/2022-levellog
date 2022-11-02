import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { queryClient } from 'index';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, QUERY_KEY } from 'constants/constants';

import { requestPostFeedback, FeedbackPostRequestType } from 'apis/feedback';
import { FeedbackType } from 'types/feedback';
import { feedbacksGetUriBuilder } from 'utils/uri';

const useFeedbackAdd = () => {
  const { showSnackbar } = useSnackbar();
  const feedbackRef = useRef<Editor[]>([]);
  const navigate = useNavigate();
  const { teamId, levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: postFeedback } = useMutation(
    ({ levellogId, feedback }: Omit<FeedbackPostRequestType, 'accessToken'>) => {
      return requestPostFeedback({ accessToken, levellogId, feedback });
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries([QUERY_KEY.FEEDBACKS]);
        showSnackbar({ message: MESSAGE.FEEDBACK_CREATE });
        navigate(feedbacksGetUriBuilder({ teamId, levellogId }));
      },
      onError: (err: unknown) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const handleClickFeedbackAddButton = () => {
    const [study, speak, etc] = feedbackRef.current;
    const feedback: FeedbackType = {
      feedback: {
        study: study.getInstance().getMarkdown(),
        speak: speak.getInstance().getMarkdown(),
        etc: etc.getInstance().getMarkdown(),
      },
    };

    postFeedback({ levellogId, feedback });
  };

  return {
    feedbackRef,
    handleClickFeedbackAddButton,
  };
};

export default useFeedbackAdd;
