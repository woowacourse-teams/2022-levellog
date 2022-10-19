import { fetcher } from 'apis';

import { AuthorizationHeader } from 'apis/index';
import { InterviewTeamType } from 'types/team';

export const requestGetMyTeams = async ({
  accessToken,
}: MyTeamGetRequestType): Promise<Record<'teams', InterviewTeamType[]>> => {
  const teamGetUri = `/my-info/teams`;

  const { data } = await fetcher.get(teamGetUri, AuthorizationHeader(accessToken));

  return data;
};

interface MyTeamGetRequestType {
  accessToken: string | null;
}
