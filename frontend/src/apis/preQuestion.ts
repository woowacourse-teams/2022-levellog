import { fetcher } from 'apis';

import { PreQuestionApiType, PreQuestionFormatType } from 'types/preQuestion';

export const requestGetPreQuestion = async ({
  accessToken,
  levellogId,
}: Pick<PreQuestionApiType, 'accessToken' | 'levellogId'>): Promise<PreQuestionFormatType> => {
  const { data } = await fetcher.get(`/levellogs/${levellogId}/pre-questions/my`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export const requestPostPreQuestion = async ({
  accessToken,
  levellogId,
  preQuestionContent,
}: Pick<PreQuestionApiType, 'accessToken' | 'levellogId' | 'preQuestionContent'>) => {
  await fetcher.post(
    `/levellogs/${levellogId}/pre-questions`,
    {
      content: preQuestionContent,
    },
    {
      headers: { Authorization: `Bearer ${accessToken}` },
    },
  );
};

export const requestEditPreQuestion = async ({
  accessToken,
  levellogId,
  preQuestionId,
  preQuestionContent,
}: Omit<PreQuestionApiType, 'preQuestion'>) => {
  await fetcher.put(
    `/levellogs/${levellogId}/pre-questions/${preQuestionId}`,
    {
      content: preQuestionContent,
    },
    {
      headers: { Authorization: `Bearer ${accessToken}` },
    },
  );
};

export const requestDeletePreQuestion = async ({
  accessToken,
  levellogId,
  preQuestionId,
}: Pick<PreQuestionApiType, 'accessToken' | 'levellogId' | 'preQuestionId'>) => {
  await fetcher.delete(`/levellogs/${levellogId}/pre-questions/${preQuestionId}`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
