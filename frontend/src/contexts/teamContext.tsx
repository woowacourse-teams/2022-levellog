import { createContext, Dispatch, SetStateAction, useState } from 'react';

import { InterviewTeamType } from 'types/team';

type InterviewTeamDispatchType = Dispatch<SetStateAction<InterviewTeamType>>;

const teamInfo: InterviewTeamType = {
  id: '',
  title: '',
  place: '',
  startAt: '',
  teamImage: '',
  hostId: '',
  status: '',
  isParticipant: false,
  interviewerNumber: 0,
  interviewers: [],
  interviewees: [],
  participants: [],
  watchers: [],
};

export const TeamContext = createContext<InterviewTeamType>(teamInfo);
export const TeamDispatchContext = createContext<InterviewTeamDispatchType>(() => {});

export const TeamProvider = ({ children }: { children: JSX.Element }) => {
  const [teamState, setTeamState] = useState<InterviewTeamType>(teamInfo);

  return (
    <TeamContext.Provider value={teamState}>
      <TeamDispatchContext.Provider value={setTeamState}>{children}</TeamDispatchContext.Provider>
    </TeamContext.Provider>
  );
};
