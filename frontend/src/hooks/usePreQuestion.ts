import { useRef } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import { requestPostPreQuestion } from 'apis/preQuestion';
import { PreQuestionCustomHookType } from 'types/preQuestion';

const usePreQuestion = () => {
  const navigate = useNavigate();
  const preQuestionRef = useRef<Editor>(null);

  const accessToken = localStorage.getItem('accessToken');

  const postPreQuestion = async ({ levellogId, preQuestion }: PreQuestionCustomHookType) => {
    try {
      await requestPostPreQuestion({
        accessToken,
        levellogId,
        preQuestion,
      });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

  const onClickPreQuestionAddButton = ({
    levellogId,
  }: Pick<PreQuestionCustomHookType, 'levellogId'>) => {
    if (preQuestionRef.current) {
      postPreQuestion({
        levellogId,
        preQuestion: preQuestionRef.current.getInstance().getEditorElements().mdEditor.innerText,
      });
    }
  };

  return { preQuestionRef, onClickPreQuestionAddButton };
};

export default usePreQuestion;
