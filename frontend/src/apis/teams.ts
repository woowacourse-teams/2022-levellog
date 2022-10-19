import { fetcher } from 'apis';

import { TeamCustomHookType } from 'hooks/team/types';

import { AuthorizationHeader } from 'apis/index';
import { InterviewTeamType, InterviewTeamDetailType, TeamsConditionType } from 'types/team';

export const requestPostTeam = async ({ teamInfo, accessToken }: TeamPostRequestType) => {
  const TEAM_POST_URI = '/teams';

  await fetcher.post(TEAM_POST_URI, teamInfo, AuthorizationHeader(accessToken));
};

export const requestGetTeams = async ({
  accessToken,
  teamsCondition,
}: TeamsGetRequestType): Promise<Record<'teams', InterviewTeamType[]>> => {
  const teamsGetUri = `/teams?condition=${teamsCondition}&size=1000`;

  const { data } = await fetcher.get(teamsGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestGetTeam = async ({
  teamId,
  accessToken,
}: TeamRequestCommonType): Promise<InterviewTeamDetailType> => {
  const teamGetUri = `/teams/${teamId}`;

  const { data } = await fetcher.get(
    teamGetUri,
    accessToken ? AuthorizationHeader(accessToken) : {},
  );

  return data;
};

export const requestEditTeam = async ({ teamId, teamInfo, accessToken }: TeamEditRequestType) => {
  const teamPutUri = `/teams/${teamId}`;

  await fetcher.put(teamPutUri, teamInfo, AuthorizationHeader(accessToken));
};

export const requestDeleteTeam = async ({ teamId, accessToken }: TeamRequestCommonType) => {
  const teamDeleteUri = `/teams/${teamId}`;

  await fetcher.delete(teamDeleteUri, AuthorizationHeader(accessToken));
};

export const requestCloseTeamInterview = async ({ teamId, accessToken }: TeamRequestCommonType) => {
  const interviewClosePostUri = `/teams/${teamId}/close`;

  await fetcher.post(interviewClosePostUri, {}, AuthorizationHeader(accessToken));
};

interface TeamRequestCommonType {
  accessToken: string | null;
  teamId: string | undefined;
}

interface TeamPostRequestType {
  teamInfo: TeamCustomHookType;
  accessToken: string | null;
}

interface TeamsGetRequestType {
  accessToken: string | null;
  teamsCondition: TeamsConditionType;
}

interface TeamEditRequestType extends TeamRequestCommonType {
  teamInfo: TeamCustomHookType;
}
