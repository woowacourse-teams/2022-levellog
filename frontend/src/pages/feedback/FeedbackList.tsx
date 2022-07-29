import { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedback from 'hooks/useFeedback';

import { ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Feedback from 'components/feedbacks/Feedback';
import { FeedbackType } from 'types/feedback';

const FeedbackList = () => {
  const { feedbacks, getFeedbacksInTeam, onClickDeleteButton } = useFeedback();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  if (typeof levellogId !== 'string') {
    alert('잘못된 접근입니다.');
    navigate(ROUTES_PATH.HOME);

    return <div></div>;
  }

  useEffect(() => {
    getFeedbacksInTeam({ levellogId });
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
          feedbacks.map((feedbackInfo: FeedbackType) => (
            <Feedback
              key={feedbackInfo.id}
              levellogId={levellogId}
              feedbackInfo={feedbackInfo}
              onClickDeleteButton={onClickDeleteButton}
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
