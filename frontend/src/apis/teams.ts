import { fetcher } from 'apis';
import axios, { AxiosPromise } from 'axios';

import { NotAccessTokenRemoveHeader } from 'apis/utils';
import { TeamApiType, InterviewTeamType, InterviewTeamDetailType } from 'types/team';

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

export const requestGetTeam = ({
  teamId,
  accessToken,
}: Omit<TeamApiType, 'teamInfo'>): AxiosPromise<InterviewTeamDetailType> => {
  return axios(
    NotAccessTokenRemoveHeader({
      accessToken,
      method: 'get',
      url: `${process.env.API_URI}/teams/${teamId}`,
      headers: { Authorization: `Bearer ${accessToken}` },
    }),
  );
};

export const requestEditTeam = ({ teamId, teamInfo, accessToken }: TeamApiType) => {
  return axios({
    method: 'put',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/teams/${teamId}`,
    data: teamInfo,
  });
};

export const requestDeleteTeam = ({
  teamId,
  accessToken,
}: Omit<TeamApiType, 'teamInfo'>): AxiosPromise<void> => {
  return axios({
    method: 'delete',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/teams/${teamId}`,
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
