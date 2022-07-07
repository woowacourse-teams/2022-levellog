import React from 'react';
import styled from 'styled-components';

import LevelLogReport from '../components/LevelLogReport';
import LevelLogFeedback from '../components/LevelLogFeedback';

const FeedbackAdd = () => {
  return (
    <>
      <h1>결의 레벨로그 피드백</h1>
      <FeedbackAddContainer>
        <LevelLogReport />
        <LevelLogFeedback />
      </FeedbackAddContainer>
    </>
  );
};

const FeedbackAddContainer = styled.div`
  display: flex;
`;

export default FeedbackAdd;
