import { requestGetTeams } from 'apis/teams';
import { TeamApiType } from 'types/team';

const useTeamsQuery = () => {
  const accessToken = localStorage.getItem('accessToken');

  const getTeams = ({ teamsCondition }: Pick<TeamApiType, 'teamsCondition'>) => {
    return requestGetTeams({ accessToken, teamsCondition }).then((res) => res.data);
  };

  return {
    getTeams,
  };
};

export default useTeamsQuery;
