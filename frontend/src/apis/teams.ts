import { fetcher } from 'apis';
import axios, { AxiosPromise } from 'axios';

import { 엑세스토큰이없는경우헤더제거 } from 'apis/utils';
import { TeamApiType, InterviewTeamType, InterviewTeamDetailType } from 'types/team';

export const requestPostTeam = async ({ teamInfo, accessToken }: Omit<TeamApiType, 'teamId'>) => {
  await fetcher.post('/teams', teamInfo, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestGetTeams = ({
  accessToken,
  teamsCondition,
}: Pick<TeamApiType, 'accessToken' | 'teamsCondition'>): AxiosPromise<
  Record<'teams', InterviewTeamType[]>
> => {
  return axios(
    엑세스토큰이없는경우헤더제거({
      accessToken,
      method: 'get',
      url: `${process.env.API_URI}/teams?condition=${teamsCondition}&size=1000`,
      headers: { Authorization: `Bearer ${accessToken}` },
    }),
  );
};

export const requestGetTeam = async ({
  teamId,
  accessToken,
}: Omit<TeamApiType, 'teamInfo'>): Promise<InterviewTeamDetailType> => {
  const { data } = await fetcher.get(`/teams/${teamId}`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export const requestEditTeam = async ({ teamId, teamInfo, accessToken }: TeamApiType) => {
  await fetcher.put(`/teams/${teamId}`, teamInfo, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestDeleteTeam = async ({ teamId, accessToken }: Omit<TeamApiType, 'teamInfo'>) => {
  await fetcher.delete(`/teams/${teamId}`, {
    headers: { Authorization: `Bearer ${accessToken}` },
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
