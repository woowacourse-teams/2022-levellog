import { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

import useFeedback from 'hooks/useFeedback';
import useTeam from 'hooks/useTeam';

import { MESSAGE, ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import Feedback from 'components/feedbacks/Feedback';
import { FeedbackType } from 'types/feedback';

const Feedbacks = () => {
  const { feedbacks, getFeedbacksInTeam } = useFeedback();
  const { teamId, levellogId } = useParams();
  const { team } = useTeam();
  const navigate = useNavigate();

  if (typeof levellogId !== 'string' || typeof teamId !== 'string') {
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
        <>
          {/* 본인의 피드백리스트 페이지에서 `추가하기`버튼 제거해야함 */}
          {team.status !== TEAM_STATUS.CLOSED && (
            <Link to={`/teams/${teamId}/levellogs/${levellogId}/feedbacks/add`}>
              <Button>추가하기</Button>
            </Link>
          )}
        </>
      </ContentHeader>
      <FlexBox gap={2.5}>
        {feedbacks.length !== 0 &&
          feedbacks.map((feedbackInfo: FeedbackType) => (
            <Feedback
              key={feedbackInfo.id}
              feedbackInfo={feedbackInfo}
              teamId={teamId}
              levellogId={levellogId}
              teamStatus={team.status}
            />
          ))}
      </FlexBox>
    </>
  );
};

export default Feedbacks;