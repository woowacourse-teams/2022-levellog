import { useContext } from 'react';

import { UserContext, UserDispatchContext } from '../context';

export const useUserState = () => {
  const userState = useContext(UserContext);

  return userState;
};

export const useUserDispatch = () => {
  const userStateDispatch = useContext(UserDispatchContext);

  return userStateDispatch;
};
