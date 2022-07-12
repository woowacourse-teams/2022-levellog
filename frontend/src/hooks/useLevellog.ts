import { LevellogType } from '../types';

const useLevellog = () => {
  const stringToLevellogFormat = (inputvalue: string) => {
    const levellogContent: LevellogType = {
      content: inputvalue,
    };
    return levellogContent;
  };

  const levellogAdd = (inputvalue: string) => {
    const levellogContent = stringToLevellogFormat(inputvalue);
  };

  return { stringToLevellogFormat, levellogAdd };
};

export default useLevellog;
