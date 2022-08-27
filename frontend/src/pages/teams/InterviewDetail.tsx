import { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';

import styled, { CSSProperties } from 'styled-components';

import useLevellogModal from 'hooks/useLevellogModal';
import usePreQuestionModal from 'hooks/usePreQuestionModal';
import useTeam from 'hooks/useTeam';
import useUriBuilders from 'hooks/useUriBuilder';
import useUser from 'hooks/useUser';
import useUtil from 'hooks/useUtil';

import Error from 'pages/status/Error';
import Loading from 'pages/status/Loading';

import { TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import LevellogViewModal from 'components/levellogs/LevellogViewModal';
import PreQuestionViewModal from 'components/preQuestion/PreQuestionViewModal';
import Interviewer from 'components/teams/Interviewer';
import { InterviewTeamType, ParticipantType } from 'types/team';

const InterviewDetail = () => {
  const { loginUserId } = useUser();
  const { teamId } = useParams();
  const { teamEditUriBuilder } = useUriBuilders();
  const { convertDateAndTime } = useUtil();
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
  if (!teamId) return <Loading />;

  return (
    <>
      <ContentHeader
        imageUrl={(team as InterviewTeamType).teamImage}
        title={(team as InterviewTeamType).title}
        subTitle={`${(team as InterviewTeamType).place} | ${convertDateAndTime({
          startAt: (team as InterviewTeamType).startAt,
        })}`}
      >
        <>
          {(team as InterviewTeamType).hostId === loginUserId && (
            <S.ButtonBox>
              {(team as InterviewTeamType).status === TEAM_STATUS.READY && (
                <>
                  <Link to={teamEditUriBuilder({ teamId })}>
                    <S.Button>팀 수정하기</S.Button>
                  </Link>
                  <S.Button onClick={handleClickDeleteTeamButton}>팀 삭제하기</S.Button>
                </>
              )}
              {(team as InterviewTeamType).status === TEAM_STATUS.IN_PROGRESS && (
                <Button onClick={handleClickCloseTeamInterviewButton}>인터뷰 종료하기</Button>
              )}
            </S.ButtonBox>
          )}
        </>
      </ContentHeader>
      <S.Container>
        {isLevellogModalOpen && (
          <LevellogViewModal
            teamId={teamId}
            levellogInfo={levellogInfo}
            participant={levellogParticipant}
            userInTeam={(team as InterviewTeamType).isParticipant}
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
    </>
  );
};

const S = {
  Container: styled.div`
    max-width: 100rem;
    margin: 0 auto 6.25rem auto;
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
      gap: 0.5rem;
    }
  `,

  Button: styled(Button)`
    border-radius: 2rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    font-weight: 700;
    color: ${(props) => props.theme.new_default.BLACK};
    border: 1px solid ${(props) => props.theme.new_default.LIGHT_GRAY};
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
