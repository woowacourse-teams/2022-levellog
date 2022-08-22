import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import useUriBuilder from './useUriBuilder';
import { Editor } from '@toast-ui/react-editor';
import { requestEditLevellog, requestGetLevellog, requestPostLevellog } from 'apis/levellog';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { LevellogCustomHookType, LevellogInfoType } from 'types/levellog';

const useLevellog = () => {
  const { teamGetUriBuilder } = useUriBuilder();
  const [levellogInfo, setLevellogInfo] = useState<LevellogInfoType>(
    {} as unknown as LevellogInfoType,
  );
  const levellogRef = useRef<Editor>(null);
  const navigate = useNavigate();

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
      navigate(teamGetUriBuilder({ teamId }));
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
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
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
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
      alert('레벨로그 수정이 완료되었습니다.');
      navigate(teamGetUriBuilder({ teamId }));
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
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

  const onClickLevellogAddButton = ({ teamId }: Pick<LevellogCustomHookType, 'teamId'>) => {
    if (levellogRef.current) {
      postLevellog({
        teamId,
        inputValue: levellogRef.current.getInstance().getEditorElements().mdEditor.innerText,
      });
      alert(MESSAGE.LEVELLOG_ADD_CONFIRM);
    }
  };

  const onClickLevellogEditButton = ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
    if (levellogRef.current) {
      editLevellog({
        teamId,
        levellogId,
        inputValue: levellogRef.current.getInstance().getEditorElements().mdEditor.innerText,
      });
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
