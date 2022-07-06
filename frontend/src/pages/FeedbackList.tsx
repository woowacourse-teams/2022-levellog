import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

import Feedback from '../components/Feedback';
import FeedbackAddButton from '../components/FeedbackAddButton';

import { ROUTES_PATH } from '../constants/constants';

const FeedbackList = () => {
  return (
    <>
      <Link to={ROUTES_PATH.FEEDBACK_ADD}>
        <FeedbackAddButton />
      </Link>
      <FeedbacksContainer>
        <Feedback />
        <Feedback />
      </FeedbacksContainer>
    </>
  );
};

const FeedbacksContainer = styled.div``;

export default FeedbackList;
