import { fetcher } from 'apis';

import {
  InterviewQuestionApiType,
  InterviewQuestionsInLevellogType,
  InterviewQuestionInfoType,
} from 'types/interviewQuestion';

export const requestGetInterviewQuestion = async ({
  accessToken,
  levellogId,
}: Pick<InterviewQuestionApiType, 'accessToken' | 'levellogId'>): Promise<
  Record<'interviewQuestions', InterviewQuestionInfoType[]>
> => {
  const { data } = await fetcher.get(`/levellogs/${levellogId}/interview-questions/my`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export const requestGetInterviewQuestionsInLevellog = async ({
  accessToken,
  levellogId,
}: Pick<InterviewQuestionApiType, 'accessToken' | 'levellogId'>): Promise<
  Record<'interviewQuestions', InterviewQuestionsInLevellogType[]>
> => {
  const { data } = await fetcher.get(`/levellogs/${levellogId}/interview-questions`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export const requestPostInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestion,
}: Omit<InterviewQuestionApiType, 'interviewQuestionId'>) => {
  await fetcher.post(
    `/levellogs/${levellogId}/interview-questions`,
    {
      content: interviewQuestion,
    },
    {
      headers: { Authorization: `Bearer ${accessToken}` },
    },
  );
};

export const requestDeleteInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestionId,
}: Omit<InterviewQuestionApiType, 'interviewQuestion'>) => {
  await fetcher.delete(`/levellogs/${levellogId}/interview-questions/${interviewQuestionId}`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestEditInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestionId,
  interviewQuestion,
}: InterviewQuestionApiType) => {
  await fetcher.put(
    `/levellogs/${levellogId}/interview-questions/${interviewQuestionId}`,
    {
      content: interviewQuestion,
    },
    {
      headers: { Authorization: `Bearer ${accessToken}` },
    },
  );
};
