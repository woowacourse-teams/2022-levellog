import React from 'react';
import styled from 'styled-components';

import LevellogReport from '../components/LevellogReport';
import LevellogFeedback from '../components/LevellogFeedback';

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
