import { useState, createContext, Dispatch, SetStateAction } from 'react';

import { UserType } from 'types';

const userInfo = {
  id: '',
  nickname: '',
  profileUrl: '',
};

export const UserContext = createContext<UserType>(userInfo);
export const UserDispatchContext = createContext<UserDispatchType>(() => {});

export const UserProvider = ({ children }: { children: JSX.Element }) => {
  const [userState, setUserState] = useState<UserType>(userInfo);

  return (
    <UserContext.Provider value={userState}>
      <UserDispatchContext.Provider value={setUserState}>{children}</UserDispatchContext.Provider>
    </UserContext.Provider>
  );
};

type UserDispatchType = Dispatch<SetStateAction<UserType>>;
