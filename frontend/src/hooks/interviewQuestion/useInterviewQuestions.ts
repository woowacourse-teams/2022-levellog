import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from 'constants/constants';

import { requestGetInterviewQuestionsInLevellog } from 'apis/interviewQuestion';

const useInterviewQuestions = () => {
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { data: interviewQuestions } = useQuery(
    [QUERY_KEY.INTERVIEW_QUESTIONS, accessToken, levellogId],
    () =>
      requestGetInterviewQuestionsInLevellog({
        accessToken,
        levellogId,
      }),
  );

  return {
    interviewQuestions: interviewQuestions?.interviewQuestions,
  };
};

export default useInterviewQuestions;
