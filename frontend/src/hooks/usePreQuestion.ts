import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useSnackbar from 'hooks/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import {
  requestDeletePreQuestion,
  requestEditPreQuestion,
  requestGetPreQuestion,
  requestPostPreQuestion,
} from 'apis/preQuestion';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { PreQuestionCustomHookType, PreQuestionFormatType } from 'types/preQuestion';
import { teamGetUriBuilder } from 'utils/util';

const usePreQuestion = () => {
  const { showSnackbar } = useSnackbar();
  const [preQuestion, setPreQuestion] = useState<PreQuestionFormatType>(
    {} as unknown as PreQuestionFormatType,
  );
  const preQuestionRef = useRef<Editor>(null);
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const getPreQuestion = async ({ levellogId }: Pick<PreQuestionCustomHookType, 'levellogId'>) => {
    try {
      const res = await requestGetPreQuestion({ levellogId, accessToken });
      setPreQuestion(res.data);

      return res.data;
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const postPreQuestion = async ({
    teamId,
    levellogId,
    preQuestionContent,
  }: Pick<PreQuestionCustomHookType, 'teamId' | 'levellogId' | 'preQuestionContent'>) => {
    try {
      await requestPostPreQuestion({
        accessToken,
        levellogId,
        preQuestionResult: { content: preQuestionContent },
      });

      return true;
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
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

      return true;
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const editPreQuestion = async ({
    levellogId,
    preQuestionId,
    preQuestionContent,
  }: Omit<PreQuestionCustomHookType, 'preQuestion'>) => {
    try {
      await requestEditPreQuestion({
        accessToken,
        levellogId,
        preQuestionId,
        preQuestionResult: { content: preQuestionContent },
      });

      return true;
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const getPreQuestionOnRef = async ({
    levellogId,
  }: Pick<PreQuestionCustomHookType, 'levellogId'>) => {
    const preQuestion = await getPreQuestion({ levellogId });

    if (!preQuestion) return;

    if (typeof preQuestion.content === 'string') {
      setPreQuestion(preQuestion);
    }
    if (typeof preQuestion.content === 'string' && preQuestionRef.current) {
      preQuestionRef.current.getInstance().setMarkdown(preQuestion.content);
    }
  };

  const onClickPreQuestionAddButton = async ({
    teamId,
    levellogId,
  }: Pick<PreQuestionCustomHookType, 'teamId' | 'levellogId'>) => {
    if (!preQuestionRef.current) return;

    if (
      await postPreQuestion({
        teamId,
        levellogId,
        preQuestionContent: preQuestionRef.current.getInstance().getMarkdown(),
      })
    ) {
      showSnackbar({ message: MESSAGE.PREQUESTION_ADD });
      navigate(teamGetUriBuilder({ teamId }));
    }
  };

  const onClickPreQuestionEditButton = async ({
    teamId,
    levellogId,
    preQuestionId,
  }: Pick<PreQuestionCustomHookType, 'teamId' | 'levellogId' | 'preQuestionId'>) => {
    if (!preQuestionRef.current) return;

    if (
      await editPreQuestion({
        teamId,
        levellogId,
        preQuestionId,
        preQuestionContent: preQuestionRef.current.getInstance().getMarkdown(),
      })
    ) {
      showSnackbar({ message: MESSAGE.PREQUESTION_EDIT });
      navigate(teamGetUriBuilder({ teamId }));
    }
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
