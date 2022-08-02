import { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetTeams, requestGetTeam, requestPostTeam } from 'apis/teams';
import { MemberType } from 'types/member';
import { InterviewTeamType, TeamCustomHookType } from 'types/team';

export const useTeams = () => {
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const teamInfoRef = useRef<HTMLInputElement[]>([]);
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  const postTeams = async ({ ...teamInfo }: TeamCustomHookType) => {
    try {
      await requestPostTeam({ teamInfo, accessToken });
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

  const onSubmitTeamAddForm = async ({ ...participants }: MemberType[]) => {
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    const teamInfo = {
      title: title.value,
      place: place.value,
      startAt: `${date.value}T${time.value}`, // 시작할 시간 포맷 -> 2022-08-01T14:30:00
      interviewerNumber: interviewerNumber.value,
      participants: {
        ids: Object.values(participants).map((participants) => participants.id), // 참가자의 id 배열
      },
    };
    console.log(teamInfo);
    await postTeams({ ...teamInfo });
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
  const [team, setTeam] = useState<InterviewTeamType | Object>({});
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
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.NOT_FOUND);
      }
    }
  };

  useEffect(() => {
    // 나중에 location은 타입을 고칠 필요가 있어보임
    if ((teamLocationState as InterviewTeamType).id !== undefined) {
      setTeam(teamLocationState);
    }
  }, []);

  return { teamLocationState, team, getTeam };
};
