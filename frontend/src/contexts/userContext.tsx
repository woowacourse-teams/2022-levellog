import { useState, createContext, Dispatch, SetStateAction } from 'react';

import { UserInfoType } from 'types';

type UserDispatchType = Dispatch<SetStateAction<UserInfoType>>;

const userInfo = {
  id: '',
  nickname: '',
  profileUrl: '',
};

export const UserContext = createContext<UserInfoType>(userInfo);
export const UserDispatchContext = createContext<UserDispatchType>(() => {});

export const UserProvider = ({ children }: { children: JSX.Element }) => {
  const [userState, setUserState] = useState<UserInfoType>(userInfo);

  return (
    <UserContext.Provider value={userState}>
      <UserDispatchContext.Provider value={setUserState}>{children}</UserDispatchContext.Provider>
    </UserContext.Provider>
  );
};
