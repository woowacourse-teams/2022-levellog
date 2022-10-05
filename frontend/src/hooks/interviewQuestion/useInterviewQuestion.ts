import { useRef, useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useMutation, useQuery } from '@tanstack/react-query';

import useScrollDown from 'hooks/useScrollDown';
import useSnackbar from 'hooks/useSnackbar';

import { MESSAGE } from 'constants/constants';

import {
  requestDeleteInterviewQuestion,
  requestEditInterviewQuestion,
  requestGetInterviewQuestion,
  requestGetInterviewQuestionsInLevellog,
  requestPostInterviewQuestion,
} from 'apis/interviewQuestion';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import {
  InterviewQuestionApiType,
  InterviewQuestionsInLevellogType,
  InterviewQuestionInfoType,
} from 'types/interviewQuestion';
import { tryCatch } from 'utils/util';

const QUERY_KEY = {
  INTERVIEW_QUESTION: 'interviewQuestion',
};

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
    refetch,
  } = useQuery(
    [QUERY_KEY.INTERVIEW_QUESTION, accessToken, levellogId],
    () =>
      requestGetInterviewQuestion({
        accessToken,
        levellogId,
      }),
    {
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (
            토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
          ) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const { mutate: postInterviewQuestion } = useMutation(
    ({ interviewQuestion }: Pick<InterviewQuestionApiType, 'interviewQuestion'>) => {
      return requestPostInterviewQuestion({ accessToken, levellogId, interviewQuestion });
    },
    {
      onSuccess: () => {
        if (interviewQuestionRef.current) {
          interviewQuestionRef.current.value = '';
          interviewQuestionRef.current.focus();
        }
        afterRequestScrollDown({ requestFunction: refetch });
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (
            토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
          ) {
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
    }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => {
      return requestEditInterviewQuestion({
        accessToken,
        levellogId,
        interviewQuestionId,
        interviewQuestion,
      });
    },
    {
      onSuccess: () => {
        refetch();
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (
            토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
          ) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const { mutate: deleteInterviewQuestion } = useMutation(
    ({ interviewQuestionId }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => {
      return requestDeleteInterviewQuestion({ accessToken, levellogId, interviewQuestionId });
    },
    {
      onSuccess: () => {
        refetch();
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (
            토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
          ) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const onClickDeleteInterviewQuestionButton = async ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => {
    deleteInterviewQuestion({ interviewQuestionId });
  };

  const onSubmitEditInterviewQuestion = async ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => {
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
