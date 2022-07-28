import axios from 'axios';

export const getUserLogin = ({ code }: any) => {
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

export const getUserAuthority = ({ accessToken }: any) => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/myInfo`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
