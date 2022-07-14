import { postLevellog } from '../api/levellog';
import { LevellogType } from '../types';

const useLevellog = () => {
  const stringToLevellog = (inputvalue: string) => {
    const levellogContent: LevellogType = {
      content: inputvalue,
    };

    return levellogContent;
  };

  const levellogAdd = async (inputvalue: string) => {
    try {
      await postLevellog(stringToLevellog(inputvalue));
    } catch (err) {
      console.log(err);
    }
  };

  return { levellogAdd };
};

export default useLevellog;
