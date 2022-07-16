import axios from 'axios';

export const getUserAuthority = (code: string) =>
  axios({
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    url: '/api/auth/login',
    data: {
      authorizationCode: code,
    },
  });
