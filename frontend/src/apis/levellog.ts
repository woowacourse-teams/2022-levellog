import { fetcher } from 'apis';

import { LevellogApiType, LevellogInfoType } from 'types/levellog';

export const requestPostLevellog = async ({
  accessToken,
  teamId,
  levellogContent,
}: Omit<LevellogApiType, 'levellogId'>) => {
  await fetcher.post(`/teams/${teamId}/levellogs`, levellogContent, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const requestGetLevellog = async ({
  accessToken,
  teamId,
  levellogId,
}: Omit<LevellogApiType, 'levellogContent'>): Promise<LevellogInfoType> => {
  const { data } = await fetcher.get(`/teams/${teamId}/levellogs/${levellogId}`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

export const requestEditLevellog = async ({
  accessToken,
  teamId,
  levellogId,
  levellogContent,
}: LevellogApiType) => {
  await fetcher.put(`/teams/${teamId}/levellogs/${levellogId}`, levellogContent, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
