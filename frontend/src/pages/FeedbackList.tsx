import React, { useState } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

import Feedback from '../components/Feedback';
import FeedbackAddButton from '../components/FeedbackAddButton';

import { useFeedback } from '../hooks/useFeedback';

import { ROUTES_PATH } from '../constants/constants';
import { FeedbackType } from '../types';
import { useEffect } from 'react';

const FeedbackList = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const { feedbackLookup } = useFeedback();

  useEffect(() => {
    const requsetFeedbackLookup = async () => {
      const res = await feedbackLookup();
      setFeedbacks(res.feedbacks);
    };

    requsetFeedbackLookup();
  }, []);

  return (
    <>
      <Link to={ROUTES_PATH.FEEDBACK_ADD}>
        <FeedbackAddButton />
      </Link>
      <FeedbacksContainer>
        {feedbacks.length !== 0 &&
          feedbacks.map((feedback: FeedbackType) => (
            <Feedback key={feedback.id} userFeedback={feedback} />
          ))}
      </FeedbacksContainer>
    </>
  );
};

const FeedbacksContainer = styled.div``;

export default FeedbackList;
