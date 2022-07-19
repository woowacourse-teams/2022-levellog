import { useContext } from 'react';

import { UserContext, UserDispatchContext } from 'contexts/userContext';

const useUser = () => {
  const { id, profileUrl } = useContext(UserContext);
  const loginUserId = id;
  const loginUserProfileUrl = profileUrl;
  const userInfoDispatch = useContext(UserDispatchContext);

  return { loginUserId, loginUserProfileUrl, userInfoDispatch };
};

export default useUser;
