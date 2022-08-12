import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import useTeam from 'hooks/useTeam';

import { MESSAGE, ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

const TeamStatus = ({ needStatus, children }: TeamStatusProps) => {
  const { team } = useTeam();
  const navigate = useNavigate();

  useEffect(() => {
    if (team.status !== needStatus) {
      alert(
        needStatus === TEAM_STATUS.IN_PROGRESS
          ? MESSAGE.INTERVIEW_STATUS_NOT_IN_PROGRESS
          : MESSAGE.INTERVIEW_STATUS_NOT_READY,
      );
      navigate(ROUTES_PATH.HOME);
    }
  }, [navigate]);
  return children;
};

interface TeamStatusProps {
  needStatus: string;
  children: JSX.Element;
}

export default TeamStatus;
