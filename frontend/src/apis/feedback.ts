import { fetcher } from 'apis';
import axios from 'axios';

import { FeedbackApiType, FeedbackType } from 'types/feedback';

export const requestPostFeedback = async ({
  accessToken,
  levellogId,
  feedbackResult,
}: Omit<FeedbackApiType, 'feedbackId'>) => {
  await fetcher.post(`/levellogs/${levellogId}/feedbacks`, feedbackResult, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestGetFeedbacksInTeam = async ({
  accessToken,
  levellogId,
}: Pick<FeedbackApiType, 'accessToken' | 'levellogId'>): Promise<
  Record<'feedbacks', FeedbackType[]>
> => {
  const { data } = await fetcher.get(`/levellogs/${levellogId}/feedbacks`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export const requestGetFeedback = async ({
  accessToken,
  levellogId,
  feedbackId,
}: Omit<FeedbackApiType, 'feedbackResult'>): Promise<FeedbackType> => {
  const { data } = await fetcher.get(`/levellogs/${levellogId}/feedbacks/${feedbackId}`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export const requestEditFeedback = async ({
  accessToken,
  levellogId,
  feedbackId,
  feedbackResult,
}: FeedbackApiType) => {
  await fetcher.put(
    `/levellogs/${levellogId}/feedbacks/${feedbackId}`,
    {
      ...feedbackResult,
    },
    {
      headers: { Authorization: `Bearer ${accessToken}` },
    },
  );
};
