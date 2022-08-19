import { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedback from 'hooks/useFeedback';
import useTeam from 'hooks/useTeam';
import useUriBuilders from 'hooks/useUriBuilder';
import useUser from 'hooks/useUser';

import EmptyFeedback from 'pages/status/EmptyFeedback';
import Loading from 'pages/status/Loading';

import { MESSAGE, PATH_TYPE, ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import Feedback from 'components/feedbacks/Feedback';
import { FeedbackType } from 'types/feedback';

const Feedbacks = () => {
  const { feedbacks, getFeedbacksInTeam } = useFeedback();
  const { feedbackAddUriBuilder } = useUriBuilders();
  const { team } = useTeam();
  const { loginUserId } = useUser();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  if (typeof levellogId !== 'string' || typeof teamId !== 'string') {
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.ERROR);

    return <Loading />;
  }

  useEffect(() => {
    getFeedbacksInTeam({ levellogId });
  }, []);

  /* 본인의 피드백리스트 페이지에서 `추가하기`버튼 제거해야함 */
  if (feedbacks.length === 0) {
    return (
      <EmptyFeedback
        isShow={team.status !== TEAM_STATUS.CLOSED}
        path={feedbackAddUriBuilder({ teamId, levellogId })}
      />
    );
  }

  return (
    <>
      <ContentHeader title={'레벨 인터뷰 피드백'}>
        <>
          {/* 본인의 피드백리스트 페이지에서 `추가하기`버튼 제거해야함 */}
          {team.status !== TEAM_STATUS.CLOSED && (
            <Link to={feedbackAddUriBuilder({ teamId, levellogId })}>
              <Button>추가하기</Button>
            </Link>
          )}
        </>
      </ContentHeader>
      <S.Container>
        {feedbacks.map((feedbackInfo: FeedbackType) => (
          <Feedback
            key={feedbackInfo.id}
            loginUserId={loginUserId}
            feedbackInfo={feedbackInfo}
            teamStatus={team.status}
            teamId={teamId}
            levellogId={levellogId}
          />
        ))}
      </S.Container>
    </>
  );
};

const S = {
  Container: styled.div`
    margin: auto;
    width: 100%;
    max-width: 71rem;
    min-width: 18.75rem;
    @media (max-width: 1160px) {
      width: calc(100% - 2.5rem);
    }
  `,
};

export default Feedbacks;
