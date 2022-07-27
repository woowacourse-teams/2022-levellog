import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

const Auth = ({ needLogin }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { loginUserId } = useUser();
  const accessToken = localStorage.getItem('accessToken');

  useEffect(() => {
    if (needLogin && !accessToken) {
      alert('로그인이 필요한 페이지입니다.');
      navigate(ROUTES_PATH.HOME);
    }

    if (!loginUserId && accessToken) {
      navigate(ROUTES_PATH.LOGIN, { state: location });
      return;
    }
  }, [navigate]);

  return <Outlet />;
};

export default Auth;
