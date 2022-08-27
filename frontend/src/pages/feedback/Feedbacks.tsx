import { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedback from 'hooks/useFeedback';
import useLevellog from 'hooks/useLevellog';
import useTeam from 'hooks/useTeam';
import useUriBuilders from 'hooks/useUriBuilder';
import useUser from 'hooks/useUser';

import EmptyFeedback from 'pages/status/EmptyFeedback';
import Loading from 'pages/status/Loading';

import { MESSAGE, ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Feedback from 'components/feedbacks/Feedback';
import { FeedbackType } from 'types/feedback';

const Feedbacks = () => {
  const { feedbacks, getFeedbacksInTeam } = useFeedback();
  const { levellogInfo, getLevellog } = useLevellog();
  const { feedbackAddUriBuilder } = useUriBuilders();
  const { team } = useTeam();
  const { loginUserId, loginUserNickname, loginUserProfileUrl } = useUser();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  if (typeof levellogId !== 'string' || typeof teamId !== 'string') {
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.ERROR);

    return <Loading />;
  }

  useEffect(() => {
    getLevellog({ teamId, levellogId });
    getFeedbacksInTeam({ levellogId });
  }, []);

  if (
    !loginUserId ||
    !loginUserNickname ||
    !loginUserProfileUrl ||
    Object.keys(levellogInfo).length === 0
  ) {
    return <Loading />;
  }

  if (feedbacks.length === 0) {
    return (
      <>
        <ContentHeader
          imageUrl={levellogInfo.author.profileUrl}
          title={`${levellogInfo.author.nickname}에 대한 레벨 인터뷰 피드백`}
        ></ContentHeader>
        <EmptyFeedback
          isShow={team.status !== TEAM_STATUS.CLOSED && levellogInfo.author.id !== loginUserId}
          path={feedbackAddUriBuilder({ teamId, levellogId })}
        />
      </>
    );
  }

  return (
    <>
      <ContentHeader
        imageUrl={levellogInfo.author.profileUrl}
        title={`${levellogInfo.author.nickname}에 대한 레벨 인터뷰 피드백`}
      >
        <>
          {/*내가 피드백을 작성했다면 '작성하기' 버튼 제거하기*/}
          {team.status !== TEAM_STATUS.CLOSED && (
            <Link to={feedbackAddUriBuilder({ teamId, levellogId })}>
              <Button>작성하기</Button>
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
