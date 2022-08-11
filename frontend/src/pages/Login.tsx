import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetUserAuthority, requestGetUserLogin } from 'apis/login';

const Login = () => {
  const { userInfoDispatch } = useUser();
  const location = useLocation() as unknown as { state: { pathname: string } };
  const navigate = useNavigate();

  useEffect(() => {
    const loginGithub = async () => {
      const params = new URL(window.location.href).searchParams;
      const code = params.get('code');
      const accessToken = localStorage.getItem('accessToken');
      try {
        if (accessToken) {
          const res = await requestGetUserAuthority({ accessToken });
          userInfoDispatch({
            id: res.data.id,
            nickname: res.data.nickname,
            profileUrl: res.data.profileUrl,
          });
          navigate(location.state.pathname);

          return;
        }

        if (code) {
          const res = await requestGetUserLogin({ code });
          localStorage.setItem('accessToken', res.data.accessToken);
          const accessToken = localStorage.getItem('accessToken')!;
          const resLogin = await requestGetUserAuthority({ accessToken });
          userInfoDispatch({
            id: resLogin.data.id,
            nickname: resLogin.data.nickname,
            profileUrl: resLogin.data.profileUrl,
          });
        }

        if (location.state) {
          if (location.state.pathname === ROUTES_PATH.LOGIN) {
            navigate(location.state.pathname);

            return;
          }
          navigate(location.state.pathname);

          return;
        }
        navigate(ROUTES_PATH.HOME);
      } catch (err: unknown) {
        if (axios.isAxiosError(err)) {
          const responseBody: AxiosResponse = err.response!;
          if (err instanceof Error) {
            localStorage.removeItem('accessToken');
            alert(responseBody.data.message);
            navigate(ROUTES_PATH.HOME);
          }
        }
      }
    };

    loginGithub();
  }, []);

  return <h1>로그인 중</h1>;
};

export default Login;
