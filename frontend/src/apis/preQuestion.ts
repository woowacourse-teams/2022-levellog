import { fetcher } from 'apis';

import { AuthorizationHeader } from 'apis/index';
import { PreQuestionFormatType } from 'types/preQuestion';

export const requestGetPreQuestion = async ({
  accessToken,
  levellogId,
}: PreQuestionGetRequestType): Promise<PreQuestionFormatType> => {
  const preQuestionGetUri = `/levellogs/${levellogId}/pre-questions/my`;

  const { data } = await fetcher.get(preQuestionGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestPostPreQuestion = async ({
  accessToken,
  levellogId,
  preQuestionContent,
}: PreQuestionPostRequestType) => {
  const preQuestionPostUri = `/levellogs/${levellogId}/pre-questions`;
  const data = { content: preQuestionContent };

  await fetcher.post(preQuestionPostUri, data, AuthorizationHeader(accessToken));
};

export const requestEditPreQuestion = async ({
  accessToken,
  levellogId,
  preQuestionId,
  preQuestionContent,
}: PreQuestionEditRequestType) => {
  const preQuestionPutUri = `/levellogs/${levellogId}/pre-questions/${preQuestionId}`;
  const data = { content: preQuestionContent };

  await fetcher.put(preQuestionPutUri, data, AuthorizationHeader(accessToken));
};

export const requestDeletePreQuestion = async ({
  accessToken,
  levellogId,
  preQuestionId,
}: PreQuestionDeleteRequestType) => {
  const preQuestionDeleteUri = `/levellogs/${levellogId}/pre-questions/${preQuestionId}`;

  await fetcher.delete(preQuestionDeleteUri, AuthorizationHeader(accessToken));
};

interface PreQuestionGetRequestType {
  accessToken: string | null;
  levellogId: string | undefined;
}

interface PreQuestionDeleteRequestType extends PreQuestionGetRequestType {
  preQuestionId: string | undefined;
}

interface PreQuestionPostRequestType extends PreQuestionGetRequestType {
  preQuestionContent: string;
}

interface PreQuestionEditRequestType extends PreQuestionGetRequestType {
  preQuestionId: string | undefined;
  preQuestionContent: string;
}
