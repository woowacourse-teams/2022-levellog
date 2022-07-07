import React from 'react';
import styled from 'styled-components';

import LevelLogReport from '../components/LevelLogReport';
import LevelLogFeedback from '../components/LevelLogFeedback';

const FeedbackAdd = () => {
  return (
    <FeedbackAddContainer>
      <LevelLogReport />
      <LevelLogFeedback />
    </FeedbackAddContainer>
  );
};

const FeedbackAddContainer = styled.div`
  display: flex;
`;

export default FeedbackAdd;
