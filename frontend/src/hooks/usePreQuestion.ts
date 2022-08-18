import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import {
  requestDeletePreQuestion,
  requestEditPreQuestion,
  requestGetPreQuestion,
  requestPostPreQuestion,
} from 'apis/preQuestion';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          // alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      alert(MESSAGE.PREQUESTION_ADD_CONFIRM);
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
      alert(MESSAGE.PREQUESTION_EDIT_CONFIRM);
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
