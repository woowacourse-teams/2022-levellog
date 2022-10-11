import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useUser from 'hooks/useUser';
import useSnackbar from 'hooks/utils/useSnackbar';

import { ROUTES_PATH } from 'constants/constants';

import Loading from './status/Loading';
import { requestGetUserAuthority, requestGetUserLogin } from 'apis/login';
import { NotCorrectToken } from 'apis/utils';

const Login = () => {
  const { loginUserId } = useUser();
  const { userInfoDispatch } = useUser();
  const { showSnackbar } = useSnackbar();
  const navigate = useNavigate();

  const loginGithub = async () => {
    const params = new URL(window.location.href).searchParams;
    const code = params.get('code');
    const accessToken = localStorage.getItem('accessToken');

    try {
      if (accessToken && loginUserId) {
        window.location.replace('/');
      }
      if (code) {
        const res = await requestGetUserLogin({ code });
        const resLogin = await requestGetUserAuthority({ accessToken: res.data.accessToken });

        localStorage.setItem('accessToken', res.data.accessToken);
        localStorage.setItem('userId', res.data.id);
        userInfoDispatch({
          id: resLogin.data.id,
          nickname: resLogin.data.nickname,
          profileUrl: resLogin.data.profileUrl,
        });
      }
      navigate(ROUTES_PATH.HOME, { replace: true });
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  useEffect(() => {
    loginGithub();
  }, []);

  return <Loading />;
};

export default Login;
