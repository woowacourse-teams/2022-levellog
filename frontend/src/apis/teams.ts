import axios, { AxiosPromise } from 'axios';

import { API_URL } from 'constants/constants';

import { InterviewTeamType } from 'types/index';

export const requestGetTeams = (): AxiosPromise<Record<'teams', InterviewTeamType[]>> => {
  return axios({
    method: 'get',
    url: `${API_URL}/teams`,
  });
};

export const requestGetTeam = ({ teamId }: any): AxiosPromise<InterviewTeamType> => {
  return axios({
    method: 'get',
    url: `${API_URL}/teams/${teamId}`,
  });
};
