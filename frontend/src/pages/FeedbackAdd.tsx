import styled from 'styled-components';

import LevellogFeedback from 'components/feedbacks/LevellogFeedback';
import LevellogReport from 'components/feedbacks/LevellogReport';

const FeedbackAdd = () => {
  return (
    <>
      <h1>결의 레벨로그 피드백</h1>
      <FeedbackAddContainer>
        <LevellogReport />
        <LevellogFeedback />
      </FeedbackAddContainer>
    </>
  );
};

const FeedbackAddContainer = styled.div`
  display: flex;
`;

export default FeedbackAdd;
