import { fetcher } from 'apis';

export const requestGetLoginUserRole = async ({
  teamId,
  participantId,
  accessToken,
}: RoleApiType): Promise<Record<'myRole', string>> => {
  const { data } = await fetcher.get(`/teams/${teamId}/members/${participantId}/my-role`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export interface RoleApiType {
  accessToken: string | null;
  teamId: string | undefined;
  participantId: string | undefined;
}
