import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import {
  requestDeleteLevellog,
  requestEditLevellog,
  requestGetLevellog,
  requestPostLevellog,
} from 'apis/levellog';
import { LevellogCustomHookType, LevellogFormatType } from 'types/levellog';

const useLevellog = () => {
  const [levellog, setLevellog] = useState('');
  const levellogRef = useRef<HTMLInputElement>(null);
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
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

  const getLevellog = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>): Promise<string | void> => {
    try {
      const res = await requestGetLevellog({ accessToken, teamId, levellogId });
      setLevellog(res.data.content);

      return res.data.content;
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

  const deleteLevellog = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
    try {
      await requestDeleteLevellog({ accessToken, teamId, levellogId });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
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
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

  const getLevellogOnRef = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
    const levellog = await getLevellog({ teamId, levellogId });

    if (typeof levellog === 'string' && levellogRef.current) {
      levellogRef.current.value = levellog;
    }
  };

  const onSubmitLevellogEditForm = ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
    if (levellogRef.current) {
      editLevellog({ teamId, levellogId, inputValue: levellogRef.current.value });
    }
  };

  const onSubmitLevellogPostForm = ({ teamId }: Pick<LevellogCustomHookType, 'teamId'>) => {
    if (levellogRef.current) {
      postLevellog({ teamId, inputValue: levellogRef.current.value });
    }
  };

  return {
    levellog,
    levellogRef,
    postLevellog,
    getLevellog,
    editLevellog,
    deleteLevellog,
    getLevellogOnRef,
    onSubmitLevellogEditForm,
    onSubmitLevellogPostForm,
  };
};

export default useLevellog;
