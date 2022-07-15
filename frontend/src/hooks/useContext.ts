import { useContext } from 'react';

import { UserContext, UserDispatchContext } from '../contexts';

export const useUser = () => {
  const profileUrl = useContext(UserContext);
  const profileUrlDispatch = useContext(UserDispatchContext);

  return { profileUrl, profileUrlDispatch };
};
