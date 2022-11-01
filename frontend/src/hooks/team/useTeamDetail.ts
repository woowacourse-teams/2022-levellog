import { useNavigate, useParams } from 'react-router-dom';

import { useMutation, useQuery } from '@tanstack/react-query';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, QUERY_KEY, ROUTES_PATH } from 'constants/constants';

import { requestCloseTeamInterview, requestDeleteTeam, requestGetTeam } from 'apis/teams';

const useTeamDetail = () => {
  const { showSnackbar } = useSnackbar();
  const { teamId } = useParams();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const { refetch: getTeam, data: team } = useQuery(
    [QUERY_KEY.TEAM, accessToken, teamId],
    () => {
      return requestGetTeam({ accessToken, teamId });
    },
    {
      cacheTime: 0,
    },
  );

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
        errorHandler({ err, showSnackbar });
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
        errorHandler({ err, showSnackbar });
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
