export interface MembersApiType {
  accessToken: string | null;
  nickname: string;
}

export interface MembersCustomHookType {
  nicknameValue: string;
}

export interface MemberType {
  id: string;
  nickname: string;
  profileUrl: string;
}
