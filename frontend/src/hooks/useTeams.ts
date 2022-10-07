import { useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import { TEAMS_CONDITION } from 'constants/constants';

import useSnackbar from './useSnackbar';
import { requestGetMyTeams } from 'apis/myInfo';
import { requestGetTeams } from 'apis/teams';
import { NotCorrectToken } from 'apis/utils';
import { InterviewTeamType, TeamApiType, TeamConditionsType } from 'types/team';

const useTeams = () => {
  const { showSnackbar } = useSnackbar();

  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const [teamsCondition, setTeamsCondition] = useState({
    open: true,
    close: false,
    my: false,
  });

  const accessToken = localStorage.getItem('accessToken');

  const getTeams = async ({ teamsCondition }: Record<'teamsCondition', TeamConditionsType>) => {
    try {
      if (teamsCondition.open) {
        const res = await requestGetTeams({ accessToken, teamsCondition: TEAMS_CONDITION.OPEN });
        setTeams(res.data.teams);

        return res.data;
      }

      if (teamsCondition.close) {
        const res = await requestGetTeams({ accessToken, teamsCondition: TEAMS_CONDITION.CLOSE });
        setTeams(res.data.teams);

        return res.data;
      }

      if (teamsCondition.my) {
        const res = await requestGetMyTeams({ accessToken });
        setTeams(res.data.teams);

        return res.data;
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const handleClickFilterButtons = (e: React.MouseEvent<HTMLElement>) => {
    const eventTarget = e.target as HTMLElement;

    switch (eventTarget.innerText) {
      case '진행중인 인터뷰':
        if (teamsCondition.open) return;

        setTeamsCondition({ open: true, close: false, my: false });
        break;
      case '종료된 인터뷰':
        if (teamsCondition.close === true) return;

        setTeamsCondition({ open: false, close: true, my: false });
        break;
      case '나의 인터뷰':
        if (teamsCondition.my) return;

        setTeamsCondition({ open: false, close: false, my: true });
        break;
    }
  };

  return {
    teams,
    getTeams,
    teamsCondition,
    handleClickFilterButtons,
  };
};

export default useTeams;
