import { AuthorizationHeader, fetcher } from 'apis';
import { UserType } from 'types';

export const requestGetMembers = async ({
  accessToken,
  nickname,
}: MembersGetRequestType): Promise<Record<'members', UserType[]>> => {
  const membersGetUri = `/members?nickname=${nickname}`;

  const { data } = await fetcher.get(membersGetUri, AuthorizationHeader(accessToken));

  return data;
};

interface MembersGetRequestType {
  accessToken: string | null;
  nickname: string;
}
