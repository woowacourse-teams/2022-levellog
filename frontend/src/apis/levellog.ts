import axios from 'axios';
import { LevellogType } from 'types';

export const postLevellog = (
  accessToken: string,
  teamId: string,
  levellogContent: LevellogType,
) => {
  axios({
    method: 'post',
    url: `https://levellog.app/api/teams/${teamId}/levellogs`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: levellogContent,
  });
};

export const getLevellog = (accessToken: string, teamId: string, id: string) =>
  axios({
    method: 'get',
    url: `https://levellog.app/api/teams/${teamId}/levellogs/${id}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });

export const modifyLevellog = (
  accessToken: string,
  teamId: string,
  id: string,
  levellogContent: LevellogType,
) => {
  axios({
    method: 'put',
    url: `https://levellog.app/api/teams/${teamId}/levellogs/${id}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: levellogContent,
  });
};

export const deleteLevellog = (accessToken: string, teamId: string, id: string) => {
  axios({
    method: 'delete',
    url: `https://levellog.app/api/teams/${teamId}/levellogs/${id}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
