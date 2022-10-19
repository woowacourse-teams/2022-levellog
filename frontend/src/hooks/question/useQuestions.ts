import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from 'constants/constants';

import { requestGetQuestionsInLevellog } from 'apis/question';

const useQuestions = () => {
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { data: Questions } = useQuery([QUERY_KEY.QUESTIONS, accessToken, levellogId], () =>
    requestGetQuestionsInLevellog({
      accessToken,
      levellogId,
    }),
  );

  return {
    Questions: Questions?.Questions,
  };
};

export default useQuestions;
