import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';
import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

import Image from 'components/@commons/Image';
import LevellogViewModal from 'components/levellogs/LevellogViewModal';

const Interviewer = ({
  requestInterviewTeam,
  id,
  levellogId,
  nickname,
  profileUrl,
}: InterviewerProps) => {
  const [levellog, setLevellog] = useState('');
  const [isOnModal, setIsOnModal] = useState(false);
  const { levellogLookup } = useLevellog();
  const { loginUserId } = useUser();
  const { teamId } = useParams();

  const requestLevellogLookup = async () => {
    const res = await levellogLookup(teamId, levellogId);
    setLevellog(res.content);
  };

  const handleClickToggleModal = () => {
    setIsOnModal((prev) => !prev);
    if (!levellog) {
      requestLevellogLookup();
    }
  };

  if (id === loginUserId) {
    return (
      <>
        {isOnModal === true && (
          <LevellogViewModal
            owner={true}
            nickname={nickname}
            levellogId={levellogId}
            levellog={levellog}
            setIsOnModal={setIsOnModal}
            requestInterviewTeam={requestInterviewTeam}
          />
        )}
        <InterviewerContainer>
          <InterviewerStyle>
            <Image src={profileUrl} sizes={'HUGE'} />
            <NicknameStyle>
              <p>{nickname}</p>
            </NicknameStyle>
          </InterviewerStyle>
          <ContentStyle>
            {levellogId ? (
              <p onClick={handleClickToggleModal}>레벨로그 보기</p>
            ) : (
              <Link to={`${ROUTES_PATH.LEVELLOG_ADD}/${teamId}`}>
                <p>레벨로그 작성</p>
              </Link>
            )}
          </ContentStyle>
        </InterviewerContainer>
      </>
    );
  }

  return (
    <>
      {isOnModal === true && (
        <LevellogViewModal
          owner={false}
          nickname={nickname}
          levellogId={levellogId}
          levellog={levellog}
          setIsOnModal={setIsOnModal}
          requestInterviewTeam={requestInterviewTeam}
        />
      )}
      <InterviewerContainer>
        <InterviewerStyle>
          <Image src={profileUrl} sizes={'HUGE'} />
          <NicknameStyle>
            <p>{nickname}</p>
          </NicknameStyle>
        </InterviewerStyle>
        <ContentStyle>
          <a onClick={handleClickToggleModal}>레벨로그 보기</a>
          <Link to="">
            <p>사전 질문 작성</p>
          </Link>
          <Link to={`/levellogs/${levellogId}/feedbacks`}>
            <p>피드백</p>
          </Link>
        </ContentStyle>
      </InterviewerContainer>
    </>
  );
};

interface InterviewerProps {
  requestInterviewTeam: () => Promise<void>;
  id: string;
  levellogId: string;
  nickname: string;
  profileUrl: string;
}

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

const InterviewerButton = styled.button``;

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
  gap: 1.5rem;
`;

export default Interviewer;
