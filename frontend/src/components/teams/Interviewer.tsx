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

  if (participant.id === loginUserId) {
    return (
      <>
        <InterviewerContainer>
          <InterviewerStyle>
            <Image src={participant.profileUrl} sizes={'HUGE'} />
            <NicknameStyle>
              <p>{participant.nickname}</p>
            </NicknameStyle>
          </InterviewerStyle>
          <ContentStyle>
            {participant.levellogId ? (
              <InterviewerButton onClick={handleClickToggleModal}>레벨로그 보기</InterviewerButton>
            ) : (
              <Link to={`${ROUTES_PATH.LEVELLOG_ADD}/${teamId}`}>
                <InterviewerButton>레벨로그 작성</InterviewerButton>
              </Link>
            )}
          </ContentStyle>
        </InterviewerContainer>
      </>
    );
  }

  return (
    <>
      <InterviewerContainer>
        <InterviewerStyle>
          <Image src={participant.profileUrl} sizes={'HUGE'} />
          <NicknameStyle>
            <p>{participant.nickname}</p>
          </NicknameStyle>
        </InterviewerStyle>
        <ContentStyle>
          <InterviewerButton disabled={!participant.levellogId} onClick={handleClickToggleModal}>
            레벨로그 보기
          </InterviewerButton>
          <Link to="">
            <InterviewerButton disabled={!participant.levellogId}>사전 질문 작성</InterviewerButton>
          </Link>
          <Link to={`/teams/${teamId}/levellogs/${participant.levellogId}/feedbacks`}>
            <InterviewerButton disabled={!participant.levellogId}>피드백</InterviewerButton>
          </Link>
        </ContentStyle>
      </InterviewerContainer>
    </>
  );
};

interface InterviewerProps {
  participant: ParticipantType;
  onClickToggleModal: ({ teamId, participant }: LevellogParticipantType) => void;
}

const InterviewerButton = styled(Button)`
  padding: 0;
  background-color: ${(props) => props.theme.default.INVISIBLE};
  font-size: 1.125rem;
  font-weight: 400;
`;

const InterviewerContainer = styled.div`
  display: flex;
  width: 240px;
  height: 300px;
  padding: 20px 24px 30px 24px;
  border: 1px solid ${(props) => props.theme.default.BLACK};
  flex-direction: column;
  gap: 1.5rem;
`;

const InterviewerStyle = styled.div`
  position: relative;
  width: 120px;
  height: 130px;
  margin: 0 auto;
`;

const NicknameStyle = styled.div`
  display: flex;
  position: absolute;
  top: 100px;
  left: 10px;
  width: 100px;
  height: 30px;
  border: 1px solid ${(props) => props.theme.default.BLACK};
  background-color: ${(props) => props.theme.default.WHITE};
  justify-content: center;
  align-items: center;
`;

const ContentStyle = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.125rem;
`;

export default Interviewer;
