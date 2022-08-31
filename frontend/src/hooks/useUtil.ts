import { useState } from 'react';

import { Team } from 'types/team';

const useUtil = () => {
  const [debounce, setDebounce] = useState<boolean>(false);

  const isDebounce = () => {
    if (debounce) {
      setDebounce(false);
      setTimeout(() => {
        setDebounce(true);
      }, 100);
      return true;
    }
    return false;
  };

  const convertDateAndTime = ({ startAt }: Pick<Team, 'startAt'>) => {
    const year = startAt.slice(0, 4);
    const month = startAt.slice(5, 7);
    const day = startAt.slice(8, 10);
    const time = `${startAt.slice(11, 13)}시 ${startAt.slice(14, 16)}분`;

    return `${year}년 ${month}월 ${day}일 ${time}`;
  };

  return { isDebounce, convertDateAndTime };
};

export default useUtil;
