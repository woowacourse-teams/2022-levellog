import { Link } from 'react-router-dom';

import styled from 'styled-components';

import { ROUTES_PATH } from 'constants/constants';

import Button from '../@commons/Button';

const LevellogViewModal = ({ owner, nickname, levellog, setIsOnModal }) => {
  if (owner === false) {
    return (
      <LevellogModalStyle>
        <Header>
          <h3>{nickname}의 Levellog</h3>
          <CloseButton onClick={setIsOnModal}>X</CloseButton>
        </Header>
        <Levellog>{levellog}</Levellog>
        <Footer>
          <Link to={ROUTES_PATH.HOME}>
            <Button color="#FF0000">사전 질문 작성</Button>
          </Link>
        </Footer>
      </LevellogModalStyle>
    );
  } else {
    return (
      <LevellogModalStyle>
        <Header>
          <h3>나의 Levellog</h3>
          <CloseButton onClick={setIsOnModal}>X</CloseButton>
        </Header>
        <Levellog>{levellog}</Levellog>
        <Footer>
          <Link to={ROUTES_PATH.HOME}>
            <Button color="#FF0000">수정하기</Button>
          </Link>
          <Link to={ROUTES_PATH.HOME}>
            <Button color="#FF0000">삭제하기</Button>
          </Link>
        </Footer>
      </LevellogModalStyle>
    );
  }
};

const Footer = styled.footer`
  width: 100%;
  height: 10%;
  box-sizing: border-box;
  display: flex;
  justify-content: flex-end;
  align-items: center;
`;

const Levellog = styled.div`
  height: 80%;
  width: 100%;
  box-sizing: border-box;
  background-color: white;
`;

const Header = styled.header`
  width: 100%;
  height: 10%;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const CloseButton = styled.button`
  width: 18px;
  height: 18px;
  cursor: pointer;
  background-color: rgba(255, 255, 255, 0);
  border-style: none;
  font-size: 22px;
  font-weight: 800;
`;

const LevellogModalStyle = styled.div`
  padding: 0 20px;
  width: 820px;
  height: 720px;
  background-color: #f1f1f1;
  border-radius: 5px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

export default LevellogViewModal;
