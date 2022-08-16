import { Link, useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import { ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import Image from 'components/@commons/Image';
import Role from 'components/@commons/Role';
import { LevellogParticipantType } from 'types/levellog';
import { PreQuestionParticipantType } from 'types/preQuestion';
import { RoleType } from 'types/role';
import { ParticipantType } from 'types/team';

const Interviewer = ({
  participant,
  userInTeam,
  role,
  teamStatus,
  onClickOpenLevellogModal,
  onClickOpenPreQuestionModal,
}: InterviewerProps) => {
  const navigate = useNavigate();
  const { teamId } = useParams();
  const { loginUserId } = useUser();

  const handleClickOpenLevellogModal = () => {
    if (typeof teamId === 'string') {
      onClickOpenLevellogModal({ teamId, participant });
    }
  };

  const handleClickOpenPreQuestionModal = () => {
    onClickOpenPreQuestionModal({ participant });
  };

  const handleClickPreQuestionButton = () => {
    navigate(`/pre-questions/teams/${teamId}/levellogs/${participant.levellogId}`);
  };

  const handleClickFeedbackButton = () => {
    navigate(`/teams/${teamId}/levellogs/${participant.levellogId}/feedbacks`);
  };

  if (!teamId) return <div></div>;

  if (participant.memberId === loginUserId) {
    return (
      <S.Container>
        <S.Profile>
          <Image src={participant.profileUrl} sizes={'HUGE'} />
          <S.Nickname>
            <p>{participant.nickname}</p>
          </S.Nickname>
        </S.Profile>
        <S.Content>
          {participant.levellogId ? (
            <>
              <S.InterviewerButton onClick={handleClickOpenLevellogModal}>
                레벨로그 보기
              </S.InterviewerButton>
              <Link to={`/interview-questions/levellogs/${participant.levellogId}`}>
                <S.InterviewerButton disabled={!participant.levellogId || !userInTeam}>
                  인터뷰 질문 보기
                </S.InterviewerButton>
              </Link>
              <Link to={`/teams/${teamId}/levellogs/${participant.levellogId}/feedbacks`}>
                <S.InterviewerButton disabled={!participant.levellogId || !userInTeam}>
                  피드백
                </S.InterviewerButton>
              </Link>
            </>
          ) : (
            <>
              {teamStatus === TEAM_STATUS.READY ? (
                <Link to={`${ROUTES_PATH.LEVELLOG_ADD}/${teamId}`}>
                  <S.InterviewerButton>레벨로그 작성</S.InterviewerButton>
                </Link>
              ) : (
                <S.InterviewerButton disabled>레벨로그 작성</S.InterviewerButton>
              )}
            </>
          )}
        </S.Content>
      </S.Container>
    );
  }

  return (
    <S.Container>
      <S.Profile>
        {role.interviewee && role.interviewer === false && <Role role={'인터뷰이'} />}
        {role.interviewer && role.interviewee === false && <Role role={'인터뷰어'} />}
        {role.interviewee && role.interviewer && <Role role={'상호 인터뷰'} />}
        <Image src={participant.profileUrl} sizes={'HUGE'} />
        <S.Nickname>
          <p>{participant.nickname}</p>
        </S.Nickname>
      </S.Profile>
      <S.Content>
        <S.InterviewerButton
          disabled={!participant.levellogId}
          onClick={handleClickOpenLevellogModal}
        >
          레벨로그 보기
        </S.InterviewerButton>
        {loginUserId && (
          <>
            {participant.preQuestionId ? (
              <S.InterviewerButton
                disabled={!participant.levellogId || !userInTeam}
                onClick={handleClickOpenPreQuestionModal}
              >
                사전 질문 보기
              </S.InterviewerButton>
            ) : (
              <S.InterviewerButton
                disabled={!participant.levellogId || !userInTeam}
                onClick={handleClickPreQuestionButton}
              >
                사전 질문 작성
              </S.InterviewerButton>
            )}
            <S.InterviewerButton
              disabled={!participant.levellogId || !userInTeam}
              onClick={handleClickFeedbackButton}
            >
              피드백
            </S.InterviewerButton>
          </>
        )}
      </S.Content>
    </S.Container>
  );
};

interface InterviewerProps {
  participant: ParticipantType;
  teamStatus: string;
  role: RoleType;
  userInTeam: Boolean;
  onClickOpenLevellogModal: ({ teamId, participant }: LevellogParticipantType) => void;
  onClickOpenPreQuestionModal: ({ participant }: PreQuestionParticipantType) => void;
}

const S = {
  Container: styled.div`
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    width: 15rem;
    height: 18.75rem;
    padding: 1.25rem 1.5rem 1.875rem 1.5rem;
    border: 0.0625rem solid ${(props) => props.theme.default.BLACK};
  `,

  Profile: styled.div`
    position: relative;
    width: 7.5rem;
    height: 8.125rem;
    margin: 0 auto;
  `,

  Nickname: styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 6.25rem;
    left: 0.625rem;
    width: 6.25rem;
    height: 1.875rem;
    border: 0.0625rem solid ${(props) => props.theme.default.BLACK};
    background-color: ${(props) => props.theme.default.WHITE};
  `,

  Content: styled.div`
    display: flex;
    flex-direction: column;
    gap: 1.125rem;
  `,

  InterviewerButton: styled(Button)`
    padding: 0;
    background-color: ${(props) => props.theme.default.INVISIBLE};
    font-size: 1.125rem;
    font-weight: 400;
  `,
};

export default Interviewer;
