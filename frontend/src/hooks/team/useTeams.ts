import { useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import useSnackbar from 'hooks/utils/useSnackbar';

import { NOT_YET_HTTP_STATUS, TEAMS_CONDITION } from 'constants/constants';

import { requestGetMyTeams } from 'apis/myInfo';
import { requestGetTeams } from 'apis/teams';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { InterviewTeamType, TeamApiType } from 'types/team';

const useTeams = () => {
  const { showSnackbar } = useSnackbar();
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const [isActive, setIsActive] = useState({
    status: NOT_YET_HTTP_STATUS,
    open: true,
    close: false,
    my: false,
  });

  const accessToken = localStorage.getItem('accessToken');

  const getTeams = async ({ teamsCondition }: Pick<TeamApiType, 'teamsCondition'>) => {
    try {
      const res = await requestGetTeams({ accessToken, teamsCondition });

      switch (teamsCondition) {
        case TEAMS_CONDITION.OPEN:
          setIsActive({ status: res.status, open: true, close: false, my: false });
          break;
        case TEAMS_CONDITION.CLOSE:
          setIsActive({ status: res.status, open: false, close: true, my: false });
          break;
      }
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
      setIsActive({ status: res.status, open: false, close: false, my: true });
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

  const handleClickFilterButtons = async (e: React.MouseEvent<HTMLElement>) => {
    const eventTarget = e.target as HTMLElement;

    switch (eventTarget.innerText) {
      case '진행중인 인터뷰':
        if (isActive.open) return;

        await getTeams({ teamsCondition: TEAMS_CONDITION.OPEN });
        break;
      case '종료된 인터뷰':
        if (isActive.close) return;

        await getTeams({ teamsCondition: TEAMS_CONDITION.CLOSE });
        break;
      case '나의 인터뷰':
        if (isActive.my) return;

        await getMyTeams();
        break;
    }
  };

  return {
    teams,
    getTeams,
    isActive,
    handleClickFilterButtons,
  };
};

export default useTeams;
