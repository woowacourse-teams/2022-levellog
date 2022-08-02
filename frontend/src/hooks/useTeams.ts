import { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetTeams, requestGetTeam } from 'apis/teams';
import { InterviewTeamType } from 'types/team';

export const useTeams = () => {
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const navigate = useNavigate();

  const getTeams = async () => {
    try {
      const res = await requestGetTeams();
      const teams = await res.data?.teams;

      setTeams(teams);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
      }
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
  const { loginUserId } = useUser();
  const [team, setTeam] = useState<InterviewTeamType | Object>({});
  const [userInTeam, setUserInTeam] = useState(false);
  // 나중에 location은 타입을 고칠 필요가 있어보임
  const location = useLocation() as unknown as { state: InterviewTeamType };
  const { teamId } = useParams();
  const navigate = useNavigate();
  const teamLocationState: InterviewTeamType | undefined = location.state;

  const getTeam = async () => {
    try {
      if (typeof teamId === 'string') {
        const res = await requestGetTeam({ teamId });

        setTeam(res.data);
        checkUserInTeam({ team: res.data });
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

  const checkUserInTeam = ({ team }: Record<'team', InterviewTeamType>) => {
    setUserInTeam(team.participants.some((participant) => participant.memberId === loginUserId));
  };

  useEffect(() => {
    // 나중에 location은 타입을 고칠 필요가 있어보임
    if (teamLocationState && (teamLocationState as InterviewTeamType).id !== undefined) {
      setTeam(teamLocationState);
      checkUserInTeam({ team: teamLocationState });
    }
  }, []);

  return { teamLocationState, team, userInTeam, getTeam };
};
