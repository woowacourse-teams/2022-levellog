import axios, { AxiosResponse } from 'axios';

import { useQuery } from '@tanstack/react-query';

import useSnackbar from 'hooks/utils/useSnackbar';

import { TEAMS_CONDITION } from 'constants/constants';

import { requestGetMyTeams } from 'apis/myInfo';
import { requestGetTeams } from 'apis/teams';
import { NotCorrectToken } from 'apis/utils';
import { TeamConditionsType } from 'types/team';

const useTeams = (teamsCondition: TeamConditionsType) => {
  const { showSnackbar } = useSnackbar();
  const accessToken = localStorage.getItem('accessToken');

  const getTeams = async ({ teamsCondition }: Record<'teamsCondition', TeamConditionsType>) => {
    try {
      if (teamsCondition.open) {
        const res = await requestGetTeams({ accessToken, teamsCondition: TEAMS_CONDITION.OPEN });

        return res.data;
      }

      if (teamsCondition.close) {
        const res = await requestGetTeams({ accessToken, teamsCondition: TEAMS_CONDITION.CLOSE });

        return res.data;
      }

      if (teamsCondition.my) {
        const res = await requestGetMyTeams({ accessToken });

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

  const { data: teams } = useQuery(['teams', teamsCondition], () => getTeams({ teamsCondition }), {
    suspense: true,
  });

  return {
    teams: teams?.teams,
  };
};

export default useTeams;
