import { useRef, useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useScrollDown from 'hooks/useScrollDown';

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

const useInterviewQuestion = () => {
  const { scrollRef: interviewQuestionContentRef, afterRequestScrollDown } = useScrollDown();
  const [interviewQuestionInfos, setInterviewQuestionInfos] = useState<InterviewQuestionInfoType[]>(
    [],
  );
  const [interviewQuestionInfosInLevellog, setInterviewQuestionInfosInLevellog] = useState<
    InterviewQuestionsInLevellogType[]
  >([]);
  const interviewQuestionRef = useRef<HTMLInputElement>(null);
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const getInterviewQuestion = async () => {
    try {
      if (typeof levellogId === 'string') {
        const res = await requestGetInterviewQuestion({ accessToken, levellogId });
        setInterviewQuestionInfos(res.data.interviewQuestions);
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
      }
    }
  };

  const getInterviewQuestionsInLevellog = async () => {
    try {
      if (typeof levellogId === 'string') {
        const res = await requestGetInterviewQuestionsInLevellog({ accessToken, levellogId });
        setInterviewQuestionInfosInLevellog(res.data.interviewQuestions);
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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

  useEffect(() => {
    getInterviewQuestionsInLevellog();
  }, []);

  return {
    interviewQuestionInfos,
    interviewQuestionInfosInLevellog,
    interviewQuestionRef,
    interviewQuestionContentRef,
    getInterviewQuestion,
    getInterviewQuestionsInLevellog,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
    handleSubmitInterviewQuestion,
  };
};

export default useInterviewQuestion;
