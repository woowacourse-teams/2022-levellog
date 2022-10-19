import { AuthorizationHeader, fetcher } from 'apis';

import { FeedbackInfoType, FeedbackType } from 'types/feedback';

export const requestPostFeedback = async ({
  accessToken,
  levellogId,
  feedback,
}: FeedbackPostRequestType) => {
  const feedbackPostUri = `/levellogs/${levellogId}/feedbacks`;

  await fetcher.post(feedbackPostUri, feedback, AuthorizationHeader(accessToken));
};

export const requestGetFeedbacksInTeam = async ({
  accessToken,
  levellogId,
}: FeedbacksRequestCommonType): Promise<Record<'feedbacks', FeedbackInfoType[]>> => {
  const feedbacksGetUri = `/levellogs/${levellogId}/feedbacks`;

  const { data } = await fetcher.get(feedbacksGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestGetFeedback = async ({
  accessToken,
  levellogId,
  feedbackId,
}: FeedbackGetRequestType): Promise<FeedbackInfoType> => {
  const feedbackGetUri = `/levellogs/${levellogId}/feedbacks/${feedbackId}`;

  const { data } = await fetcher.get(feedbackGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestEditFeedback = async ({
  accessToken,
  levellogId,
  feedbackId,
  feedback,
}: FeedbackEditRequestType) => {
  const feedbackPutUri = `/levellogs/${levellogId}/feedbacks/${feedbackId}`;

  await fetcher.put(feedbackPutUri, feedback, AuthorizationHeader(accessToken));
};

interface FeedbacksRequestCommonType {
  accessToken: string | null;
  levellogId: string | undefined;
}

interface FeedbackGetRequestType extends FeedbacksRequestCommonType {
  feedbackId: string | undefined;
}

export interface FeedbackPostRequestType extends FeedbacksRequestCommonType {
  feedback: FeedbackType;
}

export interface FeedbackEditRequestType extends FeedbacksRequestCommonType {
  feedbackId: string | undefined;
  feedback: FeedbackType;
}
