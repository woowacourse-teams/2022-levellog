import { useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';

import ModalPortal from 'ModalPortal';
import styled from 'styled-components';

import Button from 'components/@commons/Button';

const LevellogViewModal = ({
  owner,
  nickname,
  levellogId,
  levellog,
  setIsOnModal,
  getTeam,
  deleteLevellog,
}: any) => {
  const { teamId } = useParams();

  const handleClickLevellogDelete = async () => {
    await deleteLevellog({ teamId, levellogId });
    getTeam();
    setIsOnModal(false);
  };

  const handleClickCloseModal = () => {
    setIsOnModal(false);
  };

  if (owner === false) {
    return (
      <ModalPortal>
        <Dimmer onClick={handleClickCloseModal}>
          <LevellogModalStyle>
            <Header>
              <ModalTitle>{nickname}의 Levellog</ModalTitle>
              <CloseButton onClick={handleClickCloseModal}>X</CloseButton>
            </Header>
            <Levellog>{levellog}</Levellog>
            <Footer>
              <Button>사전 질문 작성</Button>
            </Footer>
          </LevellogModalStyle>
        </Dimmer>
      </ModalPortal>
    );
  }

  return (
    <ModalPortal>
      <Dimmer onClick={handleClickCloseModal}>
        <LevellogModalStyle>
          <Header>
            <ModalTitle>나의 Levellog</ModalTitle>
            <CloseButton onClick={handleClickCloseModal}>X</CloseButton>
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

//   <Levellog>{levellog}</Levellog> 추가

const Dimmer = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: ${(props) => props.theme.default.OPACITY_BLACK};
`;

const LevellogModalStyle = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  padding: 0.875rem 1.875rem 1.875rem 1.875rem;
  border-radius: 0.5rem;
  background-color: ${(props) => props.theme.default.GRAY};
  transform: translate(-50%, -50%);
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 3.625rem;
`;

const ModalTitle = styled.h1`
  font-size: 1.875rem;
  @media (max-width: 35rem) {
    font-size: 1.25rem;
  }
  @media (max-height: 35rem) {
    font-size: 1.25rem;
  }
`;

const CloseButton = styled.button`
  width: 1.125rem;
  height: 1.125rem;
  border-style: none;
  background-color: ${(props) => props.theme.default.GRAY};
  font-size: 1.375rem;
  font-weight: 800;
  cursor: pointer;
`;

const Levellog = styled.div`
  overflow: auto;
  width: 42.5rem;
  height: 40.5rem;
  padding: 1rem;
  border-radius: 0.25rem;
  word-spacing: 0.0625rem;
  background-color: ${(props) => props.theme.default.WHITE};
  line-height: 1.875rem;
  @media (max-width: 51.875rem) {
    width: 31.25rem;
  }
  @media (max-height: 51.875rem) {
    height: 31.875rem;
  }
  @media (max-width: 35rem) {
    width: 16.25rem;
  }
  @media (max-height: 40rem) {
    height: 18.75rem;
  }
`;

const Footer = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
  margin-top: 1.25rem;
`;

export default LevellogViewModal;
