import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { ROUTES_PATH } from 'constants/constants';

const Error = () => {
  const navigate = useNavigate();

  useEffect(() => {
    navigate(ROUTES_PATH.HOME);
  }, []);

  return <h1>에러!</h1>;
};

export default Error;
