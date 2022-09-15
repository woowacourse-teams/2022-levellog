import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

import useUser from 'hooks/useUser';

import Loading from 'pages/status/Loading';

import { ROUTES_PATH } from 'constants/constants';

const AuthLogin = ({ needLogin }: AuthProps) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { loginUserId } = useUser();
  const accessToken = localStorage.getItem('accessToken');

  useEffect(() => {
    if (needLogin && !accessToken) {
      navigate(ROUTES_PATH.HOME, { replace: true });
    }

    if (!loginUserId && accessToken) {
      navigate(ROUTES_PATH.LOGIN, { state: location, replace: true });
    }
  }, [navigate]);

  if (!navigate) return <Loading />;

  return <Outlet />;
};

interface AuthProps {
  needLogin: boolean;
}

export default AuthLogin;
