import { createContext, Dispatch, SetStateAction, useState } from 'react';

import { InterviewTeamType } from 'types/team';

type InterviewTeamDispatchType = Dispatch<SetStateAction<InterviewTeamType>>;

const currentTeam: InterviewTeamType = {
  id: '',
  title: '',
  place: '',
  startAt: '',
  teamImage: '',
  hostId: '',
  isClosed: false,
  // status: '', // READY, IN_PROGRESS, CLOSED
  isParticipant: false,
  interviewers: [],
  interviewees: [],
  participants: [],
};

export const TeamContext = createContext<InterviewTeamType>(currentTeam);
export const TeamDispatchContext = createContext<InterviewTeamDispatchType>(() => {});

export const TeamProvider = ({ children }: { children: JSX.Element }) => {
  const [teamState, setTeamState] = useState<InterviewTeamType>(currentTeam);

  return (
    <TeamContext.Provider value={teamState}>
      <TeamDispatchContext.Provider value={setTeamState}>{children}</TeamDispatchContext.Provider>
    </TeamContext.Provider>
  );
};
