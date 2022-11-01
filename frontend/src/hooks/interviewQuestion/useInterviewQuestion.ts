import { useRef } from 'react';
import { useParams } from 'react-router-dom';

import { queryClient } from 'index';

import { useMutation, useQuery } from '@tanstack/react-query';

import errorHandler from 'hooks/utils/errorHandler';
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
  } = useQuery([QUERY_KEY.INTERVIEW_QUESTION, accessToken, levellogId], () => {
    return requestGetInterviewQuestion({
      accessToken,
      levellogId,
    });
  });

  const { mutate: postInterviewQuestion } = useMutation(
    ({ interviewQuestion }: Pick<InterviewQuestionPostRequestType, 'interviewQuestion'>) => {
      return requestPostInterviewQuestion({ accessToken, levellogId, interviewQuestion });
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries([QUERY_KEY.INTERVIEW_QUESTIONS]);
        if (interviewQuestionRef.current) {
          interviewQuestionRef.current.value = '';
          interviewQuestionRef.current.focus();
        }
        afterRequestScrollDown({ requestFunction: interviewQuestionRefetch });
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
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
        queryClient.invalidateQueries([QUERY_KEY.INTERVIEW_QUESTIONS]);
        interviewQuestionRefetch();
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const { mutate: deleteInterviewQuestion } = useMutation(
    ({ interviewQuestionId }: Pick<InterviewQuestionDeleteRequestType, 'interviewQuestionId'>) => {
      return requestDeleteInterviewQuestion({ accessToken, levellogId, interviewQuestionId });
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries([QUERY_KEY.INTERVIEW_QUESTIONS]);
        interviewQuestionRefetch();
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
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
