import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetTeams } from 'apis/teams';
import { InterviewTeamType } from 'types/team';

const useTeams = () => {
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');

  const getTeams = async () => {
    try {
      const res = await requestGetTeams({ accessToken });
      setTeams(res.data.teams);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
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

  return {
    teams,
    getTeams,
    handleClickInterviewGroup,
  };
};

export default useTeams;
