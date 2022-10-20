import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { requestGetPreQuestion } from 'apis/preQuestion';

const QUERY_KEY = {
  PREQUESTION: 'preQuestion',
};

const usePreQuestionQuery = () => {
  const { showSnackbar } = useSnackbar();
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const {
    isError: preQuestionError,
    isSuccess: preQuestionSuccess,
    data: preQuestion,
  } = useQuery(
    [QUERY_KEY.PREQUESTION, accessToken, levellogId],
    () =>
      requestGetPreQuestion({
        accessToken,
        levellogId,
      }),
    {
      refetchOnMount: false,
      refetchOnReconnect: false,
      refetchOnWindowFocus: false,
      retry: 0,
      retryOnMount: false,
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  return { preQuestionError, preQuestionSuccess, preQuestion };
};

export default usePreQuestionQuery;
