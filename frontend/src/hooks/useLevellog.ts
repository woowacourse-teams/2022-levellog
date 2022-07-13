import { getLevellog, postLevellog } from '../api/levellog';
import { LevellogType } from '../types';

const useLevellog = () => {
  const stringToLevellogFormat = (inputvalue: string) => {
    const levellogContent: LevellogType = {
      content: inputvalue,
    };
    return levellogContent;
  };

  const levellogAdd = async (inputvalue: string) => {
    try {
      await postLevellog(stringToLevellogFormat(inputvalue));
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
  return { stringToLevellogFormat, levellogAdd, levellogLookup };
};

export default useLevellog;
