import { useState, createContext, Dispatch, SetStateAction } from 'react';

type UserDispatchType = Dispatch<SetStateAction<string>>;

const profileUrl: string = '';

export const UserContext = createContext<string>('');
export const UserDispatchContext = createContext<UserDispatchType>(() => {});

export const UserProvider = ({ children }: { children: JSX.Element }) => {
  const [userState, setUserState] = useState(profileUrl);

  return (
    <UserContext.Provider value={userState}>
      <UserDispatchContext.Provider value={setUserState}>{children}</UserDispatchContext.Provider>
    </UserContext.Provider>
  );
};
