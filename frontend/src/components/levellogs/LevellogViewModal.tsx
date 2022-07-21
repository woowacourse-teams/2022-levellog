import { Link, useParams } from 'react-router-dom';

import ModalPortal from 'ModalPortal';
import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';

import { ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';

const LevellogViewModal = ({
  owner,
  nickname,
  levellogId,
  levellog,
  setIsOnModal,
  requestInterviewTeam,
}) => {
  const { levellogDelete } = useLevellog();
  const { teamId } = useParams();

  const handleClickLevellogDelete = async () => {
    await levellogDelete(teamId, levellogId);
    requestInterviewTeam();
    setIsOnModal(false);
  };

  if (owner === false) {
    return (
      <ModalPortal>
        <Dimmer onClick={() => setIsOnModal(false)}>
          <LevellogModalStyle>
            <Header>
              <ModalTitle>{nickname}의 Levellog</ModalTitle>
              <CloseButton onClick={() => setIsOnModal(false)}>X</CloseButton>
            </Header>
            <Levellog>{levellog}</Levellog>
            <Footer>
              <Link to={ROUTES_PATH.HOME}>
                <Button>사전 질문 작성</Button>
              </Link>
            </Footer>
          </LevellogModalStyle>
        </Dimmer>
      </ModalPortal>
    );
  }

  return (
    <ModalPortal>
      <Dimmer onClick={() => setIsOnModal(false)}>
        <LevellogModalStyle>
          <Header>
            <ModalTitle>나의 Levellog</ModalTitle>
            <CloseButton onClick={() => setIsOnModal(false)}>X</CloseButton>
          </Header>
          <Levellog>{levellog}</Levellog>
          <Footer>
            <Link to={`/levellog/modify/teams/${teamId}/levellogs/${levellogId}`}>
              <Button>수정하기</Button>
            </Link>
            <Button onClick={handleClickLevellogDelete}>삭제하기</Button>
          </Footer>
        </LevellogModalStyle>
      </Dimmer>
    </ModalPortal>
  );
};

const Dimmer = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: ${(props) => props.theme.default.OPACITY_BLACK};
`;

// 모바일 화면 padding 조절?
const LevellogModalStyle = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  width: 46.375rem;
  height: 51.25rem;
  padding: 0.75rem 1.875rem 2.375rem 1.875rem;
  border-radius: 0.5rem;
  background-color: ${(props) => props.theme.default.GRAY};
  transform: translate(-50%, -50%);
  @media (max-width: 51.875rem) {
    width: 35rem;
  }
  @media (max-height: 51.875rem) {
    height: 35rem;
  }
  @media (max-width: 35rem) {
    width: 20rem;
    height: 25rem;
  }
  @media (max-height: 35rem) {
    width: 20rem;
    height: 25rem;
  }
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 3.625rem;
  box-sizing: border-box;
`;

const ModalTitle = styled.h1`
  height: 2.375rem;
  font-size: 1.875rem;
  @media (max-width: 35rem) {
    font-size: 1.25rem;
  }
  @media (max-height: 35rem) {
    font-size: 1.2rem;
  }
`;

const CloseButton = styled.button`
  width: 1.125rem;
  height: 1.125rem;
  border-style: none;
  background-color: rgba(255, 255, 255, 0);
  font-size: 1.375rem;
  font-weight: 800;
  cursor: pointer;
`;

// 모바일 화면 padding 조절?
const Levellog = styled.div`
  box-sizing: border-box;
  width: 42.5rem;
  height: 40.5rem;
  background-color: white;
  @media (max-width: 51.875rem) {
    width: 31.25rem;
  }
  @media (max-height: 51.875rem) {
    width: 31.25rem;
  }
  @media (max-width: 35rem) {
    width: 16.25rem;
    height: 14.375rem;
  }
  @media (max-height: 35rem) {
    width: 16.25rem;
    height: 14.375rem;
  }
`;

const Footer = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  box-sizing: border-box;
  width: 100%;
  height: 3rem;
  padding-top: 1.875rem;
`;

export default LevellogViewModal;
