import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation, useQuery } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, QUERY_KEY } from 'constants/constants';

import {
  requestEditLevellog,
  requestGetLevellog,
  requestPostLevellog,
  LevellogRequestCommonType,
  LevellogPostRequestType,
  LevellogEditRequestType,
} from 'apis/levellog';
import { teamGetUriBuilder } from 'utils/uri';

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
    ({ teamId, levellog }: Omit<LevellogPostRequestType, 'accessToken'>) => {
      return requestPostLevellog({ accessToken, teamId, levellog });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.LEVELLOG_ADD });
        navigate(teamGetUriBuilder({ teamId }));
      },
    },
  );

  const { mutate: editLevellog } = useMutation(
    ({ teamId, levellogId, levellog }: Omit<LevellogEditRequestType, 'accessToken'>) => {
      return requestEditLevellog({
        accessToken,
        teamId,
        levellogId,
        levellog,
      });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.LEVELLOG_EDIT });
        navigate(teamGetUriBuilder({ teamId }));
      },
    },
  );

  const onClickLevellogAddButton = async ({
    teamId,
  }: Omit<LevellogRequestCommonType, 'accessToken'>) => {
    if (!levellogRef.current) return;
    postLevellog({
      teamId,
      levellog: { content: levellogRef.current.getInstance().getMarkdown() },
    });
  };

  const onClickLevellogEditButton = async ({
    teamId,
    levellogId,
  }: Omit<LevellogEditRequestType, 'accessToken' | 'levellog'>) => {
    if (!levellogRef.current) return;
    editLevellog({
      teamId,
      levellogId,
      levellog: { content: levellogRef.current.getInstance().getMarkdown() },
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
