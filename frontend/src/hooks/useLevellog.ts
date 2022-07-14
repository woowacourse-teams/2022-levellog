import { getLevellog, postLevellog } from '../api/levellog';
import { LevellogType } from '../types';

const useLevellog = () => {
  const stringToLevellog = (inputValue: string) => {
    const levellogContent: LevellogType = {
      content: inputvalue,
    };
    return levellogContent;
  };

  const levellogAdd = async (inputValue: string) => {
    try {
      await postLevellog(stringToLevellog(inputvalue));
    } catch (err) {
      console.log(err);
    }
  };

  const levellogLookup = async (id: number) => {
    try {
      const res = await getLevellog(id);
      const levellog = res.data;

      return levellog;
    } catch (err) {
      console.log(err);
    }
  };
  return { levellogAdd, levellogLookup };
};

export default useLevellog;
