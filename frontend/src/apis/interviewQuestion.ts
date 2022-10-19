import { fetcher } from 'apis';

import { AuthorizationHeader } from 'apis/index';
import {
  InterviewQuestionsInLevellogType,
  InterviewQuestionInfoType,
} from 'types/interviewQuestion';

export const requestGetInterviewQuestion = async ({
  accessToken,
  levellogId,
}: QuestionGetApiType): Promise<Record<'interviewQuestions', InterviewQuestionInfoType[]>> => {
  const QUESTION_MY_GET = `/levellogs/${levellogId}/interview-questions/my`;

  const { data } = await fetcher.get(QUESTION_MY_GET, AuthorizationHeader(accessToken));

  return data;
};

export const requestGetInterviewQuestionsInLevellog = async ({
  accessToken,
  levellogId,
}: QuestionGetApiType): Promise<
  Record<'interviewQuestions', InterviewQuestionsInLevellogType[]>
> => {
  const questionGetUri = `/levellogs/${levellogId}/interview-questions`;

  const { data } = await fetcher.get(questionGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestPostInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestion,
}: QuestionPostApiType) => {
  const questionPostUri = `/levellogs/${levellogId}/interview-questions`;
  const data = { content: interviewQuestion };

  await fetcher.post(questionPostUri, data, AuthorizationHeader(accessToken));
};

export const requestDeleteInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestionId,
}: QuestionDeleteApiType) => {
  const questionDeleteUri = `/levellogs/${levellogId}/interview-questions/${interviewQuestionId}`;

  await fetcher.delete(questionDeleteUri, AuthorizationHeader(accessToken));
};

export const requestEditInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestionId,
  interviewQuestion,
}: QuestionEditApiType) => {
  const questionEditUri = `/levellogs/${levellogId}/interview-questions/${interviewQuestionId}`;
  const data = { content: interviewQuestion };

  await fetcher.put(questionEditUri, data, AuthorizationHeader(accessToken));
};

interface QuestionGetApiType {
  accessToken: string | null;
  levellogId: string | undefined;
}

interface QuestionPostApiType extends QuestionGetApiType {
  interviewQuestion: string;
}

interface QuestionDeleteApiType extends QuestionGetApiType {
  interviewQuestionId: string;
}

interface QuestionEditApiType extends QuestionGetApiType {
  interviewQuestionId: string;
  interviewQuestion: string;
}
