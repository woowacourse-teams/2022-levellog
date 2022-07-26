import { useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { LevellogType } from 'types';

import { ROUTES_PATH } from 'constants/constants';

import {
  requestDeleteLevellog,
  requestEditLevellog,
  requestGetLevellog,
  requestPostLevellog,
} from 'apis/levellog';

const useLevellog = () => {
  const { teamId, levellogId } = useParams();
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

  const postLevellog = async ({ inputValue }: any) => {
    try {
      await requestPostLevellog({
        accessToken,
        teamId,
        levellogContent: stringToLevellog({ inputValue }),
      });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      console.log(err);
      navigate(ROUTES_PATH.HOME);
    }
  };

  const getLevellog = async () => {
    try {
      const res = await requestGetLevellog({ accessToken, teamId, levellogId });
      setLevellog(res.data);
      return res.data;
    } catch (err) {
      console.log(err);
      navigate(ROUTES_PATH.HOME);
    }
  };

  const deleteLevellog = async () => {
    try {
      await requestDeleteLevellog({ accessToken, teamId, levellogId });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      console.log(err);
      navigate(ROUTES_PATH.NOT_FOUND);
    }
  };

  const editLevellog = async ({ inputValue }: any) => {
    try {
      await requestEditLevellog({
        accessToken,
        teamId,
        levellogId,
        levellogContent: stringToLevellog({ inputValue }),
      });
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      console.log(err);
      navigate(ROUTES_PATH.NOT_FOUND);
    }
  };

  const handleSubmitLevellogEditForm = (e: any) => {
    editLevellog({ inputValue: levellogRef.current.value });
  };

  const handleSubmitLevellogPostForm = (e: any) => {
    postLevellog({ inputValue: levellogRef.current.value });
  };

  const getLevellogOnRef = async () => {
    const res = await getLevellog();
    levellogRef.current.value = res.content;
  };

  return {
    levellog,
    levellogRef,
    postLevellog,
    getLevellog,
    editLevellog,
    deleteLevellog,
    getLevellogOnRef,
    handleSubmitLevellogEditForm,
    handleSubmitLevellogPostForm,
  };
};

export default useLevellog;
