import useAuth from 'hooks/useAuth';

import Error from 'pages/status/Error';
import Loading from 'pages/status/Loading';

const Auth = ({ children, requireAuth }: AuthProps) => {
  const [isLoad, isError] = useAuth({ requireAuth });

  if (isLoad) return <Loading />;
  if (isError) return <Error />;

  return children;
};

interface AuthProps {
  requireAuth: string;
  children: JSX.Element;
}

export default Auth;
