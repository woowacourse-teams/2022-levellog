import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useTeam } from 'hooks/useTeams';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetLoginUserRole } from 'apis/role';
import { RoleCustomHookType } from 'types/role';
import { InterviewTeamType } from 'types/team';

const useRole = () => {
  const { getTeam } = useTeam();
  const [feedbackWriterRole, setFeedbackWriterRole] = useState('');
  const [levellogWriter, setLevellogWriter] = useState('');
  const navigate = useNavigate();
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
    levellogWriter,
    feedbackWriterRole,
    getWriterInfo,
  };
};

export default useRole;
