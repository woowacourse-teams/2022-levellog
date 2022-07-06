import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

import Feedback from '../components/Feedback';
import FeedbackAddButton from '../components/FeedbackAddButton';

import { useFeedback } from '../hooks/useFeedback';

import { ROUTES_PATH } from '../constants/constants';
import { FeedbackType } from '../types';

const FeedbackList = () => {
  const { useFeedbackLookup } = useFeedback();
  const feedbacks = useFeedbackLookup();
  console.log(feedbacks);
  return (
    <>
      <Link to={ROUTES_PATH.FEEDBACK_ADD}>
        <FeedbackAddButton />
      </Link>
      <FeedbacksContainer>
        {/* {feedbacks &&
          feedbacks.map((feedback: FeedbackType) => {
            <Feedback feedback={feedback} />;
          })} */}
        <Feedback />
        <Feedback />
      </FeedbacksContainer>
    </>
  );
};

const FeedbacksContainer = styled.div``;

export default FeedbackList;
