import { createContext, Dispatch, SetStateAction, useState } from 'react';

import { InterviewTeamDetailType, InterviewTeamType } from 'types/team';

type InterviewTeamDetailDispatchType = Dispatch<SetStateAction<InterviewTeamDetailType>>;

const teamInfo: InterviewTeamDetailType = {
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
  watchers: [],
  participants: [],
};

export const TeamContext = createContext<InterviewTeamDetailType>(teamInfo);
export const TeamDispatchContext = createContext<InterviewTeamDetailDispatchType>(() => {});

export const TeamProvider = ({ children }: { children: JSX.Element }) => {
  const [teamState, setTeamState] = useState<InterviewTeamDetailType>(teamInfo);

  return (
    <TeamContext.Provider value={teamState}>
      <TeamDispatchContext.Provider value={setTeamState}>{children}</TeamDispatchContext.Provider>
    </TeamContext.Provider>
  );
};
