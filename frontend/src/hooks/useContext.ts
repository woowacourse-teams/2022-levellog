import { useContext } from 'react';

import { UserContext, UserDispatchContext } from '../context';

export const useUser = () => {
  const profileUrl = useContext(UserContext);
  const profileUrlDispatch = useContext(UserDispatchContext);

  return { profileUrl, profileUrlDispatch };
};
