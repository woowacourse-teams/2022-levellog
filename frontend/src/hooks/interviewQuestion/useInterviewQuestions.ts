import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { requestGetInterviewQuestionsInLevellog } from 'apis/interviewQuestion';

const QUERY_KEY = {
  INTERVIEW_QUESTIONS: 'interviewQuestions',
};

const useInterviewQuestions = () => {
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { isError: interviewQuestionsError, data: interviewQuestions } = useQuery(
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
