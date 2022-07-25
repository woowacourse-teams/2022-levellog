import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import useUser from 'hooks/useUser';

import { getUserAuthority, getUserLogin } from 'apis/login';

const Login = () => {
  const { userInfoDispatch } = useUser();
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const loginGithub = async () => {
      const params = new URL(window.location.href).searchParams;
      const code = params.get('code');
      const state = location.state as any;
      const accessToken = localStorage.getItem('accessToken');
      try {
        if (accessToken) {
          const res = await getUserAuthority(accessToken);
          userInfoDispatch({
            id: res.data.id,
            profileUrl: res.data.profileUrl,
          });
          navigate(state.pathname);
          return;
        }

        const res = await getUserLogin(code);
        localStorage.setItem('accessToken', res.data.accessToken);
        userInfoDispatch({
          id: res.data.id,
          profileUrl: res.data.profileUrl,
        });

        if (state?.pathname) {
          if (location.pathname === '/login') {
            navigate(state.pathname);

            return;
          }

          navigate(state.pathname);

          return;
        }

        navigate('/');
      } catch (err) {
        console.log(err);
      }
    };

    loginGithub();
  }, []);

  return <></>;
};

export default Login;
