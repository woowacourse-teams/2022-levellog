import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

const AuthLogin = ({ needLogin }: AuthProps) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { loginUserId } = useUser();
  const accessToken = localStorage.getItem('accessToken');

  useEffect(() => {
    if (needLogin && !accessToken) {
      navigate(ROUTES_PATH.HOME);
    }

    if (!loginUserId && accessToken) {
      navigate(ROUTES_PATH.LOGIN, { state: location, replace: true });
      // 이 return 필요함?
      return;
    }
  }, [navigate]);

  return <Outlet />;
};

interface AuthProps {
  needLogin: boolean;
}

export default AuthLogin;
