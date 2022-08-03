import axios, { AxiosPromise } from 'axios';

import { TeamApiType, InterviewTeamType } from 'types/team';

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

export const requestGetTeams = (): AxiosPromise<Record<'teams', InterviewTeamType[]>> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/teams`,
  });
};

export const requestGetTeam = ({
  teamId,
}: Pick<TeamApiType, 'teamId'>): AxiosPromise<InterviewTeamType> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/teams/${teamId}`,
  });
};
