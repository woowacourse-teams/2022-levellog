import { Link } from 'react-router-dom';

import { Exception } from 'pages/status';

import feedbackImage from 'assets/images/feedback.webp';

const EmptyFeedback = ({ isShow, path }: EmptyFeedbackProps) => {
  return (
    <Exception>
      <Exception.Image>{feedbackImage}</Exception.Image>
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
