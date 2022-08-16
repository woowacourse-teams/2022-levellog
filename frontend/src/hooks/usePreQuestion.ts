import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import {
  requestDeletePreQuestion,
  requestEditPreQuestion,
  requestGetPreQuestion,
  requestPostPreQuestion,
} from 'apis/preQuestion';
import { PreQuestionCustomHookType } from 'types/preQuestion';

const usePreQuestion = () => {
  const [preQuestion, setPreQuestion] = useState('');
  const preQuestionRef = useRef<Editor>(null);
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const getPreQuestion = async ({ levellogId }: Pick<PreQuestionCustomHookType, 'levellogId'>) => {
    try {
      const res = await requestGetPreQuestion({ levellogId, accessToken });
      setPreQuestion(res.data.preQuestion);

      return res.data.preQuestion;
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const postPreQuestion = async ({
    teamId,
    levellogId,
    preQuestion,
  }: Omit<PreQuestionCustomHookType, 'preQuestionId'>) => {
    try {
      await requestPostPreQuestion({
        accessToken,
        levellogId,
        preQuestion,
      });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const deletePreQuestion = async ({
    levellogId,
    preQuestionId,
  }: Pick<PreQuestionCustomHookType, 'levellogId' | 'preQuestionId'>) => {
    try {
      await requestDeletePreQuestion({ accessToken, levellogId, preQuestionId });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const editPreQuestion = async ({
    teamId,
    levellogId,
    preQuestionId,
    preQuestion,
  }: PreQuestionCustomHookType) => {
    try {
      await requestEditPreQuestion({ accessToken, levellogId, preQuestionId, preQuestion });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const getPreQuestionOnRef = async ({
    levellogId,
  }: Pick<PreQuestionCustomHookType, 'levellogId'>) => {
    const preQuestion = await getPreQuestion({ levellogId });
    if (typeof preQuestion === 'string') {
      setPreQuestion(preQuestion);
    }
    if (typeof preQuestion === 'string' && preQuestionRef.current) {
      preQuestionRef.current.getInstance().setMarkdown(preQuestion);
    }
  };

  const onClickPreQuestionAddButton = ({
    teamId,
    levellogId,
  }: Pick<PreQuestionCustomHookType, 'teamId' | 'levellogId'>) => {
    if (preQuestionRef.current) {
      postPreQuestion({
        teamId,
        levellogId,
        preQuestion: preQuestionRef.current.getInstance().getEditorElements().mdEditor.innerText,
      });
      alert('사전질문 등록이 완료되었습니다.');
    }
  };

  const onClickPreQuestionEditButton = ({
    teamId,
    levellogId,
    preQuestionId,
  }: Omit<PreQuestionCustomHookType, 'preQuestion'>) => {
    if (preQuestionRef.current) {
      editPreQuestion({
        teamId,
        levellogId,
        preQuestionId,
        preQuestion: preQuestionRef.current.getInstance().getEditorElements().mdEditor.innerText,
      });
      alert('사전질문 수정이 완료되었습니다.');
    }
    navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
  };

  return {
    preQuestion,
    preQuestionRef,
    getPreQuestion,
    deletePreQuestion,
    getPreQuestionOnRef,
    onClickPreQuestionAddButton,
    onClickPreQuestionEditButton,
  };
};

export default usePreQuestion;
