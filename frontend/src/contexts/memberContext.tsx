import { createContext, Dispatch, SetStateAction, useState } from 'react';

import { MemberType } from 'types/member';

interface ParticipantAndWatcherType {
  participants: MemberType[];
  watchers: MemberType[];
}
type MemberDispatchType = Dispatch<SetStateAction<ParticipantAndWatcherType>>;
interface MemberProviderProps {
  children: JSX.Element;
}

const initValue: ParticipantAndWatcherType = {
  participants: [],
  watchers: [],
};

export const MemberContext = createContext<ParticipantAndWatcherType>({
  participants: [],
  watchers: [],
});

export const MemberDispatchContext = createContext<MemberDispatchType>(() => {});

export const MemberProvider = ({ children }: MemberProviderProps) => {
  const [state, setState] = useState(initValue);

  return (
    <MemberContext.Provider value={state}>
      <MemberDispatchContext.Provider value={setState}>{children}</MemberDispatchContext.Provider>
    </MemberContext.Provider>
  );
};
