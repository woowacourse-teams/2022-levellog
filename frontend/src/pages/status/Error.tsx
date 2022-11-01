import { Link } from 'react-router-dom';

import { Exception } from 'pages/status';

import errorImage from 'assets/images/error.webp';

const Error = ({ onClick }: ErrorProps) => {
  return (
    <Exception>
      <Exception.Image sizes={'EXTRA_HUGE'}>{errorImage}</Exception.Image>
      <Exception.Title>예상치 못한 에러가 발생했습니다!</Exception.Title>
      <Link to="/">
        <Exception.Button onClick={onClick}>홈으로 돌아가기</Exception.Button>
      </Link>
    </Exception>
  );
};

interface ErrorProps {
  onClick?: () => void;
}

export default Error;
