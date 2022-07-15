import React, { useState } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

import Feedback from '../components/Feedback';
import Button from '../components/@commons/Button';

import useFeedback from '../hooks/useFeedback';

import { ROUTES_PATH } from '../constants/constants';
import { FeedbackType } from '../types';
import { useEffect } from 'react';

const FeedbackList = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const { feedbackLookup } = useFeedback();

  const requestFeedbackLookup = async () => {
    const res = await feedbackLookup();
    setFeedbacks(res.feedbacks);
  };

  useEffect(() => {
    requestFeedbackLookup();
  }, []);

  return (
    <>
      <h1>결의 레벨로그 피드백</h1>
      <Link to={ROUTES_PATH.FEEDBACK_ADD}>
        <Button>제출하기</Button>
      </Link>
      <FeedbacksContainer>
        {feedbacks.length !== 0 &&
          feedbacks.map((feedback: FeedbackType) => (
            <Feedback
              key={feedback.id}
              userFeedback={feedback}
              requestFeedbackLookup={requestFeedbackLookup}
            />
          ))}
      </FeedbacksContainer>
    </>
  );
};

const FeedbacksContainer = styled.div``;

export default FeedbackList;
