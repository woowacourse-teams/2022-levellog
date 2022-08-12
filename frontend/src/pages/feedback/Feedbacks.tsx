import { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

import useFeedback from 'hooks/useFeedback';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import Feedback from 'components/feedbacks/Feedback';
import { FeedbackType } from 'types/feedback';

const Feedbacks = () => {
  const { feedbacks, getFeedbacksInTeam, onClickDeleteButton } = useFeedback();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  if (typeof levellogId !== 'string') {
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);

    return <div></div>;
  }

  useEffect(() => {
    getFeedbacksInTeam({ levellogId });
  }, []);

  return (
    <>
      <ContentHeader title={'레벨 인터뷰 피드백'}>
        <Link to={`/teams/${teamId}/levellogs/${levellogId}/feedbacks/add`}>
          <Button>추가하기</Button>
        </Link>
      </ContentHeader>
      <FlexBox gap={2.5}>
        {feedbacks.length !== 0 &&
          feedbacks.map((feedbackInfo: FeedbackType) => (
            <Feedback
              key={feedbackInfo.id}
              levellogId={levellogId}
              feedbackInfo={feedbackInfo}
              onClickDeleteButton={onClickDeleteButton}
            />
          ))}
      </FlexBox>
    </>
  );
};

export default Feedbacks;
