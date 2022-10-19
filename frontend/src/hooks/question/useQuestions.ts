import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { requestGetQuestionsInLevellog } from 'apis/question';

const QUERY_KEY = {
  INTERVIEW_QUESTIONS: 'Questions',
};

const useQuestions = () => {
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { data: Questions } = useQuery(
    [QUERY_KEY.INTERVIEW_QUESTIONS, accessToken, levellogId],
    () =>
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
