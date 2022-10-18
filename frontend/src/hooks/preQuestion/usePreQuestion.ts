import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation, useQuery } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import { PreQuestionCustomHookType } from 'hooks/preQuestion/types/index';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import {
  requestDeletePreQuestion,
  requestEditPreQuestion,
  requestGetPreQuestion,
  requestPostPreQuestion,
} from 'apis/preQuestion';
import { teamGetUriBuilder } from 'utils/uri';

const QUERY_KEY = {
  PREQUESTION: 'preQuestion',
};

const usePreQuestion = () => {
  const { showSnackbar } = useSnackbar();
  const preQuestionRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const { levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { isError: preQuestionError, data: preQuestion } = useQuery(
    [QUERY_KEY.PREQUESTION, accessToken, levellogId],
    () =>
      requestGetPreQuestion({
        accessToken,
        levellogId,
      }),
  );

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
  );

  const { mutate: editPreQuestion } = useMutation(
    ({
      levellogId,
      preQuestionId,
      preQuestionContent,
    }: Omit<PreQuestionCustomHookType, 'preQuestion'>) => {
      return requestEditPreQuestion({
        accessToken,
        levellogId,
        preQuestionId,
        preQuestionContent,
      });
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

  const getPreQuestionOnRef = () => {
    if (!preQuestion) return;

    if (typeof preQuestion.content === 'string' && preQuestionRef.current) {
      preQuestionRef.current.getInstance().setMarkdown(preQuestion.content);
    }
  };

  const onClickPreQuestionAddButton = async ({
    teamId,
    levellogId,
  }: Pick<PreQuestionCustomHookType, 'teamId' | 'levellogId'>) => {
    if (!preQuestionRef.current) return;

    postPreQuestion({
      levellogId,
      preQuestionContent: preQuestionRef.current.getInstance().getMarkdown(),
    });

    showSnackbar({ message: MESSAGE.PREQUESTION_ADD });
    navigate(teamGetUriBuilder({ teamId }));
  };

  const onClickPreQuestionEditButton = async ({
    teamId,
    levellogId,
    preQuestionId,
  }: Pick<PreQuestionCustomHookType, 'teamId' | 'levellogId' | 'preQuestionId'>) => {
    if (!preQuestionRef.current) return;

    editPreQuestion({
      teamId,
      levellogId,
      preQuestionId,
      preQuestionContent: preQuestionRef.current.getInstance().getMarkdown(),
    });

    showSnackbar({ message: MESSAGE.PREQUESTION_EDIT });
    navigate(teamGetUriBuilder({ teamId }));
  };

  return {
    preQuestionError,
    preQuestion,
    preQuestionRef,
    deletePreQuestion,
    getPreQuestionOnRef,
    onClickPreQuestionAddButton,
    onClickPreQuestionEditButton,
  };
};

export default usePreQuestion;
