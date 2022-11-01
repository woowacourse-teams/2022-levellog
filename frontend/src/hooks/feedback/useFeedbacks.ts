import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from 'constants/constants';

import { requestGetFeedbacksInTeam } from 'apis/feedback';

const useFeedbacks = () => {
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { data: feedbacks } = useQuery(
    [QUERY_KEY.FEEDBACKS, accessToken, levellogId],
    () =>
      requestGetFeedbacksInTeam({
        accessToken,
        levellogId,
      }),
    {
      staleTime: 60,
    },
  );

  return {
    feedbacks,
  };
};

export default useFeedbacks;
