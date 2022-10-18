import axios from 'axios';

export const requestGetUserLogin = ({ code }: Pick<LoginApiType, 'code'>) => {
  return axios({
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    url: `${process.env.API_URI}/auth/login`,
    data: {
      authorizationCode: code,
    },
  });
};

export const requestGetUserAuthority = ({ accessToken }: Pick<LoginApiType, 'accessToken'>) => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/my-info`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export interface LoginApiType {
  code: string;
  accessToken: string;
}
