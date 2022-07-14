import { useContext } from 'react';

import { UserContext, UserDispatchContext } from '../context';

export const useUserState = () => {
  const state = useContext(UserContext);

  return state;
};

export const useUserDispatch = () => {
  const dispatch = useContext(UserDispatchContext);

  return dispatch;
};
