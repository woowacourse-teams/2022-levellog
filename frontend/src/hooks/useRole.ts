import { useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import { requestGetLoginUserRole } from 'apis/role';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { RoleCustomHookType } from 'types/role';

const useRole = () => {
  const [feedbackWriterRole, setFeedbackWriterRole] = useState('');

  const accessToken = localStorage.getItem('accessToken');

  // 얜 뭐임? 밑에랑 나눠져있는 이유가 뭐지?
  const getWriterInfo = async ({
    teamId,
    participantId,
  }: Omit<RoleCustomHookType, 'levellogId'>) => {
    try {
      getFeedbackWriterRole({ teamId, participantId });
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
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
