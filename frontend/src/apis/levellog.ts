import axios, { AxiosPromise } from 'axios';

import { LevellogApiType, LevellogInfoType } from 'types/levellog';

export const requestPostLevellog = ({
  accessToken,
  teamId,
  levellogContent,
}: Omit<LevellogApiType, 'levellogId'>): AxiosPromise<void> => {
  return axios({
    method: 'post',
    url: `${process.env.API_URI}/teams/${teamId}/levellogs`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: levellogContent,
  });
};

export const requestGetLevellog = ({
  accessToken,
  teamId,
  levellogId,
}: Omit<LevellogApiType, 'levellogContent'>): AxiosPromise<LevellogInfoType> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/teams/${teamId}/levellogs/${levellogId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestEditLevellog = ({
  accessToken,
  teamId,
  levellogId,
  levellogContent,
}: LevellogApiType): AxiosPromise<void> => {
  return axios({
    method: 'put',
    url: `${process.env.API_URI}/teams/${teamId}/levellogs/${levellogId}`,
    headers: { Authorization: `Bearer ${accessToken}` },
    data: levellogContent,
  });
};
