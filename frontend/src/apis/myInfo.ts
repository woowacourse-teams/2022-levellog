import { fetcher } from 'apis';

import { AuthorizationHeader } from 'apis/index';
import { InterviewTeamType } from 'types/team';

export const requestGetMyTeams = async ({
  accessToken,
}: MyTeamGetRequestType): Promise<Record<'teams', InterviewTeamType[]>> => {
  const TEAM_GET_URI = `/my-info/teams`;

  const { data } = await fetcher.get(TEAM_GET_URI, AuthorizationHeader(accessToken));

  return data;
};

interface MyTeamGetRequestType {
  accessToken: string | null;
}
