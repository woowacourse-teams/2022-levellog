import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetLoginUserRole } from 'apis/role';
import { RoleCustomHookType } from 'types/role';

const useRole = () => {
  const [feedbackWriterRole, setFeedbackWriterRole] = useState('');
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');

  const getWriterInfo = async ({
    teamId,
    participantId,
  }: Omit<RoleCustomHookType, 'levellogId'>) => {
    try {
      getFeedbackWriterRole({ teamId, participantId });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const getFeedbackWriterRole = async ({
    teamId,
    participantId,
  }: Omit<RoleCustomHookType, 'levellogId'>) => {
    try {
      const res = await requestGetLoginUserRole({ teamId, participantId, accessToken });
      setFeedbackWriterRole(res.data.myRole);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  return {
    feedbackWriterRole,
    getWriterInfo,
  };
};

export default useRole;
