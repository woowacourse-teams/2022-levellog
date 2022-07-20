import { Navigate } from 'react-router-dom';

import { ROUTES_PATH } from '../constants/constants';

function RequireAuth({ children }) {
  const accessToken = localStorage.getItem('accessToken');

  if (!accessToken) {
    return <Navigate to={ROUTES_PATH.HOME} replace></Navigate>;
  }

  return children;
}

export default RequireAuth;
