import { useNavigate } from 'react-router-dom';

import { LevellogType } from 'types';

import { ROUTES_PATH } from 'constants/constants';

import { deleteLevellog, getLevellog, postLevellog, modifyLevellog } from 'apis/levellog';

const useLevellog = () => {
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  const stringToLevellog = (inputValue: string) => {
    const levellogContent: LevellogType = {
      content: inputValue,
    };
    return levellogContent;
  };

  const levellogAdd = async (teamId: string, inputValue: string) => {
    try {
      await postLevellog(accessToken, teamId, stringToLevellog(inputValue));
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      console.log(err);
      navigate(ROUTES_PATH.HOME);
    }
  };

  const levellogLookup = async (teamId: string, id: string) => {
    try {
      const res = await getLevellog(accessToken, teamId, id);
      const levellog = res.data;

      return levellog;
    } catch (err) {
      console.log(err);
      navigate(ROUTES_PATH.HOME);
    }
  };

  const levellogDelete = async (teamId: string, id: string) => {
    try {
      await deleteLevellog(accessToken, teamId, id);
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      console.log(err);
      navigate(ROUTES_PATH.NOT_FOUND);
    }
  };

  const levellogModify = async (teamId: string, id: string, inputValue: string) => {
    try {
      await modifyLevellog(accessToken, teamId, id, stringToLevellog(inputValue));
      navigate(`${ROUTES_PATH.INTERVIEW_TEAMS}/${teamId}`);
    } catch (err) {
      console.log(err);
      navigate(ROUTES_PATH.NOT_FOUND);
    }
  };

  return { levellogAdd, levellogLookup, levellogModify, levellogDelete };
};

export default useLevellog;
