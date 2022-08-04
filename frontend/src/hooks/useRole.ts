import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetMyRole } from 'apis/role';
import { RoleApiType } from 'types/role';

const useRole = () => {
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');
  const [myRole, setMyRole] = useState('');

  const getMyRole = async ({ teamId, participantId }: Omit<RoleApiType, 'accessToken'>) => {
    try {
      const res = await requestGetMyRole({ teamId, participantId, accessToken });
      setMyRole(res.data);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  return {
    myRole,
    getMyRole,
  };
};

export default useRole;
