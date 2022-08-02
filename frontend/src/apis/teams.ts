import axios, { AxiosPromise } from 'axios';

import { TeamApiType, InterviewTeamType } from 'types/team';

export const requestGetTeams = (): AxiosPromise<Record<'teams', InterviewTeamType[]>> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/teams`,
  });
};

export const requestGetTeam = ({ teamId }: TeamApiType): AxiosPromise<InterviewTeamType> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/teams/${teamId}`,
  });
};
