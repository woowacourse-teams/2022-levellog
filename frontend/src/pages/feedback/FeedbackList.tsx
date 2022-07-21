import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

import styled from 'styled-components';
import { FeedbackType } from 'types';

import useFeedback from 'hooks/useFeedback';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Feedback from 'components/feedbacks/Feedback';

const FeedbackList = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const { levellogId, teamId } = useParams();
  const { feedbackLookup } = useFeedback();

  const requestFeedbackLookup = async () => {
    const res = await feedbackLookup(levellogId);
    setFeedbacks(res.feedbacks);
  };

  useEffect(() => {
    requestFeedbackLookup();
  }, []);

  return (
    <>
      <ContentHeader title={'레벨로그 피드백'}>
        <Link to={`/teams/${teamId}/levellogs/${levellogId}/feedbacks/add`}>
          <Button>추가하기</Button>
        </Link>
      </ContentHeader>
      <FeedbacksContainer>
        {feedbacks.length !== 0 &&
          feedbacks.map((feedback: FeedbackType) => (
            <Feedback
              key={feedback.id}
              feedbackId={String(feedback.id)}
              userFeedback={feedback}
              requestFeedbackLookup={requestFeedbackLookup}
            />
          ))}
      </FeedbacksContainer>
    </>
  );
};

const FeedbacksContainer = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  gap: 40px;
`;

export default FeedbackList;
