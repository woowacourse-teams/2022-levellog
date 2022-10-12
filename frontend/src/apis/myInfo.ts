import { fetcher } from 'apis';

import { InterviewTeamType, TeamApiType } from 'types/team';

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
