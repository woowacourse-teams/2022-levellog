export interface RoleApiType {
  teamId: string;
  participantId: string;
  accessToken: string | null;
}

export interface LoginUserRoleType {
  interviewee: boolean;
  interviewer: boolean;
}
