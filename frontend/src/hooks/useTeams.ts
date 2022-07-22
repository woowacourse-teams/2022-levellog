import { useContext } from 'react';

import { getTeam, getTeams } from 'apis/teams';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';

const useTeams = () => {
  const currentTeam = useContext(TeamContext);
  const teamInfoDispatch = useContext(TeamDispatchContext);

  // 하위 컴포넌트에 팀 정보를 수정할 때 호출
  const teamRequestAndDispatch = async (teamId = null) => {
    if (teamId !== null) {
      currentTeam.id === teamId;
    }
    try {
      const res = await teamLookup(currentTeam.id);
      console.log('res:', res);
      teamInfoDispatch(res);
      return true;
    } catch (err) {
      console.log(err);
      return false;
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

      return res.data;
    } catch (err) {
      console.log(err);
    }
  };

  return { teamRequestAndDispatch, teamsLookup, teamLookup };
};

export default useTeams;
