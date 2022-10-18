import { fetcher } from 'apis';

import { TeamCustomHookType } from 'hooks/team/types/index';

import { TeamsConditionType } from 'types/team';
import { InterviewTeamType } from 'types/team';

export const requestGetMyTeams = async ({
  accessToken,
}: Pick<TeamApiType, 'accessToken' | 'teamsCondition'>): Promise<
  Record<'teams', InterviewTeamType[]>
> => {
  const { data } = await fetcher.get(`/my-info/teams`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export interface TeamApiType {
  accessToken: string | null;
  teamId: string | undefined;
  teamInfo: TeamCustomHookType;
  teamsCondition?: TeamsConditionType;
}
