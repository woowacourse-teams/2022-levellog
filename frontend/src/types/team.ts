export interface TeamApiType {
  teamId: string;
  teamInfo: TeamCustomHookType;
  accessToken: string | null;
}

export interface TeamEditApiType {
  teamId: string;
  teamInfo: Pick<TeamCustomHookType, 'title' | 'place' | 'startAt'>;
  accessToken: string | null;
}

export interface TeamCustomHookType {
  title: string;
  place: string;
  startAt: string;
  interviewerNumber: string;
  participants: {
    ids: Array<string>;
  };
}

export interface Team {
  title: string;
  place: string;
  startAt: string;
}

export interface InterviewTeamType {
  id: string;
  title: string;
  place: string;
  startAt: string;
  teamImage: string;
  hostId: string;
  isClosed: Boolean;
  // status: string;
  isParticipant: Boolean;
  interviewers: Array<number | null>;
  interviewees: Array<number | null>;
  participants: ParticipantType[];
}

export interface ParticipantType {
  memberId: string;
  levellogId: string;
  nickname: string;
  profileUrl: string;
  preQuestionId: string;
}

export interface ParticipantSmallType {
  id: string;
  nickname: string;
  profileUrl: string;
}
