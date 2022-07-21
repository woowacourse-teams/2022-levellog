import axios from 'axios';

import { API_URL } from 'constants/constants';

export const getUserAuthority = (code: string) =>
  axios({
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    url: `${API_URL}/auth/login`,
    data: {
      authorizationCode: code,
    },
  });
