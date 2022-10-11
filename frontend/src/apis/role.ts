import { fetcher } from 'apis';

import { RoleApiType } from 'types/role';

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
