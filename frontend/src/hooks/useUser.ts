import { useContext } from 'react';

import { UserContext, UserDispatchContext } from 'contexts/userContext';

export const useUser = () => {
  const { id, profileUrl } = useContext(UserContext);
  const userInfoDispatch = useContext(UserDispatchContext);

  return { id, profileUrl, userInfoDispatch };
};
