import { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import { InterviewTeamType } from 'types';

import { requestGetTeams, requestGetTeam } from 'apis/teams';

export const useTeams = () => {
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const navigate = useNavigate();

  const getTeams = async () => {
    try {
      const res = await requestGetTeams();
      const teams = await res.data?.teams;

      setTeams(teams);
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  const handleClickInterviewGroup = (e: React.MouseEvent<HTMLElement>) => {
    const target = e.target as HTMLElement;
    const team = teams.find((team) => +team.id === +target.id);

    navigate(`/interview/teams/${target.id}`, {
      state: team,
    });
  };

  return { teams, getTeams, handleClickInterviewGroup };
};

export const useTeam = () => {
  const [team, setTeam] = useState<{ [key: string]: any }>({});
  const location = useLocation();
  const { teamId } = useParams();
  const teamLocationState = location.state;

  const getTeam = async () => {
    try {
      const res = await requestGetTeam({ teamId });

      setTeam(res.data);
    } catch (err) {
      const res = err.response as any;
      if (err instanceof Error) alert(res.data.message);
    }
  };

  useEffect(() => {
    if (teamLocationState) {
      setTeam(teamLocationState);
    }
  }, []);

  return { teamLocationState, team, getTeam };
};
