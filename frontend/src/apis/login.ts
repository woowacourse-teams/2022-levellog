import axios from 'axios';

import { API_URL } from 'constants/constants';

export const getUserLogin = ({ code }: any) => {
  return axios({
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    url: `${API_URL}/auth/login`,
    data: {
      authorizationCode: code,
    },
  });
};

export const getUserAuthority = ({ accessToken }: any) => {
  return axios({
    method: 'get',
    url: `${API_URL}/myInfo`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
