import { useEffect } from 'react';

import { MESSAGE } from 'constants/constants';

const usePreventGoBack = () => {
  useEffect(() => {
    const preventGoBack = () => {
      if (confirm(MESSAGE.ESCAPE_NOW_PAGE)) {
        window.location.replace(location.href);
      }
    };
    window.addEventListener('popstate', preventGoBack);

    return () => window.removeEventListener('popstate', preventGoBack);
  }, []);
};

export default usePreventGoBack;
