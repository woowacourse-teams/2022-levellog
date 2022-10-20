import { Suspense, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedbacks from 'hooks/feedback/useFeedbacks';
import useLevellogQuery from 'hooks/levellog/useLevellogQuery';
import useTeam from 'hooks/team/useTeam';
import useUser from 'hooks/useUser';

import EmptyFeedback from 'pages/status/EmptyFeedback';
import Error from 'pages/status/Error';
import Loading from 'pages/status/Loading';

import plusIcon from 'assets/images/plus.svg';
import { TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Image from 'components/@commons/Image';
import Feedback from 'components/feedbacks/Feedback';
import { FeedbackType } from 'types/feedback';
import { feedbackAddUriBuilder, teamGetUriBuilder } from 'utils/util';

const Feedbacks = () => {
  const { feedbackError, feedbacks } = useFeedbacks();
  const { levellogError, levellogInfo } = useLevellogQuery();
  const { team } = useTeam();
  const { loginUserId } = useUser();
  const { teamId, levellogId } = useParams();

  if (feedbackError) {
    return <Error />;
  }

  if (levellogError) {
    return <Error />;
  }

  if (!team) {
    return <Loading />;
  }

  if (feedbacks?.feedbacks.length === 0) {
    return (
      <Suspense fallback={<Loading />}>
        <ContentHeader
          imageUrl={levellogInfo?.author.profileUrl}
          title={`${levellogInfo?.author.nickname}에 대한 레벨 인터뷰 피드백`}
        >
          <Link to={teamGetUriBuilder({ teamId })}>
            <Button>팀 상세화면 이동</Button>
          </Link>
        </ContentHeader>
        <EmptyFeedback
          isShow={team.status !== TEAM_STATUS.CLOSED && levellogInfo?.author.id !== loginUserId}
          path={feedbackAddUriBuilder({ teamId, levellogId })}
        />
      </Suspense>
    );
  }

  return (
    <Suspense fallback={<Loading />}>
      <ContentHeader
        imageUrl={levellogInfo?.author.profileUrl}
        title={`${levellogInfo?.author.nickname}에 대한 레벨 인터뷰 피드백`}
      >
        <>
          <Link to={teamGetUriBuilder({ teamId })}>
            <Button>팀 상세화면 이동</Button>
          </Link>
          {team.status !== TEAM_STATUS.CLOSED && (
            <Link to={feedbackAddUriBuilder({ teamId, levellogId })}>
              <S.TeamDetailButton>
                {'작성하기'}
                <S.ImageBox>
                  <Image src={plusIcon} sizes={'TINY'} />
                </S.ImageBox>
              </S.TeamDetailButton>
            </Link>
          )}
        </>
      </ContentHeader>
      <S.Container>
        {feedbacks?.feedbacks.map((feedbackInfo: FeedbackType) => (
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
    </Suspense>
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

  TeamDetailButton: styled(Button)`
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    left: 0;
    right: 0;
    bottom: 6.875rem;
    z-index: 10;
    width: 8.125rem;
    height: 3.125rem;
    margin: 0 auto;
    border-radius: 2rem;
    background-color: ${(props) => props.theme.new_default.DARK_BLACK};
    font-size: 1rem;
  `,

  ImageBox: styled.div`
    margin: 0 0 1px 5px;
  `,
};

export default Feedbacks;
