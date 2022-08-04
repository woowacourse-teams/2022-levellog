export interface RoleApiType {
  teamId: string;
  participantId: string;
  accessToken: string | null;
}

export interface RoleType {
  interviewee: boolean;
  interviewer: boolean;
}
