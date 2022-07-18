import { Link } from 'react-router-dom';

import styled from 'styled-components';
import { ParticipantType } from 'types';

import profileDefaultImage from 'assets/images/defaultProfile.png';

import Image from './@commons/Image';

const Interviewer = ({ id, levellogId, nickname, profileUrl }: ParticipantType) => {
  return (
    <InterviewerContainer>
      <InterviewerStyle>
        <Image src={profileUrl} sizes={'HUGE'} />
        <NicknameStyle>
          <p>{nickname}</p>
        </NicknameStyle>
      </InterviewerStyle>
      <ContentStyle>
        <p id={levellogId}>레벨로그 보기</p>
        <Link to="">
          <p>사전 질문 작성</p>
        </Link>
        <Link to="/feedback">
          <p>피드백</p>
        </Link>
      </ContentStyle>
    </InterviewerContainer>
  );
};

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
