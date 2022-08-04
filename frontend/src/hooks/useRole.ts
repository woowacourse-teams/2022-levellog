import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetLoginUserRole } from 'apis/role';
import { RoleApiType } from 'types/role';

const useRole = () => {
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');
  const [loginUserRole, setLoginUserRole] = useState('');

  const getLoginUserRole = async ({ teamId, participantId }: Omit<RoleApiType, 'accessToken'>) => {
    try {
      const res = await requestGetLoginUserRole({ teamId, participantId, accessToken });
      setLoginUserRole(res.data.myRole);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  return {
    loginUserRole,
    getLoginUserRole,
  };
};

export default useRole;
