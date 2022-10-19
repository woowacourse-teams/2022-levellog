import { Link } from 'react-router-dom';

import { Exception } from 'pages/status';

import QuestionImage from 'assets/images/interviewQuestion.webp';

const EmptyQuestion = ({ isShow, path }: EmptyQuestionProps) => {
  return (
    <Exception>
      <Exception.Image sizes={'EXCEPTION'}>{QuestionImage}</Exception.Image>
      <Exception.Title>작성된 인터뷰 질문이 없습니다.</Exception.Title>
      {isShow && (
        <Link to={path}>
          <Exception.Button>작성하기</Exception.Button>
        </Link>
      )}
    </Exception>
  );
};

export default EmptyQuestion;

interface EmptyQuestionProps {
  isShow: Boolean;
  path: string;
}
