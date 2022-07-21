import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import useUser from 'hooks/useUser';

import { getUserLogin } from 'apis/login';

const Login = () => {
  const { userInfoDispatch } = useUser();
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const loginGithub = async () => {
      const params = new URL(window.location.href).searchParams;
      const code = params.get('code');
      try {
        const res = await getUserLogin(code);
        localStorage.setItem('accessToken', res.data.accessToken);
        userInfoDispatch({
          id: res.data.id,
          profileUrl: res.data.profileUrl,
        });

        const state = location.state as any;

        if (state?.pathname) {
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
