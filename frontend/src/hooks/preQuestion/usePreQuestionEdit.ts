import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import usePreQuestionQuery from './usePreQuestionQuery';
import { PreQuestionEditRequestType, requestEditPreQuestion } from 'apis/preQuestion';
import { teamGetUriBuilder } from 'utils/uri';

const usePreQuestionEdit = () => {
  const { preQuestionError, preQuestionSuccess, preQuestion } = usePreQuestionQuery();
  const { showSnackbar } = useSnackbar();
  const preQuestionRef = useRef<Editor>(null);
  const { teamId, levellogId, preQuestionId } = useParams();
  const navigate = useNavigate();

  if (preQuestionSuccess) {
    if (preQuestionRef) {
      // 사전 질문 ref에 넣어주기
      // preQuestionRef.current.getInstance().setMarkdown(preQuestion?.content);
    }
    preQuestionRef.current;
  }

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: editPreQuestion } = useMutation(
    ({
      levellogId,
      preQuestionId,
      preQuestionContent,
    }: Omit<PreQuestionEditRequestType, 'accessToken'>) => {
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

  return { preQuestionRef, handleClickPreQuestionEditButton };
};

export default usePreQuestionEdit;
