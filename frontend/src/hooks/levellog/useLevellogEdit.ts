import { useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useLevellogQuery from 'hooks/levellog/useLevellogQuery';
import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { requestEditLevellog } from 'apis/levellog';
import { LevellogCustomHookType } from 'types/levellog';
import { debounce, teamGetUriBuilder } from 'utils/util';

const useLevellogEdit = () => {
  const { levellogInfo } = useLevellogQuery();
  const { showSnackbar } = useSnackbar();
  const levellogRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const { teamId, levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: editLevellog } = useMutation(
    ({ teamId, levellogId, inputValue }: LevellogCustomHookType) => {
      return requestEditLevellog({
        accessToken,
        teamId,
        levellogId,
        levellogContent: { content: inputValue },
      });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.LEVELLOG_EDIT });
        navigate(teamGetUriBuilder({ teamId }));
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const handleClickLevellogEditButton = async () => {
    if (typeof teamId !== 'string' || typeof levellogId !== 'string') {
      showSnackbar({ message: MESSAGE.WRONG_ACCESS });
    }

    if (levellogRef.current) {
      debounce.action({
        func: editLevellog,
        args: {
          teamId,
          levellogId,
          inputValue: levellogRef.current.getInstance().getMarkdown(),
        },
      });
    }
  };

  useEffect(() => {
    if (levellogInfo && levellogRef.current) {
      levellogRef.current.getInstance().setMarkdown(levellogInfo.content);
    }
  }, [levellogRef]);

  return { levellogRef, handleClickLevellogEditButton };
};

export default useLevellogEdit;
