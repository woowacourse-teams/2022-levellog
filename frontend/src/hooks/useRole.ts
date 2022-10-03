import { useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import useSnackbar from 'hooks/useSnackbar';

import { requestGetLoginUserRole } from 'apis/role';
import { NotCorrectToken } from 'apis/utils';
import { RoleCustomHookType } from 'types/role';

const useRole = () => {
  const { showSnackbar } = useSnackbar();
  const [feedbackWriterRole, setFeedbackWriterRole] = useState('');

  const accessToken = localStorage.getItem('accessToken');

  const getWriterInfo = async ({
    teamId,
    participantId,
  }: Omit<RoleCustomHookType, 'levellogId'>) => {
    try {
      const res = await requestGetLoginUserRole({ teamId, participantId, accessToken });
      setFeedbackWriterRole(res.data.myRole);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  return {
    feedbackWriterRole,
    getWriterInfo,
  };
};

export default useRole;
