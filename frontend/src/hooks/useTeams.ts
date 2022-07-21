import { useContext } from 'react';

import { getTeam, getTeams } from 'apis/teams';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';

const useTeams = () => {
  const currentTeam = useContext(TeamContext);
  const teamInfoDispatch = useContext(TeamDispatchContext);

  // 하위 컴포넌트에 팀 정보를 수정할 때 호출
  const teamRequestAndDispatch = async () => {
    try {
      const res = await teamLookup(currentTeam.id);
      teamInfoDispatch(res);
    } catch (err) {
      console.log(err);
    }
  };

  const teamsLookup = async () => {
    try {
      const res = await getTeams();
      const teams = await res.data?.teams;

      return teams ? teams : [];
    } catch (err) {
      console.log(err);
    }
  };

  const teamLookup = async (teamId: string) => {
    try {
      const res = await getTeam(teamId);

      return res;
    } catch (err) {
      console.log(err);
    }
  };

  return { teamRequestAndDispatch, teamsLookup, teamLookup };
};

export default useTeams;
