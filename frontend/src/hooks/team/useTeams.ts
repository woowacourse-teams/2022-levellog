import { useQuery } from '@tanstack/react-query';

import { TEAMS_CONDITION } from 'constants/constants';
import { QUERY_KEY } from 'constants/constants';

import { requestGetMyTeams } from 'apis/myInfo';
import { requestGetTeams } from 'apis/teams';
import { TeamConditionsType } from 'types/team';

const useTeams = (teamsCondition: TeamConditionsType) => {
  const accessToken = localStorage.getItem('accessToken');

  const getTeams = async ({ teamsCondition }: Record<'teamsCondition', TeamConditionsType>) => {
    if (teamsCondition.open) {
      return await requestGetTeams({ accessToken, teamsCondition: TEAMS_CONDITION.OPEN });
    }

    if (teamsCondition.close) {
      return await requestGetTeams({ accessToken, teamsCondition: TEAMS_CONDITION.CLOSE });
    }

    if (teamsCondition.my) {
      return await requestGetMyTeams({ accessToken });
    }
  };

  const { data: teams } = useQuery([QUERY_KEY.TEAMS, teamsCondition], () => {
    return getTeams({ teamsCondition });
  });

  return {
    teams: teams?.teams,
  };
};

export default useTeams;
