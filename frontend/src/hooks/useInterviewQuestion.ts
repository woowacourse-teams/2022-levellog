import { useRef, useState } from 'react';
import { useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useScrollDown from 'hooks/useScrollDown';

import { MESSAGE } from 'constants/constants';

import {
  requestDeleteInterviewQuestion,
  requestEditInterviewQuestion,
  requestGetInterviewQuestion,
  requestPostInterviewQuestion,
} from 'apis/interviewQuestion';
import { InterviewQuestionApiType, InterviewQuestionType } from 'types/interviewQuestion';

const useInterviewQuestion = () => {
  const { scrollRef: interviewQuestionContentRef, afterRequestScrollDown } = useScrollDown();
  const [interviewQuestionsInfo, setInterviewQuestions] = useState<InterviewQuestionType[]>([]);
  const interviewQuestionRef = useRef<HTMLInputElement>(null);
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const getInterviewQuestion = async () => {
    try {
      if (typeof levellogId === 'string') {
        const res = await requestGetInterviewQuestion({ accessToken, levellogId });
        setInterviewQuestions(res.data.interviewQuestions);
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const postInterviewQuestion = async ({
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestion'>) => {
    try {
      if (typeof levellogId === 'string') {
        await requestPostInterviewQuestion({ accessToken, levellogId, interviewQuestion });
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const deleteInterviewQuestion = async ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => {
    try {
      if (typeof levellogId === 'string') {
        await requestDeleteInterviewQuestion({ accessToken, levellogId, interviewQuestionId });
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const editInterviewQuestion = async ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => {
    try {
      if (typeof levellogId === 'string') {
        await requestEditInterviewQuestion({
          accessToken,
          levellogId,
          interviewQuestionId,
          interviewQuestion,
        });
        getInterviewQuestion();
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const onClickDeleteInterviewQuestionButton = async ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => {
    await deleteInterviewQuestion({ interviewQuestionId });
    if (typeof levellogId === 'string') {
      getInterviewQuestion();
    }
  };

  const onSubmitEditInterviewQuestion = async ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => {
    await editInterviewQuestion({ interviewQuestionId, interviewQuestion });
    if (typeof levellogId === 'string') {
      getInterviewQuestion();
    }
  };

  const handleSubmitInterviewQuestion = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (interviewQuestionRef.current) {
      if (interviewQuestionRef.current.value.length < 3) {
        alert(MESSAGE.WRITE_MORE);

        return;
      }
      await postInterviewQuestion({ interviewQuestion: interviewQuestionRef.current.value });
      afterRequestScrollDown({ requestFunction: getInterviewQuestion });

      interviewQuestionRef.current.value = '';
      interviewQuestionRef.current.focus();
    }
  };

  return {
    interviewQuestionsInfo,
    interviewQuestionRef,
    interviewQuestionContentRef,
    getInterviewQuestion,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
    handleSubmitInterviewQuestion,
  };
};

export default useInterviewQuestion;
