import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from 'constants/constants';

import { requestGetPreQuestion } from 'apis/preQuestion';

const usePreQuestionQuery = () => {
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { isError: preQuestionError, data: preQuestion } = useQuery(
    [QUERY_KEY.PRE_QUESTION, accessToken, levellogId],
    () =>
      requestGetPreQuestion({
        accessToken,
        levellogId,
      }),
    {
      cacheTime: 0,
      useErrorBoundary: false,
    },
  );

  return { preQuestionError, preQuestion };
};

export default usePreQuestionQuery;
