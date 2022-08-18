import { Link, useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import feedbackIcon from 'assets/images/feedbackIcon.svg';
import interviewQuestionIcon from 'assets/images/interviewQuestionIcon.svg';
import levellogIcon from 'assets/images/levellogIcon.svg';
import preQuestionIcon from 'assets/images/preQuestionIcon.svg';
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

  const handleClickInterviewQuestionButton = () => {
    navigate(`/interview-questions/teams/${teamId}/levellogs/${participant.levellogId}`);
  };

  if (!teamId) return <div></div>;

  if (participant.memberId === loginUserId) {
    return (
      <S.Container>
        <S.Profile>
          <Image src={participant.profileUrl} sizes={'HUGE'} />
          <S.NicknameBox>
            <S.Nickname>{participant.nickname}</S.Nickname>
          </S.NicknameBox>
        </S.Profile>
        <S.Content>
          <S.ButtonBox>
            {participant.levellogId ? (
              <>
                <S.Button onClick={handleClickOpenLevellogModal}>
                  <Image src={levellogIcon} sizes={'SMALL'} borderRadius={false} />
                  <S.ButtonText>레벨로그 보기</S.ButtonText>
                </S.Button>
                <S.Button
                  disabled={!participant.levellogId || !userInTeam}
                  onClick={handleClickInterviewQuestionButton}
                >
                  <Image src={interviewQuestionIcon} sizes={'SMALL'} borderRadius={false} />
                  <S.ButtonText>인터뷰질문 보기</S.ButtonText>
                </S.Button>

                <S.Button
                  disabled={!participant.levellogId || !userInTeam}
                  onClick={handleClickFeedbackButton}
                >
                  <Image src={feedbackIcon} sizes={'SMALL'} borderRadius={false} />
                  <S.ButtonText>피드백 보기</S.ButtonText>
                </S.Button>
              </>
            ) : (
              <>
                {teamStatus === TEAM_STATUS.READY ? (
                  <Link to={`${ROUTES_PATH.LEVELLOG_ADD}/${teamId}`}>
                    <S.Button>
                      <Image src={levellogIcon} sizes={'SMALL'} borderRadius={false} />
                      <S.ButtonText>레벨로그 작성</S.ButtonText>
                    </S.Button>
                  </Link>
                ) : (
                  <S.Button disabled>
                    <Image src={levellogIcon} sizes={'SMALL'} borderRadius={false} />
                    <S.ButtonText>레벨로그 작성</S.ButtonText>
                  </S.Button>
                )}
              </>
            )}
          </S.ButtonBox>
        </S.Content>
      </S.Container>
    );
  }

  return (
    <S.Container>
      {role.interviewee && role.interviewer === false && <Role role={'인터뷰이'} />}
      {role.interviewer && role.interviewee === false && <Role role={'인터뷰어'} />}
      {role.interviewee && role.interviewer && <Role role={'상호 인터뷰'} />}
      <S.Profile>
        <Image src={participant.profileUrl} sizes={'HUGE'} />
        <S.NicknameBox>
          <S.Nickname>{participant.nickname}</S.Nickname>
        </S.NicknameBox>
      </S.Profile>
      <S.Content>
        <S.ButtonBox>
          <S.Button disabled={!participant.levellogId} onClick={handleClickOpenLevellogModal}>
            <Image src={levellogIcon} sizes={'SMALL'} borderRadius={false} />
            <S.ButtonText>레벨로그 보기</S.ButtonText>
          </S.Button>
          {loginUserId && (
            <>
              {participant.preQuestionId ? (
                <S.Button
                  disabled={!participant.levellogId || !userInTeam}
                  onClick={handleClickOpenPreQuestionModal}
                >
                  <Image src={preQuestionIcon} sizes={'SMALL'} borderRadius={false} />
                  <S.ButtonText>사전질문 보기</S.ButtonText>
                </S.Button>
              ) : (
                <S.Button
                  disabled={!participant.levellogId || !userInTeam}
                  onClick={handleClickPreQuestionButton}
                >
                  <Image src={preQuestionIcon} sizes={'SMALL'} borderRadius={false} />
                  <S.ButtonText>사전질문 작성</S.ButtonText>
                </S.Button>
              )}
              <S.Button
                disabled={!participant.levellogId || !userInTeam}
                onClick={handleClickFeedbackButton}
              >
                <Image src={feedbackIcon} sizes={'SMALL'} borderRadius={false} />
                <S.ButtonText>피드백 보기</S.ButtonText>
              </S.Button>
            </>
          )}
        </S.ButtonBox>
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
    position: relative;
    width: 17.5rem;
    height: 24rem;
    padding: 1.25rem 2.125rem 0 2.125rem;
    border-radius: 0.625rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    box-shadow: 0.0625rem 0.25rem 0.625rem ${(props) => props.theme.new_default.GRAY};
  `,

  Profile: styled.div`
    position: relative;
    width: 7.5rem;
    height: 8.125rem;
    margin: 0 auto;
  `,

  NicknameBox: styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 6.875rem;
    left: -2.875rem;
    width: 13.25rem;
    height: 2.5rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.GRAY};
    background-color: ${(props) => props.theme.new_default.WHITE};
  `,

  IconImage: styled(Image)`
    border-radius: 0;
  `,

  Nickname: styled.p`
    font-weight: 600;
  `,

  Content: styled.div`
    display: flex;
    flex-direction: column;
    gap: 1.125rem;
  `,

  ButtonBox: styled.div`
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin-top: 2.375rem;
  `,

  Button: styled(Button)`
    display: flex;
    align-items: center;
    gap: 1rem;
    padding: 0.625rem 0.75rem;
    border-radius: 2rem;
    background-color: ${(props) => props.theme.default.INVISIBLE};
    font-size: 1.125rem;
    font-weight: 600;
    color: ${(props) =>
      props.disabled ? props.theme.new_default.GRAY : props.theme.new_default.BLACK};
    :hover {
      box-shadow: 0.25rem 0.25rem 0.25rem ${(props) => props.theme.new_default.GRAY};
    }
  `,

  ButtonText: styled.p`
    font-size: 1.25rem;
  `,
};

export default Interviewer;
