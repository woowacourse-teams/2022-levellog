import axios, { AxiosPromise } from 'axios';

import { MembersApiType, MemberType } from 'types/member';

export const requestGetMembers = ({
  accessToken,
  nickname,
}: MembersApiType): AxiosPromise<Record<'members', MemberType[]>> => {
  return axios({
    method: 'get',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/members?nickname=${nickname}`,
  });
};
