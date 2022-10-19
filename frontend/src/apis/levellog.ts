import { AuthorizationHeader, fetcher } from 'apis';

import { LevellogFormatType } from './../types/levellog';

import { LevellogInfoType } from 'types/levellog';

export const requestPostLevellog = async ({
  accessToken,
  teamId,
  levellogContent,
}: Omit<LevellogRequestType, 'levellogId'>) => {
  const levellogPostUri = `/teams/${teamId}/levellogs`;

  await fetcher.post(levellogPostUri, levellogContent, AuthorizationHeader(accessToken));
};

export const requestGetLevellog = async ({
  accessToken,
  teamId,
  levellogId,
}: Omit<LevellogRequestType, 'levellogContent'>): Promise<LevellogInfoType> => {
  const levellogGetUri = `/teams/${teamId}/levellogs/${levellogId}`;

  const { data } = await fetcher.get(levellogGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestEditLevellog = async ({
  accessToken,
  teamId,
  levellogId,
  levellogContent,
}: LevellogRequestType) => {
  const levellogPutUri = `/teams/${teamId}/levellogs/${levellogId}`;

  await fetcher.put(levellogPutUri, levellogContent, AuthorizationHeader(accessToken));
};

interface LevellogRequestType {
  accessToken: string | null;
  teamId: string | undefined;
  levellogId: string | undefined;
  levellogContent: LevellogFormatType;
}
