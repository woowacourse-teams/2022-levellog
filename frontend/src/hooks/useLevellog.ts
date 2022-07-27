import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { LevellogType } from 'types';

import { ROUTES_PATH } from 'constants/constants';

import {
  requestDeleteLevellog,
  requestEditLevellog,
  requestGetLevellog,
  requestPostLevellog,
} from 'apis/levellog';

const useLevellog = () => {
  const [levellog, setLevellog] = useState('');
  const levellogRef = useRef(null);
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  const stringToLevellog = ({ inputValue }: any) => {
    const levellogContent: LevellogType = {
      content: inputValue,
    };
    return levellogContent;
  };

  const postLevellog = async ({ teamId, inputValue }: any) => {
    try {
      await requestPostLevellog({
        accessToken,
        teamId,
        levellogContent: stringToLevellog({ inputValue }),
      });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
      navigate(ROUTES_PATH.HOME);
    }
  };

  const getLevellog = async ({ teamId, levellogId }) => {
    try {
      const res = await requestGetLevellog({ accessToken, teamId, levellogId });
      setLevellog(res.data.content);
      return res.data.content;
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
      navigate(ROUTES_PATH.HOME);
    }
  };

  const deleteLevellog = async ({ teamId, levellogId }) => {
    try {
      await requestDeleteLevellog({ accessToken, teamId, levellogId });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
      navigate(ROUTES_PATH.NOT_FOUND);
    }
  };

  const editLevellog = async ({ teamId, levellogId, inputValue }: any) => {
    try {
      await requestEditLevellog({
        accessToken,
        teamId,
        levellogId,
        levellogContent: stringToLevellog({ inputValue }),
      });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
      navigate(ROUTES_PATH.NOT_FOUND);
    }
  };

  const getLevellogOnRef = async ({ teamId, levellogId }) => {
    const levellog = await getLevellog({ teamId, levellogId });
    levellogRef.current.value = levellog;
  };

  const onSubmitLevellogEditForm = ({ teamId, levellogId }) => {
    editLevellog({ teamId, levellogId, inputValue: levellogRef.current.value });
  };

  const onSubmitLevellogPostForm = ({ teamId }) => {
    postLevellog({ teamId, inputValue: levellogRef.current.value });
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
