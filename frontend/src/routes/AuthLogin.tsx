import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useUser from 'hooks/useUser';
import useSnackbar from 'hooks/utils/useSnackbar';

import Loading from 'pages/status/Loading';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetUserAuthority } from 'apis/login';
import { NotCorrectToken } from 'apis/utils';

const AuthLogin = ({ needLogin }: AuthProps) => {
  const { userInfoDispatch } = useUser();
  const { showSnackbar } = useSnackbar();
  const navigate = useNavigate();
  const { loginUserId } = useUser();
  const accessToken = localStorage.getItem('accessToken');

  const checkUserAuthority = async () => {
    try {
      if (typeof accessToken === 'string') {
        const res = await requestGetUserAuthority({ accessToken });
        userInfoDispatch({
          id: res.data.id,
          nickname: res.data.nickname,
          profileUrl: res.data.profileUrl,
        });
      }
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
    if (needLogin && !accessToken) {
      navigate(ROUTES_PATH.HOME, { replace: true });
    }

    if (!loginUserId && accessToken) {
      checkUserAuthority();
    }
  }, [navigate]);

  if (!navigate) return <Loading />;

  return <Outlet />;
};

interface AuthProps {
  needLogin: boolean;
}

export default AuthLogin;
