import React, { useEffect, useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import useSnackbar from 'hooks/useSnackbar';

import { TEAMS_STATUS } from 'constants/constants';

import { requestGetMyTeams } from 'apis/myInfo';
import { requestGetTeams } from 'apis/teams';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { InterviewTeamType, TeamApiType } from 'types/team';

const useTeams = () => {
  const { showSnackbar } = useSnackbar();
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const [isActive, setIsActive] = useState({ open: true, close: false, my: false });

  const accessToken = localStorage.getItem('accessToken');

  const getTeams = async ({ teamsStatus }: Pick<TeamApiType, 'teamsStatus'>) => {
    try {
      const res = await requestGetTeams({ accessToken, teamsStatus });
      setTeams(res.data.teams);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const getMyTeams = async () => {
    try {
      const res = await requestGetMyTeams({ accessToken });
      setTeams(res.data.teams);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const handleClickFilterButtons = async (e: React.SyntheticEvent<HTMLElement>) => {
    const eventTarget = e.target as HTMLElement;
    switch (eventTarget.innerText) {
      case '진행중인 인터뷰':
        if (isActive.open) return;

        await getTeams({ teamsStatus: TEAMS_STATUS.OPEN });
        setIsActive({ open: true, close: false, my: false });
        break;
      case '종료된 인터뷰':
        if (isActive.close) return;

        await getTeams({ teamsStatus: TEAMS_STATUS.CLOSE });
        setIsActive({ open: false, close: true, my: false });
        break;
      case '나의 인터뷰':
        if (isActive.my) return;

        await getMyTeams();
        setIsActive({ open: false, close: false, my: true });
        break;
    }
  };

  useEffect(() => {
    getTeams({ teamsStatus: TEAMS_STATUS.OPEN });
  }, []);

  return {
    teams,
    getTeams,
    isActive,
    handleClickFilterButtons,
  };
};

export default useTeams;
