import { useState } from 'react';

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

  return { isDebounce };
};

export default useUtil;
