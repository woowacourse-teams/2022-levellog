export interface MembersApiType {
  accessToken: string | null;
  nickname: string;
}

export interface MembersCustomHookType {
  nickname: string;
}

export interface MemberType {
  id: string;
  nickname: string;
  profileUrl: string;
}
