import { fetcher } from './index';
import { AuthorizationHeader } from 'apis/index';

export const requestPostUserLogin = ({ code }: UserLoginPostRequestType) => {
  const LOGIN_POST_URI = '/auth/login';
  const HEADER = { headers: { 'Content-Type': 'application/json;charset=UTF-8' } };
  const data = { authorizationCode: code };

  return fetcher.post(LOGIN_POST_URI, data, HEADER);
};

export const requestGetUserAuthority = ({ accessToken }: UserAuthorityGetRequestType) => {
  const MY_INFO_GET_URI = '/my-info';

  return fetcher.get(MY_INFO_GET_URI, AuthorizationHeader(accessToken));
};

interface UserLoginPostRequestType {
  code: string;
}

interface UserAuthorityGetRequestType {
  accessToken: string | null;
}
