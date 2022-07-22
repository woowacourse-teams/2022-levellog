import { useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';
import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

import Image from 'components/@commons/Image';
import LevellogViewModal from 'components/levellogs/LevellogViewModal';

const Interviewer = ({ id, levellogId, nickname, profileUrl }: InterviewerProps) => {
  const [levellog, setLevellog] = useState('');
  const [isOnModal, setIsOnModal] = useState(false);
  const navigate = useNavigate();
  const { levellogLookup } = useLevellog();
  const { loginUserId } = useUser();
  const { teamId } = useParams();

  const handleClickToggleModal = () => {
/////
    if (!levellogId) {
      alert('레벨로그가 아직 작성되지 않았습니다.');
      return;
    }
    setIsOnModal((prev) => !prev);
    if (!levellog) {
/////
    if (levellogId && teamId) {
/////
      requestLevellogLookup();
    }
    if (!levellogId || !teamId) {
      alert('작성된 레벨로그가 없습니다.');
      return;
    }
    setIsOnModal((prev) => !prev);
  };

  const handleClickFeedbackButton = () => {
    if (!levellogId) {
      alert('레벨로그가 아직 작성되지 않았습니다.');
      return;
    }

    if (!loginUserId) {
      alert('로그인이 필요합니다.');
      return;
    }
    navigate(`/teams/${teamId}/levellogs/${levellogId}/feedbacks`);
  };

  const requestLevellogLookup = async () => {
    const res = await levellogLookup(teamId, levellogId);
    setLevellog(res.content);
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
////
          {/* <Link to={`/teams/${teamId}/levellogs/${levellogId}/feedbacks`}> */}
          <div onClick={handleClickFeedbackButton}>
            <a>피드백</a>
          </div>
          {/* </Link> */}
/////
          {levellogId ? (
            <Link to={`/levellogs/${levellogId}/feedbacks`}>
              <p>피드백</p>
            </Link>
          ) : (
            <p></p>
          )}
/////
        </ContentStyle>
      </InterviewerContainer>
    </>
  );
};

interface InterviewerProps {
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
