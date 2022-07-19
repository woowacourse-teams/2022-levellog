import axios, { AxiosPromise } from 'axios';

import { InterviewTeamType } from 'types/index';

export const getTeams = (): AxiosPromise<Record<'teams', InterviewTeamType[]>> =>
  axios({
    method: 'get',
    url: '/api/teams',
  });

export const getTeam = (teamId: string): AxiosPromise<InterviewTeamType> =>
  axios({
    method: 'get',
    url: `/api/teams/${teamId}`,
  });
