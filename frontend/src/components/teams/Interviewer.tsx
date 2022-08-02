import { Link, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import Image from 'components/@commons/Image';
import { LevellogParticipantType } from 'types/levellog';
import { ParticipantType } from 'types/team';

const Interviewer = ({ participant, onClickToggleModal }: InterviewerProps) => {
  const { teamId } = useParams();
  const { loginUserId } = useUser();

  const handleClickToggleModal = () => {
    if (typeof teamId === 'string') {
      onClickToggleModal({ teamId, participant });
    }
  };

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
            <S.InterviewerButton onClick={handleClickToggleModal}>
              레벨로그 보기
            </S.InterviewerButton>
          ) : (
            <Link to={`${ROUTES_PATH.LEVELLOG_ADD}/${teamId}`}>
              <S.InterviewerButton>레벨로그 작성</S.InterviewerButton>
            </Link>
          )}
        </S.Content>
      </S.Container>
    );
  }

  return (
    <S.Container>
      <S.Profile>
        <Image src={participant.profileUrl} sizes={'HUGE'} />
        <S.Nickname>
          <p>{participant.nickname}</p>
        </S.Nickname>
      </S.Profile>
      <S.Content>
        <S.InterviewerButton disabled={!participant.levellogId} onClick={handleClickToggleModal}>
          레벨로그 보기
        </S.InterviewerButton>
        <Link to="">
          <S.InterviewerButton disabled={!participant.levellogId}>
            사전 질문 작성
          </S.InterviewerButton>
        </Link>
        <Link to={`/teams/${teamId}/levellogs/${participant.levellogId}/feedbacks`}>
          <S.InterviewerButton disabled={!participant.levellogId}>피드백</S.InterviewerButton>
        </Link>
      </S.Content>
    </S.Container>
  );
};

interface InterviewerProps {
  participant: ParticipantType;
  onClickToggleModal: ({ teamId, participant }: LevellogParticipantType) => void;
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
