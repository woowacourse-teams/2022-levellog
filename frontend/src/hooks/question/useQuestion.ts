import { useRef } from 'react';
import { useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useMutation, useQuery } from '@tanstack/react-query';

import useScrollDown from 'hooks/utils/useScrollDown';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, QUERY_KEY } from 'constants/constants';

import {
  requestDeleteQuestion,
  requestEditQuestion,
  requestGetQuestion,
  requestPostQuestion,
  QuestionDeleteRequestType,
  QuestionEditRequestType,
  QuestionPostRequestType,
} from 'apis/question';
import { WrongAccessToken } from 'apis/utils';

const useQuestion = () => {
  const { scrollRef: QuestionContentRef, afterRequestScrollDown } =
    useScrollDown<HTMLUListElement>();
  const { showSnackbar } = useSnackbar();
  const QuestionRef = useRef<HTMLInputElement>(null);
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const {
    isError: QuestionError,
    data: QuestionInfos,
    refetch: QuestionRefetch,
  } = useQuery(
    [QUERY_KEY.QUESTION, accessToken, levellogId],
    () => {
      return requestGetQuestion({
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

  const { mutate: postQuestion } = useMutation(
    ({ Question }: Pick<QuestionPostRequestType, 'Question'>) => {
      return requestPostQuestion({ accessToken, levellogId, Question });
    },
    {
      onSuccess: () => {
        if (QuestionRef.current) {
          QuestionRef.current.value = '';
          QuestionRef.current.focus();
        }
        afterRequestScrollDown({ requestFunction: QuestionRefetch });
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

  const { mutate: editQuestion } = useMutation(
    ({ QuestionId, Question }: Pick<QuestionEditRequestType, 'QuestionId' | 'Question'>) => {
      return requestEditQuestion({
        accessToken,
        levellogId,
        QuestionId,
        Question,
      });
    },
    {
      onSuccess: () => {
        QuestionRefetch();
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

  const { mutate: deleteQuestion } = useMutation(
    ({ QuestionId }: Pick<QuestionDeleteRequestType, 'QuestionId'>) => {
      return requestDeleteQuestion({ accessToken, levellogId, QuestionId });
    },
    {
      onSuccess: () => {
        QuestionRefetch();
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

  const onClickDeleteQuestionButton = async ({
    QuestionId,
  }: Pick<QuestionDeleteRequestType, 'QuestionId'>) => {
    deleteQuestion({ QuestionId });
  };

  const onSubmitEditQuestion = async ({
    QuestionId,
    Question,
  }: Pick<QuestionEditRequestType, 'QuestionId' | 'Question'>) => {
    editQuestion({ QuestionId, Question });
  };

  const handleSubmitQuestion = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (QuestionRef.current) {
      if (QuestionRef.current.value.length < 3) {
        showSnackbar({ message: MESSAGE.WRITE_MORE });

        return;
      }

      postQuestion({ Question: QuestionRef.current.value });
    }
  };

  return {
    QuestionError,
    QuestionInfos,
    QuestionRef,
    QuestionContentRef,
    onClickDeleteQuestionButton,
    onSubmitEditQuestion,
    handleSubmitQuestion,
  };
};

export default useQuestion;
