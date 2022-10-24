import { useRef } from 'react';
import { useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useMutation, useQuery } from '@tanstack/react-query';

import useScrollDown from 'hooks/utils/useScrollDown';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, QUERY_KEY } from 'constants/constants';

import {
  requestDeleteInterviewQuestion,
  requestEditInterviewQuestion,
  requestGetInterviewQuestion,
  requestPostInterviewQuestion,
  InterviewQuestionDeleteRequestType,
  InterviewQuestionEditRequestType,
  InterviewQuestionPostRequestType,
} from 'apis/InterviewQuestion';
import { WrongAccessToken } from 'apis/utils';

const useInterviewQuestion = () => {
  const { scrollRef: InterviewQuestionContentRef, afterRequestScrollDown } =
    useScrollDown<HTMLUListElement>();
  const { showSnackbar } = useSnackbar();
  const InterviewQuestionRef = useRef<HTMLInputElement>(null);
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const {
    isError: InterviewQuestionError,
    data: InterviewQuestionInfos,
    refetch: InterviewQuestionRefetch,
  } = useQuery(
    [QUERY_KEY.INTERVIEW_QUESTION, accessToken, levellogId],
    () => {
      return requestGetInterviewQuestion({
        accessToken,
        levellogId,
      });
    },
    {
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (WrongAccessToken({ message: responseBody.data.message, showSnackbar })) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const { mutate: postInterviewQuestion } = useMutation(
    ({ InterviewQuestion }: Pick<InterviewQuestionPostRequestType, 'InterviewQuestion'>) => {
      return requestPostInterviewQuestion({ accessToken, levellogId, InterviewQuestion });
    },
    {
      onSuccess: () => {
        if (InterviewQuestionRef.current) {
          InterviewQuestionRef.current.value = '';
          InterviewQuestionRef.current.focus();
        }
        afterRequestScrollDown({ requestFunction: InterviewQuestionRefetch });
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (WrongAccessToken({ message: responseBody.data.message, showSnackbar })) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const { mutate: editInterviewQuestion } = useMutation(
    ({
      InterviewQuestionId,
      InterviewQuestion,
    }: Pick<InterviewQuestionEditRequestType, 'InterviewQuestionId' | 'InterviewQuestion'>) => {
      return requestEditInterviewQuestion({
        accessToken,
        levellogId,
        InterviewQuestionId,
        InterviewQuestion,
      });
    },
    {
      onSuccess: () => {
        InterviewQuestionRefetch();
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (WrongAccessToken({ message: responseBody.data.message, showSnackbar })) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const { mutate: deleteInterviewQuestion } = useMutation(
    ({ InterviewQuestionId }: Pick<InterviewQuestionDeleteRequestType, 'InterviewQuestionId'>) => {
      return requestDeleteInterviewQuestion({ accessToken, levellogId, InterviewQuestionId });
    },
    {
      onSuccess: () => {
        InterviewQuestionRefetch();
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (WrongAccessToken({ message: responseBody.data.message, showSnackbar })) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const onClickDeleteInterviewQuestionButton = async ({
    InterviewQuestionId,
  }: Pick<InterviewQuestionDeleteRequestType, 'InterviewQuestionId'>) => {
    deleteInterviewQuestion({ InterviewQuestionId });
  };

  const onSubmitEditInterviewQuestion = async ({
    InterviewQuestionId,
    InterviewQuestion,
  }: Pick<InterviewQuestionEditRequestType, 'InterviewQuestionId' | 'InterviewQuestion'>) => {
    editInterviewQuestion({ InterviewQuestionId, InterviewQuestion });
  };

  const handleSubmitInterviewQuestion = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (InterviewQuestionRef.current) {
      if (InterviewQuestionRef.current.value.length < 3) {
        showSnackbar({ message: MESSAGE.WRITE_MORE });

        return;
      }

      postInterviewQuestion({ InterviewQuestion: InterviewQuestionRef.current.value });
    }
  };

  return {
    InterviewQuestionError,
    InterviewQuestionInfos,
    InterviewQuestionRef,
    InterviewQuestionContentRef,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
    handleSubmitInterviewQuestion,
  };
};

export default useInterviewQuestion;
