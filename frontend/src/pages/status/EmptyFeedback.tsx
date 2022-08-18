import { Link } from 'react-router-dom';

import { Exception } from 'pages/status';

import NotFoundImage from 'assets/images/free-icon-cone-7564055.webp';

const EmptyFeedback = ({ isShow, path }: EmptyFeedbackProps) => {
  return (
    <Exception>
      <Exception.Image>{NotFoundImage}</Exception.Image>
      <Exception.Title>작성된 피드백이 없습니다.</Exception.Title>
      {isShow && (
        <Link to={path}>
          <Exception.Button>작성하기</Exception.Button>
        </Link>
      )}
    </Exception>
  );
};

export default EmptyFeedback;

interface EmptyFeedbackProps {
  isShow: Boolean;
  path: string;
}
