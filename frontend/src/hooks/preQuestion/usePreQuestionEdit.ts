import { useRef, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import usePreQuestionQuery from './usePreQuestionQuery';
import { requestEditPreQuestion } from 'apis/preQuestion';
import { PreQuestionCustomHookType } from 'types/preQuestion';
import { teamGetUriBuilder } from 'utils/util';

const usePreQuestionEdit = () => {
  const { preQuestion } = usePreQuestionQuery();
  const { showSnackbar } = useSnackbar();
  const preQuestionRef = useRef<Editor>(null);
  const { teamId, levellogId, preQuestionId } = useParams();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: editPreQuestion } = useMutation(
    ({
      levellogId,
      preQuestionId,
      preQuestionContent,
    }: Omit<PreQuestionCustomHookType, 'teamId' | 'preQuestion'>) => {
      return requestEditPreQuestion({
        accessToken,
        levellogId,
        preQuestionId,
        preQuestionContent,
      });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.PREQUESTION_EDIT });
        navigate(teamGetUriBuilder({ teamId }));
      },
    },
  );

  const handleClickPreQuestionEditButton = () => {
    if (!preQuestionRef.current) return;

    editPreQuestion({
      levellogId,
      preQuestionId,
      preQuestionContent: preQuestionRef.current.getInstance().getMarkdown(),
    });
  };

  useEffect(() => {
    if (preQuestion && preQuestionRef.current) {
      preQuestionRef.current.getInstance().setMarkdown(preQuestion.content);
    }
  }, [preQuestionRef]);

  return { preQuestionRef, handleClickPreQuestionEditButton };
};

export default usePreQuestionEdit;
