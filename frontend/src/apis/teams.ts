import axios, { AxiosPromise } from 'axios';

import { 엑세스토큰이없는경우헤더제거 } from 'apis/utils';
import { TeamApiType, InterviewTeamType, InterviewTeamDetailType } from 'types/team';

export const requestPostTeam = ({
  teamInfo,
  accessToken,
}: Omit<TeamApiType, 'teamId'>): AxiosPromise<void> => {
  return axios({
    method: 'post',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/teams`,
    data: teamInfo,
  });
};

export const requestGetTeams = ({
  accessToken,
  teamsStatus,
}: Pick<TeamApiType, 'accessToken' | 'teamsStatus'>): AxiosPromise<
  Record<'teams', InterviewTeamType[]>
> => {
  return axios(
    엑세스토큰이없는경우헤더제거({
      accessToken,
      method: 'get',
      url: `${process.env.API_URI}/teams?condition=${teamsStatus}`,
      headers: { Authorization: `Bearer ${accessToken}` },
    }),
  );
};

export const requestGetTeam = ({
  teamId,
  accessToken,
}: Omit<TeamApiType, 'teamInfo'>): AxiosPromise<InterviewTeamDetailType> => {
  return axios(
    엑세스토큰이없는경우헤더제거({
      accessToken,
      method: 'get',
      url: `${process.env.API_URI}/teams/${teamId}`,
      headers: { Authorization: `Bearer ${accessToken}` },
    }),
  );
};

export const requestEditTeam = ({ teamId, teamInfo, accessToken }: TeamApiType) => {
  return axios({
    method: 'put',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/teams/${teamId}`,
    data: teamInfo,
  });
};

export const requestDeleteTeam = ({
  teamId,
  accessToken,
}: Omit<TeamApiType, 'teamInfo'>): AxiosPromise<void> => {
  return axios({
    method: 'delete',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/teams/${teamId}`,
  });
};

export const requestCloseTeamInterview = ({
  teamId,
  accessToken,
}: Omit<TeamApiType, 'teamInfo'>): AxiosPromise<void> => {
  return axios({
    method: 'post',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/teams/${teamId}/close`,
  });
};
