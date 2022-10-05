import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import useTeam from 'hooks/team/useTeam';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { TeamStatusType } from 'types/team';

const TeamStatus = ({ allowedStatuses, children }: TeamStatusProps) => {
  const { team } = useTeam();
  const { showSnackbar } = useSnackbar();
  const navigate = useNavigate();

  useEffect(() => {
    if (allowedStatuses.some((allowedStatus: any) => allowedStatus === team.status)) {
      return;
    }
    showSnackbar({ message: MESSAGE.WRONG_ACCESS });
    navigate(ROUTES_PATH.HOME);
  }, [navigate]);

  return children;
};

interface TeamStatusProps {
  allowedStatuses: TeamStatusType[];
  children: JSX.Element;
}

export default TeamStatus;
