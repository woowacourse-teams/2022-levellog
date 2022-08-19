import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { PATH_TYPE } from 'constants/constants';

import useRouteUri from './useRouteUri';
import { requestGetTeams } from 'apis/teams';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { InterviewTeamType } from 'types/team';

const useTeams = () => {
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const { teamUri } = useRouteUri();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const getTeams = async () => {
    try {
      const res = await requestGetTeams({ accessToken });
      setTeams(res.data.teams);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
      }
    }
  };

  const handleClickInterviewGroup = (e: React.MouseEvent<HTMLElement>) => {
    const target = e.target as HTMLElement;
    const team = teams.find((team) => +team.id === +target.id);

    navigate(teamUri({ pathType: PATH_TYPE.GET }), {
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
