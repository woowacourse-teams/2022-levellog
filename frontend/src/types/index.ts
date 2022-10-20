export interface UserInfoType {
  id: string;
  nickname: string;
  profileUrl: string;
}

export type ImageSizeType =
  | 'EXCEPTION'
  | 'EXTRA_HUGE'
  | 'HUGE'
  | 'LARGE'
  | 'MEDIUM'
  | 'SMALL'
  | 'TINY';

export interface RequestType {
  accessToken: string | null;
  method: 'get' | 'post' | 'put' | 'delete';
  url: string;
  headers: {
    Authorization: string;
  };
}
