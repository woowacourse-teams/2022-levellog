import axios, { AxiosPromise } from 'axios';

import { InterviewTeamType } from 'types/index';

export const getTeams = (): AxiosPromise<Record<'teams', InterviewTeamType[]>> =>
  axios({
    method: 'get',
    url: 'https://levellog.app/api/teams',
  });

export const getTeam = (teamId: string): AxiosPromise<InterviewTeamType> =>
  axios({
    method: 'get',
    url: `https://levellog.app/api/teams/${teamId}`,
  });
