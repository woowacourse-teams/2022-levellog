import { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import useUser from './useUser';
import { requestGetTeams, requestGetTeam, requestPostTeam } from 'apis/teams';
import { MemberType } from 'types/member';
import { InterviewTeamType, TeamCustomHookType } from 'types/team';

export const useTeams = () => {
  const { loginUserId } = useUser();
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const teamInfoRef = useRef<HTMLInputElement[]>([]);
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  const postTeams = async ({ teamInfo }: { teamInfo: TeamCustomHookType }) => {
    try {
      teamInfo.participants.ids = teamInfo.participants.ids.filter((id) => id !== loginUserId);
      await requestPostTeam({ teamInfo, accessToken });
      alert(MESSAGE.TEAM_CREATE);
      navigate(ROUTES_PATH.HOME);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

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

  const onSubmitTeamAddForm = async ({ participants }: { participants: MemberType[] }) => {
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    const teamInfo = {
      title: title.value,
      place: place.value,
      startAt: `${date.value}T${time.value}`,
      interviewerNumber: interviewerNumber.value,
      participants: {
        ids: Object.values(participants).map((participants) => participants.id),
      },
    };
    await postTeams({ teamInfo });
  };

  const handleClickInterviewGroup = (e: React.MouseEvent<HTMLElement>) => {
    const target = e.target as HTMLElement;
    const team = teams.find((team) => +team.id === +target.id);

    navigate(`/interview/teams/${target.id}`, {
      state: team,
    });
  };

  return {
    teams,
    teamInfoRef,
    postTeams,
    getTeams,
    onSubmitTeamAddForm,
    handleClickInterviewGroup,
  };
};

export const useTeam = () => {
  const { loginUserId } = useUser();
  const [team, setTeam] = useState<InterviewTeamType | Object>({});
  // 나중에 location은 타입을 고칠 필요가 있어보임
  const [userInTeam, setUserInTeam] = useState(false);
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
    // teamLocationState && 안 해주면 에러남
    if (teamLocationState && (teamLocationState as InterviewTeamType).id !== undefined) {
      setTeam(teamLocationState);
    }
  }, []);

  return { teamLocationState, team, userInTeam, getTeam };
};
