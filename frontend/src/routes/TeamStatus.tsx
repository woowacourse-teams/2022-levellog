import { useNavigate, useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { requestGetTeam } from 'apis/teams';
import { TeamStatusType } from 'types/team';

const QUERY_KEY = {
  TEAM: 'team',
};

const TeamStatus = ({ allowedStatuses, children }: TeamStatusProps) => {
  const { teamId } = useParams();

  const accessToken = localStorage.getItem('accessToken');
  const { showSnackbar } = useSnackbar();
  const navigate = useNavigate();

  useQuery(
    [QUERY_KEY.TEAM],
    () => {
      return requestGetTeam({ accessToken, teamId });
    },
    {
      onSuccess: (res) => {
        if (allowedStatuses.some((allowedStatus: any) => allowedStatus === res.status)) {
          return;
        }
        showSnackbar({ message: MESSAGE.WRONG_ACCESS });
        navigate(ROUTES_PATH.HOME);
      },
    },
  );

  return children;
};

interface TeamStatusProps {
  allowedStatuses: TeamStatusType[];
  children: JSX.Element;
}

export default TeamStatus;
