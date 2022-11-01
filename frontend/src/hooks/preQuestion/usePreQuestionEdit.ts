import { useRef, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation, useQuery } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, QUERY_KEY } from 'constants/constants';

import {
  PreQuestionEditRequestType,
  requestEditPreQuestion,
  requestGetPreQuestion,
} from 'apis/preQuestion';
import { teamGetUriBuilder } from 'utils/uri';

const usePreQuestionEdit = () => {
  const { showSnackbar } = useSnackbar();
  const preQuestionRef = useRef<Editor>(null);
  const { teamId, levellogId, preQuestionId } = useParams();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const { data: preQuestion } = useQuery([QUERY_KEY.PRE_QUESTION, accessToken, levellogId], () =>
    requestGetPreQuestion({
      accessToken,
      levellogId,
    }),
  );

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
      onError: (err) => {
        errorHandler({ err, showSnackbar });
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
