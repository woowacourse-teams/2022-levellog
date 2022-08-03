import { useState } from 'react';

const useUtil = () => {
  const [throttle, setThrottle] = useState<boolean>(false);

  const isThrottle = () => {
    if (!throttle) {
      setThrottle(true);
      setTimeout(() => {
        setThrottle(false);
      }, 100);
      return false;
    }
    return true;
  };

  return { isThrottle };
};

export default useUtil;
