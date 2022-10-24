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
} from 'apis/interviewQuestion';
import { WrongAccessToken } from 'apis/utils';

const useInterviewQuestion = () => {
  const { scrollRef: interviewQuestionContentRef, afterRequestScrollDown } =
    useScrollDown<HTMLUListElement>();
  const { showSnackbar } = useSnackbar();
  const interviewQuestionRef = useRef<HTMLInputElement>(null);
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const {
    isError: interviewQuestionError,
    data: interviewQuestionInfos,
    refetch: interviewQuestionRefetch,
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
    ({ interviewQuestion }: Pick<InterviewQuestionPostRequestType, 'interviewQuestion'>) => {
      return requestPostInterviewQuestion({ accessToken, levellogId, interviewQuestion });
    },
    {
      onSuccess: () => {
        if (interviewQuestionRef.current) {
          interviewQuestionRef.current.value = '';
          interviewQuestionRef.current.focus();
        }
        afterRequestScrollDown({ requestFunction: interviewQuestionRefetch });
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
      interviewQuestionId,
      interviewQuestion,
    }: Pick<InterviewQuestionEditRequestType, 'interviewQuestionId' | 'interviewQuestion'>) => {
      return requestEditInterviewQuestion({
        accessToken,
        levellogId,
        interviewQuestionId,
        interviewQuestion,
      });
    },
    {
      onSuccess: () => {
        interviewQuestionRefetch();
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
    ({ interviewQuestionId }: Pick<InterviewQuestionDeleteRequestType, 'interviewQuestionId'>) => {
      return requestDeleteInterviewQuestion({ accessToken, levellogId, interviewQuestionId });
    },
    {
      onSuccess: () => {
        interviewQuestionRefetch();
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
    interviewQuestionId,
  }: Pick<InterviewQuestionDeleteRequestType, 'interviewQuestionId'>) => {
    deleteInterviewQuestion({ interviewQuestionId });
  };

  const onSubmitEditInterviewQuestion = async ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionEditRequestType, 'interviewQuestionId' | 'interviewQuestion'>) => {
    editInterviewQuestion({ interviewQuestionId, interviewQuestion });
  };

  const handleSubmitInterviewQuestion = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (interviewQuestionRef.current) {
      if (interviewQuestionRef.current.value.length < 3) {
        showSnackbar({ message: MESSAGE.WRITE_MORE });

        return;
      }

      postInterviewQuestion({ interviewQuestion: interviewQuestionRef.current.value });
    }
  };

  return {
    interviewQuestionError,
    interviewQuestionInfos,
    interviewQuestionRef,
    interviewQuestionContentRef,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
    handleSubmitInterviewQuestion,
  };
};

export default useInterviewQuestion;
