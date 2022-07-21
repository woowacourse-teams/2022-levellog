import axios from 'axios';

export const getUserAuthority = (code: string) =>
  axios({
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    url: 'https://levellog.app/api/auth/login',
    data: {
      authorizationCode: code,
    },
  });
