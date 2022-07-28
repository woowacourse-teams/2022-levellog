import axios, { AxiosPromise } from 'axios';

import { InterviewTeamType } from 'types/index';

export const requestGetTeams = (): AxiosPromise<Record<'teams', InterviewTeamType[]>> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/teams`,
  });
};

export const requestGetTeam = ({ teamId }: any): AxiosPromise<InterviewTeamType> => {
  return axios({
    method: 'get',
    url: `${process.env.API_URI}/teams/${teamId}`,
  });
};
