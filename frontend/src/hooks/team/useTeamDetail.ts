import { useNavigate, useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useMutation, useQuery } from '@tanstack/react-query';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { requestCloseTeamInterview, requestDeleteTeam, requestGetTeam } from 'apis/teams';
import { NotCorrectToken } from 'apis/utils';

const QUERY_KEY = {
  TEAM: 'team',
};

const useTeamDetail = () => {
  const { showSnackbar } = useSnackbar();
  const { teamId } = useParams();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const { refetch: getTeam, data: team } = useQuery([QUERY_KEY.TEAM], () => {
    return requestGetTeam({ accessToken, teamId });
  });

  const { mutate: deleteTeam } = useMutation(
    () => {
      return requestDeleteTeam({
        teamId,
        accessToken,
      });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.TEAM_DELETE });
        navigate(ROUTES_PATH.HOME);
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const { mutate: closeTeamInterview } = useMutation(
    () => {
      return requestCloseTeamInterview({ teamId, accessToken });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.INTERVIEW_CLOSE });
        navigate(ROUTES_PATH.HOME);
      },
      onError: (err) => {
        if (axios.isAxiosError(err)) {
          const responseBody: AxiosResponse = err.response!;
          if (err instanceof Error) {
            showSnackbar({ message: responseBody.data.message });
            navigate(ROUTES_PATH.HOME);
          }
        }
      },
    },
  );

  const handleClickDeleteTeamButton = async () => {
    if (confirm(MESSAGE.TEAM_DELETE_CONFIRM) && typeof teamId === 'string') {
      deleteTeam();
    }
  };

  const handleClickCloseTeamInterviewButton = () => {
    if (confirm(MESSAGE.INTERVIEW_CLOSE_CONFIRM) && typeof teamId === 'string') {
      closeTeamInterview();
    }
  };

  return {
    team,
    getTeam,
    handleClickDeleteTeamButton,
    handleClickCloseTeamInterviewButton,
  };
};

export default useTeamDetail;
