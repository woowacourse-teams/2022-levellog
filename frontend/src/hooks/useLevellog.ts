import { LevellogType } from 'types';

import { getLevellog, postLevellog } from 'apis/levellog';

const useLevellog = () => {
  const accessToken = localStorage.getItem('accessToken');

  const stringToLevellog = (inputValue: string) => {
    const levellogContent: LevellogType = {
      content: inputValue,
    };
    return levellogContent;
  };

  const levellogAdd = async (teamId: string, inputValue: string) => {
    try {
      await postLevellog(accessToken, teamId, stringToLevellog(inputValue));
    } catch (err) {
      console.log(err);
    }
  };

  const levellogLookup = async (teamId: string, id: string) => {
    try {
      const res = await getLevellog(accessToken, teamId, id);
      const levellog = res.data;

      return levellog;
    } catch (err) {
      console.log(err);
    }
  };

  return { levellogAdd, levellogLookup };
};

export default useLevellog;
