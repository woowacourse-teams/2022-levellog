import axios, { AxiosPromise } from 'axios';

import { API_URL } from 'constants/constants';

import { InterviewTeamType } from 'types/index';

export const getTeams = (): AxiosPromise<Record<'teams', InterviewTeamType[]>> =>
  axios({
    method: 'get',
    url: `${API_URL}/teams`,
  });

export const getTeam = (teamId: string): AxiosPromise<InterviewTeamType> =>
  axios({
    method: 'get',
    url: `${API_URL}/teams/${teamId}`,
  });
