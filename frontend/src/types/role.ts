export interface RoleApiType {
  accessToken: string | null;
  teamId: string | undefined;
  participantId: string | undefined;
}

export interface RoleType {
  interviewee: boolean;
  interviewer: boolean;
}

export interface RoleCustomHookType {
  teamId: string;
  levellogId: string;
  participantId: string;
}

export interface MyRoleType {
  myRole: 'INTERVIEWER' | 'OBSERVER';
}
