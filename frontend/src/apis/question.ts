import { fetcher } from 'apis';

import { AuthorizationHeader } from 'apis/index';
import { QuestionsInLevellogInfoType, QuestionInfoType } from 'types/question';

export const requestGetQuestion = async ({
  accessToken,
  levellogId,
}: QuestionRequestCommonType): Promise<Record<'Questions', QuestionInfoType[]>> => {
  const QUESTION_MY_GET = `/levellogs/${levellogId}/interview-questions/my`;

  const { data } = await fetcher.get(QUESTION_MY_GET, AuthorizationHeader(accessToken));

  return data;
};

export const requestGetQuestionsInLevellog = async ({
  accessToken,
  levellogId,
}: QuestionRequestCommonType): Promise<Record<'Questions', QuestionsInLevellogInfoType[]>> => {
  const questionGetUri = `/levellogs/${levellogId}/interview-questions`;

  const { data } = await fetcher.get(questionGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestPostQuestion = async ({
  accessToken,
  levellogId,
  Question,
}: QuestionPostRequestType) => {
  const questionPostUri = `/levellogs/${levellogId}/interview-questions`;
  const data = { content: Question };

  await fetcher.post(questionPostUri, data, AuthorizationHeader(accessToken));
};

export const requestDeleteQuestion = async ({
  accessToken,
  levellogId,
  QuestionId,
}: QuestionDeleteRequestType) => {
  const questionDeleteUri = `/levellogs/${levellogId}/interview-questions/${QuestionId}`;

  await fetcher.delete(questionDeleteUri, AuthorizationHeader(accessToken));
};

export const requestEditQuestion = async ({
  accessToken,
  levellogId,
  QuestionId,
  Question,
}: QuestionEditRequestType) => {
  const questionEditUri = `/levellogs/${levellogId}/interview-questions/${QuestionId}`;
  const data = { content: Question };

  await fetcher.put(questionEditUri, data, AuthorizationHeader(accessToken));
};

export interface QuestionRequestCommonType {
  accessToken: string | null;
  levellogId: string | undefined;
}

export interface QuestionPostRequestType extends QuestionRequestCommonType {
  Question: string;
}

export interface QuestionDeleteRequestType extends QuestionRequestCommonType {
  QuestionId: string;
}

export interface QuestionEditRequestType extends QuestionRequestCommonType {
  QuestionId: string;
  Question: string;
}
