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
  Record<'interviewQuestions', InterviewQuestionInfoType[]>
> => {
  const InterviewQuestionMyGet = `/levellogs/${levellogId}/interview-questions/my`;

  const { data } = await fetcher.get(InterviewQuestionMyGet, AuthorizationHeader(accessToken));

  return data;
};

export const requestGetInterviewQuestionsInLevellog = async ({
  accessToken,
  levellogId,
}: InterviewQuestionRequestCommonType): Promise<
  Record<'interviewQuestions', InterviewQuestionsInLevellogInfoType[]>
> => {
  const InterviewQuestionGetUri = `/levellogs/${levellogId}/interview-questions`;

  const { data } = await fetcher.get(InterviewQuestionGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestPostInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestion,
}: InterviewQuestionPostRequestType) => {
  const InterviewQuestionPostUri = `/levellogs/${levellogId}/interview-questions`;
  const data = { content: interviewQuestion };

  await fetcher.post(InterviewQuestionPostUri, data, AuthorizationHeader(accessToken));
};

export const requestDeleteInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestionId,
}: InterviewQuestionDeleteRequestType) => {
  const InterviewQuestionDeleteUri = `/levellogs/${levellogId}/interview-questions/${interviewQuestionId}`;

  await fetcher.delete(InterviewQuestionDeleteUri, AuthorizationHeader(accessToken));
};

export const requestEditInterviewQuestion = async ({
  accessToken,
  levellogId,
  interviewQuestionId,
  interviewQuestion,
}: InterviewQuestionEditRequestType) => {
  const InterviewQuestionEditUri = `/levellogs/${levellogId}/interview-questions/${interviewQuestionId}`;
  const data = { content: interviewQuestion };

  await fetcher.put(InterviewQuestionEditUri, data, AuthorizationHeader(accessToken));
};

export interface InterviewQuestionRequestCommonType {
  accessToken: string | null;
  levellogId: string | undefined;
}

export interface InterviewQuestionPostRequestType extends InterviewQuestionRequestCommonType {
  interviewQuestion: string;
}

export interface InterviewQuestionDeleteRequestType extends InterviewQuestionRequestCommonType {
  interviewQuestionId: string;
}

export interface InterviewQuestionEditRequestType extends InterviewQuestionRequestCommonType {
  interviewQuestionId: string;
  interviewQuestion: string;
}
