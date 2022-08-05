import axios from 'axios';

import { RoleApiType } from 'types/role';

export const requestGetLoginUserRole = ({ teamId, participantId, accessToken }: RoleApiType) => {
  return axios({
    method: 'get',
    headers: { Authorization: `Bearer ${accessToken}` },
    url: `${process.env.API_URI}/teams/${teamId}/members/${participantId}/my-role`,
  });
};
