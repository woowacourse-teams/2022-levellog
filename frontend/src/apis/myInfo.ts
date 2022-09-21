import axios, { AxiosPromise } from 'axios';

import { InterviewTeamType, TeamApiType } from 'types/team';

export const requestGetMyTeams = ({
  accessToken,
}: Pick<TeamApiType, 'accessToken'>): AxiosPromise<Record<'teams', InterviewTeamType[]>> => {
  return axios({
    method: 'get',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/my-info/teams`,
  });
};
