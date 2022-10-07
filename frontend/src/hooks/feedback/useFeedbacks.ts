import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { requestGetFeedbacksInTeam } from 'apis/feedback';

const QUERY_KEY = {
  FEEDBACKS: 'feedbacks',
};

const useFeedbacks = () => {
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { isError: feedbackError, data: feedbacks } = useQuery(
    [QUERY_KEY.FEEDBACKS, accessToken, levellogId],
    () =>
      requestGetFeedbacksInTeam({
        accessToken,
        levellogId,
      }),
    {
      cacheTime: 0,
    },
  );

  return {
    feedbackError,
    feedbacks,
  };
};

export default useFeedbacks;
