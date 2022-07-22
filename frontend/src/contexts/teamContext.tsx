import { createContext, useState } from 'react';

import { InterviewTeamType } from 'types/index';

// type TeamDispatchType = Dispatch<SetStateAction<InterviewTeamType>>;

const teamInfo = {
  id: '',
  title: '',
  place: '',
  startAt: '',
  teamImage: '',
  hostId: '',
  participants: [],
};

// TeamDispatchContext 타입 고치기
export const TeamContext = createContext<InterviewTeamType>(null);
export const TeamDispatchContext = createContext<any>(() => {});

export const TeamProvider = ({ children }: { children: JSX.Element }) => {
  const [teamState, setTeamState] = useState(teamInfo);

  return (
    <TeamContext.Provider value={teamState}>
      <TeamDispatchContext.Provider value={setTeamState}>{children}</TeamDispatchContext.Provider>
    </TeamContext.Provider>
  );
};
