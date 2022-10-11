import { fetcher } from 'apis';

import { MembersApiType, MemberType } from 'types/member';

export const requestGetMembers = async ({
  accessToken,
  nickname,
}: MembersApiType): Promise<Record<'members', MemberType[]>> => {
  const { data } = await fetcher.get(`/members?nickname=${nickname}`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};
