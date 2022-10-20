import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { requestPostLevellog } from 'apis/levellog';
import { LevellogCustomHookType } from 'types/levellog';
import { debounce, teamGetUriBuilder } from 'utils/util';

const useLevellogAdd = () => {
  const { showSnackbar } = useSnackbar();
  const levellogRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const { teamId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: postLevellog } = useMutation(
    ({ teamId, inputValue }: Omit<LevellogCustomHookType, 'levellogId'>) => {
      return requestPostLevellog({ accessToken, teamId, levellogContent: { content: inputValue } });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.LEVELLOG_ADD });
        navigate(teamGetUriBuilder({ teamId }));
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const handleClickLevellogAddButton = () => {
    if (typeof teamId !== 'string') {
      showSnackbar({ message: MESSAGE.WRONG_ACCESS });
      return;
    }

    if (levellogRef.current) {
      debounce.action({
        func: postLevellog,
        args: { teamId, inputValue: levellogRef.current.getInstance().getMarkdown() },
      });
    }
  };

  return {
    levellogRef,
    handleClickLevellogAddButton,
  };
};

export default useLevellogAdd;
