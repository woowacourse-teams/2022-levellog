import { useParams } from 'react-router-dom';

import styled from 'styled-components';
import { FeedbackType } from 'types';

import useFeedback from 'hooks/useFeedback';
import useUser from 'hooks/useUser';

import Button from '../@commons/Button';
import FlexBox from 'components/@commons/FlexBox';

const Feedback = ({ feedbackId, userFeedback, requestFeedbackLookup }: FeedbackProps) => {
  const { levellogId } = useParams();
  const { feedbackDelete } = useFeedback();
  const { loginUserId } = useUser();
  const {
    id,
    updatedAt,
    from,
    feedback: { study, speak, etc },
  } = userFeedback;

  const handleClickDeleteButton = async () => {
    const res = (await feedbackDelete(levellogId, feedbackId)) as any;
    alert(res.message);
    requestFeedbackLookup();
  };

  return (
    <FeedbackContainer>
      <FeedbackHeader>
        <h3>{from.nickname}의 피드백</h3>
        <Button onClick={handleClickDeleteButton}>삭제하기</Button>
      </FeedbackHeader>
      <FlexBox gap={1.5}>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>학습 측면에서 좋은 점과 부족한 점은?</h3>
          <FeedbackContent>{study}</FeedbackContent>
        </FlexBox>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</h3>
          <FeedbackContent>{speak}</FeedbackContent>
        </FlexBox>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>기타 피드백 (위 2 질문 외에 다른 피드백을 주세요.)</h3>
          <FeedbackContent>{etc}</FeedbackContent>
        </FlexBox>
      </FlexBox>
    </FeedbackContainer>
  );
};

interface FeedbackProps {
  feedbackId: string;
  userFeedback: FeedbackType;
  requestFeedbackLookup: Function;
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
