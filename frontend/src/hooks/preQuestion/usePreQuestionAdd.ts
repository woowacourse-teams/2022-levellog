import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { requestDeletePreQuestion, requestPostPreQuestion } from 'apis/preQuestion';
import { NotCorrectToken } from 'apis/utils';
import { PreQuestionCustomHookType } from 'types/preQuestion';
import { teamGetUriBuilder } from 'utils/util';

const usePreQuestionAdd = () => {
  const { showSnackbar } = useSnackbar();
  const preQuestionRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const { teamId, levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: postPreQuestion } = useMutation(
    ({
      levellogId,
      preQuestionContent,
    }: Pick<PreQuestionCustomHookType, 'levellogId' | 'preQuestionContent'>) => {
      return requestPostPreQuestion({
        accessToken,
        levellogId,
        preQuestionContent,
      });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.PREQUESTION_ADD });
        navigate(teamGetUriBuilder({ teamId }));
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const { mutate: deletePreQuestion } = useMutation(
    ({
      levellogId,
      preQuestionId,
    }: Pick<PreQuestionCustomHookType, 'levellogId' | 'preQuestionId'>) => {
      return requestDeletePreQuestion({ accessToken, levellogId, preQuestionId });
    },
  );

  const handleClickPreQuestionAddButton = () => {
    if (!preQuestionRef.current) return;
    postPreQuestion({
      levellogId,
      preQuestionContent: preQuestionRef.current?.getInstance().getMarkdown(),
    });
  };

  return {
    preQuestionRef,
    deletePreQuestion,
    handleClickPreQuestionAddButton,
  };
};

export default usePreQuestionAdd;
