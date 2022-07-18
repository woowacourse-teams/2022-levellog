import { getTeam, getTeams } from 'apis/teams';

const useTeams = () => {
  const teamsLookup = async () => {
    try {
      const res = await getTeams();
      const teams = await res?.data?.teams;

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

  return { teamsLookup, teamLookup };
};

export default useTeams;
