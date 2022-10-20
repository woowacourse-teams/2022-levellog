import { fetcher } from 'apis';
import axios, { AxiosPromise } from 'axios';

import { TeamApiType, InterviewTeamType, InterviewTeamDetailType } from 'types/team';

export const requestPostTeam = async ({ teamInfo, accessToken }: Omit<TeamApiType, 'teamId'>) => {
  await fetcher.post('/teams', teamInfo, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestGetTeams = async ({
  accessToken,
  teamsCondition,
}: Pick<TeamApiType, 'accessToken' | 'teamsCondition'>): Promise<
  Record<'teams', InterviewTeamType[]>
> => {
  const { data } = await fetcher.get(`/teams?condition=${teamsCondition}&size=1000`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export const requestGetTeam = async ({
  teamId,
  accessToken,
}: Omit<TeamApiType, 'teamInfo'>): Promise<InterviewTeamDetailType> => {
  if (accessToken) {
    const { data } = await fetcher.get(`/teams/${teamId}`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });

    return data;
  }

  const { data } = await fetcher.get(`/teams/${teamId}`);

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
