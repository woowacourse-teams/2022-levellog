import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

import useUser from 'hooks/useUser';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

const Auth = ({ needLogin }: AuthProps) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { loginUserId } = useUser();
  const accessToken = localStorage.getItem('accessToken');

  useEffect(() => {
    if (needLogin && !accessToken) {
      alert(MESSAGE.NEED_LOGIN);
      navigate(ROUTES_PATH.HOME);
    }

    if (!loginUserId && accessToken) {
      navigate(ROUTES_PATH.LOGIN, { state: location, replace: true });

      return;
    }
  }, [navigate]);

  return <Outlet />;
};

interface AuthProps {
  needLogin: boolean;
}

export default Auth;
