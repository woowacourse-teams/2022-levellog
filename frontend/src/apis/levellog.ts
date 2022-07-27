import axios from 'axios';

import { API_URL } from 'constants/constants';

export const requestPostLevellog = ({ accessToken, teamId, levellogContent }: any) => {
  return axios({
    method: 'post',
    url: `${API_URL}/teams/${teamId}/levellogs`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: levellogContent,
  });
};

export const requestGetLevellog = ({ accessToken, teamId, levellogId }: any) => {
  return axios({
    method: 'get',
    url: `${API_URL}/teams/${teamId}/levellogs/${levellogId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestEditLevellog = ({ accessToken, teamId, levellogId, levellogContent }: any) => {
  return axios({
    method: 'put',
    url: `${API_URL}/teams/${teamId}/levellogs/${levellogId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: levellogContent,
  });
};

export const requestDeleteLevellog = ({ accessToken, teamId, levellogId }: any) => {
  return axios({
    method: 'delete',
    url: `${API_URL}/teams/${teamId}/levellogs/${levellogId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
