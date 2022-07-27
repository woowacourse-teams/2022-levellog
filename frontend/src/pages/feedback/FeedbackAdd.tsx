import { useEffect } from 'react';
import { useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedback from 'hooks/useFeedback';
import useLevellog from 'hooks/useLevellog';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import FeedbackForm from 'components/feedbacks/FeedbackForm';
import LevellogReport from 'components/levellogs/LevellogReport';

const FeedbackAdd = () => {
  const { feedbackRef, onSubmitFeedbackForm } = useFeedback();
  const { levellog, getLevellog } = useLevellog();
  const { teamId, levellogId } = useParams();

  const handleSubmitFeedbackForm = (e: any) => {
    e.preventDefault();
    onSubmitFeedbackForm({ teamId, levellogId });
  };

  useEffect(() => {
    getLevellog({ teamId, levellogId });
  }, []);

  return (
    <FlexBox gap={1.875}>
      <ContentHeader title={'레벨로그 피드백'}>
        <Button onClick={handleSubmitFeedbackForm}>등록하기</Button>
      </ContentHeader>
      <FeedbackAddContainer>
        <LevellogReport levellog={levellog} />
        <FeedbackForm
          feedbackRef={feedbackRef}
          handleSubmitFeedbackForm={handleSubmitFeedbackForm}
        />
      </FeedbackAddContainer>
    </FlexBox>
  );
};

const FeedbackAddContainer = styled.div`
  display: flex;
  overflow: auto;
  width: 100%;
  gap: 4.875rem;
  @media (max-width: 1024px) {
    gap: 1.875rem;
  }
  @media (max-width: 520px) {
    flex-direction: column;
  }
`;

export default FeedbackAdd;
