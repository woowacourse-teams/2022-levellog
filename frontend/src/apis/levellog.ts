import axios from 'axios';

export const requestPostLevellog = ({ accessToken, teamId, levellogContent }: any) => {
  return axios({
    method: 'post',
    url: `${process.env.API_URI}/teams/${teamId}/levellogs`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: levellogContent,
  });
};

export const requestGetLevellog = ({ accessToken, teamId, levellogId }: any) => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/teams/${teamId}/levellogs/${levellogId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestEditLevellog = ({ accessToken, teamId, levellogId, levellogContent }: any) => {
  return axios({
    method: 'put',
    url: `${process.env.API_URI}/teams/${teamId}/levellogs/${levellogId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: levellogContent,
  });
};

export const requestDeleteLevellog = ({ accessToken, teamId, levellogId }: any) => {
  return axios({
    method: 'delete',
    url: `${process.env.API_URI}/teams/${teamId}/levellogs/${levellogId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
