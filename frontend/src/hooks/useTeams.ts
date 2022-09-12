import { useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import useSnackbar from 'hooks/useSnackbar';

import { requestGetTeams } from 'apis/teams';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { InterviewTeamType } from 'types/team';

const useTeams = () => {
  const { showSnackbar } = useSnackbar();
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);

  const accessToken = localStorage.getItem('accessToken');

  const getTeams = async () => {
    try {
      const res = await requestGetTeams({ accessToken });
      setTeams(res.data.teams);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (
          토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
        ) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  return {
    teams,
    getTeams,
  };
};

export default useTeams;
