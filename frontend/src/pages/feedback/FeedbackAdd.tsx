import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedback from 'hooks/useFeedback';
import useLevellog from 'hooks/useLevellog';

import { ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import FeedbackForm from 'components/feedbacks/FeedbackForm';
import LevellogReport from 'components/levellogs/LevellogReport';

const FeedbackAdd = () => {
  const { feedbackRef, onSubmitFeedbackForm } = useFeedback();
  const { levellog, getLevellog } = useLevellog();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  const handleSubmitFeedbackForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      onSubmitFeedbackForm({ teamId, levellogId });

      return;
    }
    alert('잘못된 접근입니다.');
    navigate(ROUTES_PATH.HOME);
  };

  useEffect(() => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellog({ teamId, levellogId });

      return;
    }
    alert('잘못된 접근입니다.');
    navigate(ROUTES_PATH.HOME);
  }, []);

  return (
    <FlexBox gap={1.875}>
      <FeedbackAddContainer onSubmit={handleSubmitFeedbackForm}>
        <ContentHeader title={'레벨로그 피드백'}>
          <Button type={'submit'}>등록하기</Button>
        </ContentHeader>
        <FeedbackFormContainer>
          <LevellogReport levellog={levellog} />
          <FeedbackForm feedbackRef={feedbackRef} />
        </FeedbackFormContainer>
      </FeedbackAddContainer>
    </FlexBox>
  );
};

const FeedbackAddContainer = styled.form`
  overflow: auto;
  width: 100%;
`;

const FeedbackFormContainer = styled.div`
  display: flex;
  overflow: auto;
  gap: 4.875rem;
  @media (max-width: 1024px) {
    gap: 1.875rem;
  }
  @media (max-width: 520px) {
    flex-direction: column;
  }
`;

export default FeedbackAdd;
