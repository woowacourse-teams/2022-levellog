import { MemberType } from 'types/member';

export interface TeamSubmitType {
  watchers: MemberType[];
  participants: MemberType[];
}

// 도대체 어디서 씀?
// export interface Team {
//   title: string;
//   place: string;
//   startAt: string;
// }

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
  isParticipant: Boolean;
  interviewerNumber: number;
  interviewers: Array<number | null>;
  interviewees: Array<number | null>;
  watchers: ParticipantType[];
  participants: ParticipantType[];
}

export interface ParticipantType {
  memberId: string;
  levellogId: string;
  preQuestionId: string;
  nickname: string;
  profileUrl: string;
}

export interface TeamConditionsType {
  open: boolean;
  close: boolean;
  my: boolean;
}

export type TeamStatusType = 'READY' | 'IN_PROGRESS' | 'CLOSED' | '';
export type TeamsConditionType = 'open' | 'close' | 'my';
