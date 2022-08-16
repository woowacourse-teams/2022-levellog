import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import { requestEditLevellog, requestGetLevellog, requestPostLevellog } from 'apis/levellog';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { LevellogCustomHookType } from 'types/levellog';

const useLevellog = () => {
  const [levellog, setLevellog] = useState('');
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
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
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
  }: Omit<LevellogCustomHookType, 'inputValue'>): Promise<string | void> => {
    try {
      const res = await requestGetLevellog({ accessToken, teamId, levellogId });
      setLevellog(res.data.content);

      return res.data.content;
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
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
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
    const levellog = await getLevellog({ teamId, levellogId });
    if (typeof levellog === 'string') {
      setLevellog(levellog);
    }
    if (typeof levellog === 'string' && levellogRef.current) {
      levellogRef.current.getInstance().setMarkdown(levellog);
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
    levellog,
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
