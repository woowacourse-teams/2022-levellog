import { postLevellog } from '../api/levellog';
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

  return { stringToLevellogFormat, levellogAdd };
};

export default useLevellog;
