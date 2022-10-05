import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation, useQuery } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useSnackbar from 'hooks/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { requestEditLevellog, requestGetLevellog, requestPostLevellog } from 'apis/levellog';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { LevellogCustomHookType, LevellogInfoType } from 'types/levellog';
import { teamGetUriBuilder } from 'utils/util';

const QUERY_KEY = {
  LEVELLOG: 'levellog',
};

const useLevellog = () => {
  const { showSnackbar } = useSnackbar();
  const levellogRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');

  const { teamId, levellogId } = useParams();

  const { isError: levellogError, data: levellogInfo } = useQuery(
    [QUERY_KEY.LEVELLOG, accessToken, teamId, levellogId],
    () =>
      requestGetLevellog({
        accessToken,
        teamId,
        levellogId,
      }),
    {
      onSuccess: () => {
        if (levellogInfo && levellogRef.current) {
          levellogRef.current.getInstance().setMarkdown(levellogInfo.content);
        }
      },
    },
  );

  const { mutate: postLevellog } = useMutation(
    ({ teamId, inputValue }: Omit<LevellogCustomHookType, 'levellogId'>) => {
      return requestPostLevellog({ accessToken, teamId, levellogContent: { content: inputValue } });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.LEVELLOG_ADD });
        navigate(teamGetUriBuilder({ teamId }));
      },
    },
  );

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

  const onClickLevellogAddButton = async ({ teamId }: Pick<LevellogCustomHookType, 'teamId'>) => {
    if (!levellogRef.current) return;
    postLevellog({ teamId, inputValue: levellogRef.current.getInstance().getMarkdown() });
  };

  const onClickLevellogEditButton = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
    if (!levellogRef.current) return;
    editLevellog({
      teamId,
      levellogId,
      inputValue: levellogRef.current.getInstance().getMarkdown(),
    });
  };

  return {
    levellogError,
    levellogInfo,
    levellogRef,
    postLevellog,
    editLevellog,
    onClickLevellogAddButton,
    onClickLevellogEditButton,
  };
};

export default useLevellog;
