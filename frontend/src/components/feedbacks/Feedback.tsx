import styled from 'styled-components';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import { levellog } from 'mocks/mockData';

const Feedback = ({ feedbackInfo, levellogId, onClickDeleteButton }: FeedbackProps) => {
  const handleClickDeleteButton = () => {
    onClickDeleteButton({ feedbackInfo, levellogId });
  };

  return (
    <FeedbackContainer>
      <FeedbackHeader>
        <h3>{feedbackInfo.from.nickname}의 피드백</h3>
        <Button onClick={handleClickDeleteButton}>삭제하기</Button>
      </FeedbackHeader>
      <FlexBox gap={1.5}>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>학습 측면에서 좋은 점과 부족한 점은?</h3>
          <FeedbackContent>{feedbackInfo.feedback.study}</FeedbackContent>
        </FlexBox>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</h3>
          <FeedbackContent>{feedbackInfo.feedback.speak}</FeedbackContent>
        </FlexBox>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>기타 피드백 (위 2 질문 외에 다른 피드백을 주세요.)</h3>
          <FeedbackContent>{feedbackInfo.feedback.etc}</FeedbackContent>
        </FlexBox>
      </FlexBox>
    </FeedbackContainer>
  );
};

interface FeedbackProps {
  feedbackInfo: any;
  levellogId: string;
  onClickDeleteButton: Function;
}

const FeedbackContainer = styled.div`
  overflow: hidden;
  width: 748px;
  min-width: 748px;
  min-height: 564px;
  padding: 1.25rem 1.5625rem 1.25rem 1.5rem;
  background-color: ${(props) => props.theme.default.WHITE};
  border-radius: 8px;
  @media (max-width: 560px) {
    min-width: 240px;
  }
`;

const FeedbackHeader = styled.header`
  display: flex;
  margin-bottom: 30px;
  justify-content: space-between;
  align-items: center;
`;

const FeedbackContent = styled.p`
  width: 700px;
  min-height: 100px;
  padding: 1rem;
  background-color: ${(props) => props.theme.default.LIGHT_GRAY};
  line-height: 30px;
  word-spacing: 1px;
  border-radius: 8px;
  @media (max-width: 560px) {
    width: 100%;
  }
`;

export default Feedback;
