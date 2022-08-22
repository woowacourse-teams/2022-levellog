import { useNavigate } from 'react-router-dom';

import useAuth from 'hooks/useAuth';

import Loading from 'pages/status/Loading';

import { ROUTES_PATH } from 'constants/constants';

const Auth = ({ children, requireAuth }: AuthProps) => {
  const { isLoad, isError } = useAuth({ requireAuth });
  const navigate = useNavigate();

  if (isLoad) return <Loading />;
  // 에러시, react-router-dom 에러. navigate 쓰면 안 됌
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
