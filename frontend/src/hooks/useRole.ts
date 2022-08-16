import { useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import useTeam from 'hooks/useTeam';

import { requestGetLoginUserRole } from 'apis/role';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { RoleCustomHookType } from 'types/role';
import { InterviewTeamType } from 'types/team';

const useRole = () => {
  const { getTeam } = useTeam();
  const [feedbackWriterRole, setFeedbackWriterRole] = useState('');
  const [levellogWriter, setLevellogWriter] = useState('');

  const accessToken = localStorage.getItem('accessToken');

  const getWriterInfo = async ({
    teamId,
    levellogId,
  }: Omit<RoleCustomHookType, 'participantId'>) => {
    try {
      const team = await getTeam();
      const levellogWriter = (team as InterviewTeamType).participants.find(
        (participant) => Number(participant.levellogId) === Number(levellogId),
      );
      setLevellogWriter(levellogWriter!.nickname);
      getFeedbackWriterRole({ teamId, participantId: levellogWriter!.memberId });
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
    levellogWriter,
    feedbackWriterRole,
    getWriterInfo,
  };
};

export default useRole;
