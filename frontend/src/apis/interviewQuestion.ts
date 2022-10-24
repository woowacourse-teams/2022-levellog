import { fetcher } from 'apis';

import { AuthorizationHeader } from 'apis/index';
import {
  InterviewQuestionsInLevellogInfoType,
  InterviewQuestionInfoType,
} from 'types/interviewQuestion';

export const requestGetInterviewQuestion = async ({
  accessToken,
  levellogId,
}: InterviewQuestionRequestCommonType): Promise<
  Record<'InterviewQuestions', InterviewQuestionInfoType[]>
> => {
  const InterviewQuestionMyGet = `/levellogs/${levellogId}/interview-questions/my`;

  const { data } = await fetcher.get(InterviewQuestionMyGet, AuthorizationHeader(accessToken));

  return data;
};

export const requestGetInterviewQuestionsInLevellog = async ({
  accessToken,
  levellogId,
}: InterviewQuestionRequestCommonType): Promise<
  Record<'InterviewQuestions', InterviewQuestionsInLevellogInfoType[]>
> => {
  const InterviewQuestionGetUri = `/levellogs/${levellogId}/interview-questions`;

  const { data } = await fetcher.get(InterviewQuestionGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestPostInterviewQuestion = async ({
  accessToken,
  levellogId,
  InterviewQuestion,
}: InterviewQuestionPostRequestType) => {
  const InterviewQuestionPostUri = `/levellogs/${levellogId}/interview-questions`;
  const data = { content: InterviewQuestion };

  await fetcher.post(InterviewQuestionPostUri, data, AuthorizationHeader(accessToken));
};

export const requestDeleteInterviewQuestion = async ({
  accessToken,
  levellogId,
  InterviewQuestionId,
}: InterviewQuestionDeleteRequestType) => {
  const InterviewQuestionDeleteUri = `/levellogs/${levellogId}/interview-questions/${InterviewQuestionId}`;

  await fetcher.delete(InterviewQuestionDeleteUri, AuthorizationHeader(accessToken));
};

export const requestEditInterviewQuestion = async ({
  accessToken,
  levellogId,
  InterviewQuestionId,
  InterviewQuestion,
}: InterviewQuestionEditRequestType) => {
  const InterviewQuestionEditUri = `/levellogs/${levellogId}/interview-questions/${InterviewQuestionId}`;
  const data = { content: InterviewQuestion };

  await fetcher.put(InterviewQuestionEditUri, data, AuthorizationHeader(accessToken));
};

export interface InterviewQuestionRequestCommonType {
  accessToken: string | null;
  levellogId: string | undefined;
}

export interface InterviewQuestionPostRequestType extends InterviewQuestionRequestCommonType {
  InterviewQuestion: string;
}

export interface InterviewQuestionDeleteRequestType extends InterviewQuestionRequestCommonType {
  InterviewQuestionId: string;
}

export interface InterviewQuestionEditRequestType extends InterviewQuestionRequestCommonType {
  InterviewQuestionId: string;
  InterviewQuestion: string;
}
