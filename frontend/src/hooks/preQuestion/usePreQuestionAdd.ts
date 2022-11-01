import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { PreQuestionPostRequestType, requestPostPreQuestion } from 'apis/preQuestion';
import { teamGetUriBuilder } from 'utils/uri';

const usePreQuestionAdd = () => {
  const { showSnackbar } = useSnackbar();
  const preQuestionRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const { teamId, levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: postPreQuestion } = useMutation(
    ({ levellogId, preQuestionContent }: Omit<PreQuestionPostRequestType, 'accessToken'>) => {
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
        errorHandler({ err, showSnackbar });
      },
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
    handleClickPreQuestionAddButton,
  };
};

export default usePreQuestionAdd;
