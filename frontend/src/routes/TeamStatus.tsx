import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import useTeam from 'hooks/useTeam';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

const TeamStatus = ({ allowedStatuses, children }: any) => {
  const { team } = useTeam();
  const navigate = useNavigate();

  // 추가로 수정 필요함
  useEffect(() => {
    if (!allowedStatuses.some((needStatus: any) => needStatus === team.status)) {
      alert(MESSAGE.WRONG_ACCESS);
      navigate(ROUTES_PATH.HOME);
    }
  }, [navigate]);

  return children;
};

export default TeamStatus;
