import { AuthorizationHeader, fetcher } from 'apis';

import { LevellogInfoType, LevellogType } from 'types/levellog';

export const requestPostLevellog = async ({
  accessToken,
  teamId,
  levellog,
}: LevellogPostRequestType) => {
  const levellogPostUri = `/teams/${teamId}/levellogs`;

  await fetcher.post(levellogPostUri, levellog, AuthorizationHeader(accessToken));
};

export const requestGetLevellog = async ({
  accessToken,
  teamId,
  levellogId,
}: LevellogGetRequestType): Promise<LevellogInfoType> => {
  const levellogGetUri = `/teams/${teamId}/levellogs/${levellogId}`;

  const { data } = await fetcher.get(levellogGetUri, AuthorizationHeader(accessToken));

  return data;
};

export const requestEditLevellog = async ({
  accessToken,
  teamId,
  levellogId,
  levellog,
}: LevellogEditRequestType) => {
  const levellogPutUri = `/teams/${teamId}/levellogs/${levellogId}`;

  await fetcher.put(levellogPutUri, levellog, AuthorizationHeader(accessToken));
};

export interface LevellogRequestCommonType {
  accessToken: string | null;
  teamId: string | undefined;
}
export interface LevellogGetRequestType extends LevellogRequestCommonType {
  levellogId: string | undefined;
}

export interface LevellogPostRequestType extends LevellogRequestCommonType {
  levellog: LevellogType;
}

export interface LevellogEditRequestType extends LevellogRequestCommonType {
  levellogId: string | undefined;
  levellog: LevellogType;
}
