import { useNavigate } from 'react-router-dom';

import useAuth from 'hooks/useAuth';

import Loading from 'pages/status/Loading';

import { ROUTES_PATH } from 'constants/constants';

const Auth = ({ children, requireAuth }: AuthProps) => {
  const { isLoad, isError } = useAuth({ requireAuth });
  const navigate = useNavigate();

  if (isLoad) return <Loading />;
  // 동작 체크 필요
  if (isError) {
    navigate(ROUTES_PATH.HOME);
    return <Loading />;
  }

  return children;
};

interface AuthProps {
  requireAuth: string;
  children: JSX.Element;
}

export default Auth;
