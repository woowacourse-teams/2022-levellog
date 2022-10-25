import { ParticipantType } from 'types';

export interface InterviewTeamType {
  id: string;
  title: string;
  place: string;
  startAt: string;
  teamImage: string;
  status: TeamStatusType;
  participants: Pick<ParticipantType, 'memberId' | 'profileUrl' | 'nickname'>[];
}

export interface InterviewTeamDetailType extends InterviewTeamType {
  hostId: string;
  isParticipant: boolean;
  interviewerNumber: number;
  interviewers: Array<number | null>;
  interviewees: Array<number | null>;
  watchers: ParticipantType[];
  participants: ParticipantType[];
}

export interface TeamRequestType {
  title: string;
  place: string;
  startAt: string;
  interviewerNumber: number;
  watcherIds: string[];
  participantIds: string[];
}

export interface TeamConditionsType {
  open: boolean;
  close: boolean;
  my: boolean;
}

export type TeamStatusType = 'READY' | 'IN_PROGRESS' | 'CLOSED' | '';
export type TeamsConditionType = 'open' | 'close' | 'my';
