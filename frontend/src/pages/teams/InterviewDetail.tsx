import { Link, useParams } from 'react-router-dom';

import styled, { CSSProperties } from 'styled-components';

import useLevellogModal from 'hooks/levellog/useLevellogModal';
import usePreQuestionModal from 'hooks/preQuestion/usePreQuestionModal';
import useTeamDetail from 'hooks/team/useTeamDetail';
import useUser from 'hooks/useUser';

import Loading from 'pages/status/Loading';

import { TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import LevellogViewModal from 'components/levellogs/LevellogViewModal';
import PreQuestionViewModal from 'components/preQuestion/PreQuestionViewModal';
import Interviewer from 'components/teams/Interviewer';
import Watcher from 'components/teams/Watcher';
import { ParticipantType, WatcherType } from 'types/team';
import { convertDateAndTime, teamEditUriBuilder } from 'utils/util';

const InterviewDetail = () => {
  const { loginUserId } = useUser();
  const { team, getTeam, handleClickDeleteTeamButton, handleClickCloseTeamInterviewButton } =
    useTeamDetail();
  const {
    levellogModalLoading,
    levellogParticipant,
    isLevellogModalOpen,
    levellogInfo,
    onClickOpenLevellogModal,
    handleClickCloseLevellogModal,
  } = useLevellogModal();
  const {
    preQuestionModalLoading,
    preQuestion,
    preQuestionParticipant,
    isPreQuestionModalOpen,
    onClickOpenPreQuestionModal,
    onClickDeletePreQuestion,
    handleClickClosePreQuestionModal,
  } = usePreQuestionModal();
  const { teamId } = useParams();

  if (levellogModalLoading) {
    return <Loading />;
  }

  if (preQuestionModalLoading) {
    return <Loading />;
  }

  return (
    <>
      {isLevellogModalOpen && (
        <LevellogViewModal
          teamId={teamId}
          participant={levellogParticipant}
          levellogInfo={levellogInfo}
          userInTeam={team!.isParticipant}
          teamStatus={team!.status}
          handleClickCloseLevellogModal={handleClickCloseLevellogModal}
        />
      )}
      {isPreQuestionModalOpen && (
        <PreQuestionViewModal
          teamId={teamId}
          preQuestion={preQuestion}
          participant={preQuestionParticipant}
          getTeam={getTeam}
          onClickDeletePreQuestion={onClickDeletePreQuestion}
          handleClickClosePreQuestionModal={handleClickClosePreQuestionModal}
        />
      )}

      <ContentHeader
        imageUrl={team?.teamImage}
        title={team?.title}
        subTitle={`${team?.place} | ${convertDateAndTime({
          startAt: team?.startAt,
        })}`}
      >
        <>
          {team?.hostId === loginUserId && (
            <S.ButtonBox>
              {team!.status === TEAM_STATUS.READY && (
                <>
                  <Link to={teamEditUriBuilder({ teamId })}>
                    <S.Button>팀 수정하기</S.Button>
                  </Link>
                  <S.Button onClick={handleClickDeleteTeamButton}>팀 삭제하기</S.Button>
                </>
              )}
              {team!.status === TEAM_STATUS.IN_PROGRESS && (
                <S.InterviewCloseButton onClick={handleClickCloseTeamInterviewButton}>
                  인터뷰 종료하기
                </S.InterviewCloseButton>
              )}
            </S.ButtonBox>
          )}
        </>
      </ContentHeader>
      <S.Container>
        <FlexBox flexFlow={'column wrap'} gap={5}>
          {team?.watchers.length !== 0 && (
            <FlexBox flexFlow={'column wrap'} gap={2}>
              <S.Title>참관자</S.Title>
              <S.WatcherContent>
                {team?.watchers.map(
                  (watcher: Pick<WatcherType, 'memberId' | 'nickname' | 'profileUrl'>) => (
                    <Watcher key={watcher.memberId} watcher={watcher} />
                  ),
                )}
              </S.WatcherContent>
            </FlexBox>
          )}
          <FlexBox flexFlow={'column wrap'} gap={2}>
            <S.Title>참여자</S.Title>
            <S.Content>
              {team?.participants.map((participant: ParticipantType) => (
                <Interviewer
                  key={participant.memberId}
                  participant={participant}
                  interviewees={team?.interviewees}
                  interviewers={team?.interviewers}
                  teamStatus={team?.status}
                  userInTeam={team?.isParticipant}
                  onClickOpenLevellogModal={onClickOpenLevellogModal}
                  onClickOpenPreQuestionModal={onClickOpenPreQuestionModal}
                />
              ))}
            </S.Content>
          </FlexBox>
        </FlexBox>
      </S.Container>
    </>
  );
};

const S = {
  Container: styled.div`
    max-width: 100rem;
    margin: 0 auto 6.25rem auto;
    @media (max-width: 1700px) {
      width: 79.375rem;
    }
    @media (max-width: 1480px) {
      width: 58.75rem;
    }
    @media (max-width: 1024px) {
      width: 38.125rem;
    }
    @media (max-width: 620px) {
      width: 17.5rem;
    }
  `,

  Header: styled.div<CSSProperties>`
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 3.75rem;
    margin-top: 3.125rem;
  `,

  WatcherContent: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
  `,

  Content: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 3.125rem;
  `,

  Title: styled.h1``,

  OwnerImage: styled.div`
    @media (max-width: 620px) {
      display: none;
    }
  `,

  ButtonBox: styled.div`
    display: flex;
    gap: 1rem;
    @media (max-width: 560px) {
      flex-direction: column;
      gap: 0.5rem;
    }
  `,

  InterviewCloseButton: styled(Button)`
    width: fit-content;
    padding: 0.75rem 1.4375rem;
    border: none;
    border-radius: 0.625rem;
    background-color: ${(props) => props.theme.new_default.BLUE};
    font-size: 1.25rem;
    font-weight: 500;
    color: ${(props) => props.theme.new_default.WHITE};
    white-space: pre;
    :hover {
      opacity: 70%;
    }
    @media (max-width: 520px) {
      padding: 0.625rem 0.75rem;
      font-size: 0.875rem;
    }
  `,

  Button: styled(Button)`
    border-radius: 2rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    font-weight: 700;
    color: ${(props) => props.theme.new_default.BLACK};
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    :hover {
      background-color: ${(props) => props.theme.new_default.LIGHT_GRAY};
      box-shadow: 0.25rem 0.25rem 0.375rem ${(props) => props.theme.new_default.DARK_GRAY};
    }
    @media (max-width: 520px) {
      padding: 0.4375rem 0.75rem;
      font-size: 0.75rem;
    }
  `,
};

export default InterviewDetail;
