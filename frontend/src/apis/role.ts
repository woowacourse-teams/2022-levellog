import { AuthorizationHeader, fetcher } from 'apis';

export const requestGetLoginUserRole = async ({
  teamId,
  participantId,
  accessToken,
}: RoleApiType): Promise<Record<'myRole', string>> => {
  const myRoleGetUri = `/teams/${teamId}/members/${participantId}/my-role`;

  const { data } = await fetcher.get(myRoleGetUri, AuthorizationHeader(accessToken));

  return data;
};

export interface RoleApiType {
  accessToken: string | null;
  teamId: string | undefined;
  participantId: string | undefined;
}
