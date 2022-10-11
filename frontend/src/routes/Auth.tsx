import useAuth from 'hooks/auth/useAuth';

import ErrorToHome from 'pages/status/ErrorToHome';
import Loading from 'pages/status/Loading';

const Auth = ({ children, requireAuth }: AuthProps) => {
  const { isLoad, isError } = useAuth({ requireAuth });

  if (isLoad) return <Loading />;
  if (isError) return <ErrorToHome />;

  return children;
};

interface AuthProps {
  requireAuth: string;
  children: JSX.Element;
}

export default Auth;
