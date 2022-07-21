import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

import useUser from 'hooks/useUser';

const Auth = ({ needLogin }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { loginUserId } = useUser();

  const accessToken = localStorage.getItem('accessToken');

  useEffect(() => {
    if (needLogin && !accessToken) {
      alert('로그인이 필요한 페이지입니다.');
      navigate('/');
    }

    if (!loginUserId && accessToken) {
      navigate('/login', { state: location });
      return;
    }
  }, [navigate]);

  return <Outlet />;
};

export default Auth;
