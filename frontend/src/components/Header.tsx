import React from 'react';
import styled from 'styled-components';
import { useUserState } from '../hooks/useContext';
import Button from './@commons/Button';

const Header = () => {
  const userInfo = useUserState();

  const handleClickLoginButton = () => {
    window.location.href =
      'https://github.com/login/oauth/authorize?client_id=fc4c9ab6e6d189931371&redirect_uri=http://localhost:3000/login';
  };

  const handleErrorProfileImage = (e: React.SyntheticEvent<EventTarget>) => {
    const target = e.target as HTMLImageElement;
    target.src = 'http://localhost:3000/img/defaultProfile.png';
  };

  return (
    <HeaderStyle>
      <LogoStyle src={'http://localhost:3000/img/levellog-logo.png'} alt="레벨로그 로고" />
      <Button onClick={handleClickLoginButton}>로그인</Button>
      <ProfileImgStyle onError={handleErrorProfileImage} src={userInfo} alt="프로필 이미지" />
    </HeaderStyle>
  );
};

export const HeaderStyle = styled.div`
  width: 100%;
  height: 70px;
  border-bottom: 1px solid #000000;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const LogoStyle = styled.img`
  width: 40px;
  height: 42px;
`;

export const ProfileImgStyle = styled.img`
  width: 44px;
  height: 44px;
  border-radius: 22px;
`;

export default Header;
