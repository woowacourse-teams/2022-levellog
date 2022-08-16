import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import useTeam from 'hooks/useTeam';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

const TeamStatus = ({ allowedStatuses, children }: TeamStatusProps) => {
  const { team } = useTeam();
  const navigate = useNavigate();

  useEffect(() => {
    if (allowedStatuses.some((allowedStatus: any) => allowedStatus === team.status)) {
      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  }, [navigate]);

  return children;
};

interface TeamStatusProps {
  allowedStatuses: string[];
  children: JSX.Element;
}

export default TeamStatus;
