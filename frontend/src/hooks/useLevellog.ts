import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useSnackbar from 'hooks/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import { requestEditLevellog, requestGetLevellog, requestPostLevellog } from 'apis/levellog';
import { NotCorrectToken } from 'apis/utils';
import { LevellogCustomHookType, LevellogInfoType } from 'types/levellog';
import { teamGetUriBuilder } from 'utils/util';

const useLevellog = () => {
  const { showSnackbar } = useSnackbar();
  const levellogRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const [levellogInfo, setLevellogInfo] = useState<LevellogInfoType>(
    {} as unknown as LevellogInfoType,
  );
  const accessToken = localStorage.getItem('accessToken');

  const postLevellog = async ({
    teamId,
    inputValue,
  }: Omit<LevellogCustomHookType, 'levellogId'>) => {
    try {
      await requestPostLevellog({
        accessToken,
        teamId,
        levellogContent: { content: inputValue },
      });

      return true;
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const getLevellog = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>): Promise<LevellogInfoType | undefined> => {
    try {
      const res = await requestGetLevellog({ accessToken, teamId, levellogId });
      if ('content' in res.data) {
        setLevellogInfo(res.data);
      }

      return res.data;
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const editLevellog = async ({ teamId, levellogId, inputValue }: LevellogCustomHookType) => {
    try {
      await requestEditLevellog({
        accessToken,
        teamId,
        levellogId,
        levellogContent: { content: inputValue },
      });

      return true;
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const getLevellogOnRef = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
    const levellogInfo = await getLevellog({ teamId, levellogId });
    if (levellogInfo && levellogRef.current) {
      levellogRef.current.getInstance().setMarkdown(levellogInfo.content);
    }
  };

  const onClickLevellogAddButton = async ({ teamId }: Pick<LevellogCustomHookType, 'teamId'>) => {
    if (!levellogRef.current) return;

    if (
      await postLevellog({
        teamId,
        inputValue: levellogRef.current.getInstance().getMarkdown(),
      })
    ) {
      showSnackbar({ message: MESSAGE.LEVELLOG_ADD });
      navigate(teamGetUriBuilder({ teamId }));
    }
  };

  const onClickLevellogEditButton = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
    if (!levellogRef.current) return;

    if (
      await editLevellog({
        teamId,
        levellogId,
        inputValue: levellogRef.current.getInstance().getMarkdown(),
      })
    ) {
      showSnackbar({ message: MESSAGE.LEVELLOG_EDIT });
      navigate(teamGetUriBuilder({ teamId }));
    }
  };

  return {
    levellogInfo,
    levellogRef,
    postLevellog,
    getLevellog,
    editLevellog,
    getLevellogOnRef,
    onClickLevellogAddButton,
    onClickLevellogEditButton,
  };
};

export default useLevellog;
