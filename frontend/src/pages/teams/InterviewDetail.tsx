import { useEffect } from 'react';
import { Link } from 'react-router-dom';

import styled, { CSSProperties } from 'styled-components';

import useLevellogModal from 'hooks/useLevellogModal';
import usePreQuestionModal from 'hooks/usePreQuestionModal';
import useTeam from 'hooks/useTeam';
import useUser from 'hooks/useUser';

import Error from 'pages/status/Error';

import { TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import LevellogViewModal from 'components/levellogs/LevellogViewModal';
import PreQuestionViewModal from 'components/preQuestion/PreQuestionViewModal';
import Interviewer from 'components/teams/Interviewer';
import { InterviewTeamType, ParticipantType } from 'types/team';

const InterviewDetail = () => {
  const { loginUserId } = useUser();
  const {
    teamLocationState,
    team,
    getTeam,
    onClickDeleteTeamButton,
    onClickCloseTeamInterviewButton,
  } = useTeam();
  const {
    levellogInfo,
    levellogParticipant,
    isLevellogModalOpen,
    onClickOpenLevellogModal,
    handleClickCloseLevellogModal,
  } = useLevellogModal();
  const {
    preQuestion,
    preQuestionParticipant,
    isPreQuestionModalOpen,
    onClickOpenPreQuestionModal,
    onClickDeletePreQuestion,
    handleClickClosePreQuestionModal,
  } = usePreQuestionModal();

  const handleClickDeleteTeamButton = () => {
    onClickDeleteTeamButton({ teamId: (team as InterviewTeamType).id });
  };

  const handleClickCloseTeamInterviewButton = () => {
    onClickCloseTeamInterviewButton({ teamId: (team as InterviewTeamType).id });
  };

  useEffect(() => {
    if (!teamLocationState) {
      getTeam();
    }
  }, []);

  if (team && Object.keys(team).length === 0) return <Error />;

  return (
    <S.Container>
      {isLevellogModalOpen && (
        <LevellogViewModal
          levellogInfo={levellogInfo}
          participant={levellogParticipant}
          userInTeam={(team as InterviewTeamType).isParticipant}
          handleClickCloseLevellogModal={handleClickCloseLevellogModal}
        />
      )}
      {isPreQuestionModalOpen && (
        <PreQuestionViewModal
          preQuestion={preQuestion}
          participant={preQuestionParticipant}
          getTeam={getTeam}
          onClickDeletePreQuestion={onClickDeletePreQuestion}
          handleClickClosePreQuestionModal={handleClickClosePreQuestionModal}
        />
      )}
      <S.Header>
        <FlexBox gap={1}>
          <S.OwnerImage>
            <Image src={(team as InterviewTeamType).teamImage} sizes={'LARGE'} />
          </S.OwnerImage>
          <FlexBox flexFlow="column" gap={1.125}>
            <S.Title>{(team as InterviewTeamType).title}</S.Title>
            <FlexBox gap={1}>
              <S.TitleContent>{(team as InterviewTeamType).place}</S.TitleContent>
              <S.TitleContent>{(team as InterviewTeamType).startAt}</S.TitleContent>
            </FlexBox>
          </FlexBox>
        </FlexBox>
        {(team as InterviewTeamType).hostId === loginUserId && (
          <S.ButtonBox>
            {(team as InterviewTeamType).status === TEAM_STATUS.READY && (
              <>
                <Link to={`/interview/teams/${(team as InterviewTeamType).id}/edit`}>
                  <Button>팀 수정하기</Button>
                </Link>
                <Button onClick={handleClickDeleteTeamButton}>팀 삭제하기</Button>
              </>
            )}
            {(team as InterviewTeamType).status === TEAM_STATUS.IN_PROGRESS && (
              <Button onClick={handleClickCloseTeamInterviewButton}>인터뷰 종료하기</Button>
            )}
          </S.ButtonBox>
        )}
      </S.Header>
      <S.Content>
        {(team as InterviewTeamType).participants.map((participant: ParticipantType) => {
          const role = {
            interviewee: false,
            interviewer: false,
          };
          if (loginUserId) {
            role.interviewee = (team as InterviewTeamType).interviewees.includes(
              Number(participant.memberId),
            );
            role.interviewer = (team as InterviewTeamType).interviewers.includes(
              Number(participant.memberId),
            );
          }
          return (
            <Interviewer
              key={participant.memberId}
              teamStatus={team.status}
              participant={participant}
              role={role}
              userInTeam={(team as InterviewTeamType).isParticipant}
              onClickOpenLevellogModal={onClickOpenLevellogModal}
              onClickOpenPreQuestionModal={onClickOpenPreQuestionModal}
            />
          );
        })}
      </S.Content>
    </S.Container>
  );
};

const S = {
  Container: styled.div`
    max-width: 1600px;
    margin: auto;
    @media (max-width: 1700px) {
      width: 1270px;
    }
    @media (max-width: 1480px) {
      width: 940px;
    }
    @media (max-width: 1024px) {
      width: 610px;
    }
    @media (max-width: 620px) {
      width: 280px;
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

  Content: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 3.125rem;
  `,

  Title: styled.h3`
    width: 11.5rem;
    word-break: break-all;
  `,

  TitleContent: styled.p`
    word-break: break-all;
  `,

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
    }
  `,
};

export default InterviewDetail;
