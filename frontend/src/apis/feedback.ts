import { AuthorizationHeader, fetcher } from 'apis';

import { FeedbackFormatType } from './../types/feedback';

import { FeedbackType } from 'types/feedback';

export const requestPostFeedback = async ({
  accessToken,
  levellogId,
  feedbackResult,
}: FeedbackPostOrEditApiType) => {
  const feedbackPostUri = `/levellogs/${levellogId}/feedbacks`;

  await fetcher.post(feedbackPostUri, feedbackResult, AuthorizationHeader(accessToken));
};

export const requestGetFeedbacksInTeam = async ({
  accessToken,
  levellogId,
}: FeedbacksGetApiType): Promise<Record<'feedbacks', FeedbackType[]>> => {
  const feedbacksGetUri = `/levellogs/${levellogId}/feedbacks`;

  const { data } = await fetcher.get(feedbacksGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestGetFeedback = async ({
  accessToken,
  levellogId,
  feedbackId,
}: FeedbackGetApiType): Promise<FeedbackType> => {
  const feedbackGetUri = `/levellogs/${levellogId}/feedbacks/${feedbackId}`;

  const { data } = await fetcher.get(feedbackGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestEditFeedback = async ({
  accessToken,
  levellogId,
  feedbackId,
  feedbackResult,
}: FeedbackPostOrEditApiType) => {
  const feedbackPutUri = `/levellogs/${levellogId}/feedbacks/${feedbackId}`;

  await fetcher.put(feedbackPutUri, feedbackResult, AuthorizationHeader(accessToken));
};

interface FeedbacksGetApiType {
  accessToken: string | null;
  levellogId: string | undefined;
}

interface FeedbackGetApiType extends FeedbacksGetApiType {
  feedbackId: string | undefined;
}

interface FeedbackPostOrEditApiType extends FeedbackGetApiType {
  feedbackResult: FeedbackFormatType;
}
