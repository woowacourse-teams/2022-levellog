import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { Exception } from 'pages/status';

import errorImage from 'assets/images/error.webp';
import { ROUTES_PATH } from 'constants/constants';

const ErrorToHome = () => {
  const navigate = useNavigate();

  useEffect(() => {
    navigate(ROUTES_PATH.HOME);
  }, []);

  return (
    <Exception>
      <Exception.Image sizes={'EXTRA_HUGE'}>{errorImage}</Exception.Image>
      <Exception.Title>예상치 못한 에러가 발생했습니다!</Exception.Title>
    </Exception>
  );
};

export default ErrorToHome;
