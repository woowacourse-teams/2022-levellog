import { useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useLevellogQuery from 'hooks/levellog/useLevellogQuery';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { requestEditLevellog } from 'apis/levellog';
import { LevellogCustomHookType } from 'types/levellog';
import { teamGetUriBuilder } from 'utils/util';

const useLevellogEdit = () => {
  const { levellogRefetch, levellogInfo } = useLevellogQuery();
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
    },
  );

  const handleClickLevellogEditButton = async () => {
    if (typeof teamId !== 'string' || typeof levellogId !== 'string') {
      showSnackbar({ message: MESSAGE.WRONG_ACCESS });
    }

    if (!levellogRef.current) return;
    editLevellog({
      teamId,
      levellogId,
      inputValue: levellogRef.current.getInstance().getMarkdown(),
    });
  };

  useEffect(() => {
    if (levellogInfo && levellogRef.current) {
      levellogRef.current.getInstance().setMarkdown(levellogInfo.content);
    }
  }, [levellogRef]);

  return { levellogInfo, levellogRef, levellogRefetch, handleClickLevellogEditButton };
};

export default useLevellogEdit;
