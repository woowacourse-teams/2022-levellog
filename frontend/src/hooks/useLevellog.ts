import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import { requestEditLevellog, requestGetLevellog, requestPostLevellog } from 'apis/levellog';
import { LevellogCustomHookType, LevellogFormatType, LevellogInfoType } from 'types/levellog';

const useLevellog = () => {
  const [levellogInfo, setLevellogInfo] = useState<LevellogInfoType>(
    {} as unknown as LevellogInfoType,
  );

  const levellogRef = useRef<Editor>(null);
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  const stringToLevellog = ({
    inputValue,
  }: Pick<LevellogCustomHookType, 'inputValue'>): LevellogFormatType => {
    const levellogContent = {
      content: inputValue,
    };

    return levellogContent;
  };

  const postLevellog = async ({
    teamId,
    inputValue,
  }: Omit<LevellogCustomHookType, 'levellogId'>) => {
    try {
      await requestPostLevellog({
        accessToken,
        teamId,
        levellogContent: stringToLevellog({ inputValue }),
      });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
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
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const editLevellog = async ({ teamId, levellogId, inputValue }: LevellogCustomHookType) => {
    try {
      await requestEditLevellog({
        accessToken,
        teamId,
        levellogId,
        levellogContent: stringToLevellog({ inputValue }),
      });
      alert('레벨로그 수정이 완료되었습니다.');
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
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
      alert('레벨로그 작성이 완료되었습니다.');
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
