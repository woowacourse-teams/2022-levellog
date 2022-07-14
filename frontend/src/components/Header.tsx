import React, { useState } from 'react';
import styled from 'styled-components';

import Button from './@commons/Button';

import { useUserState } from '../hooks/useContext';

import ProfileDropdown from './ProfileDropdown';

const Header = () => {
  const [isProfileDropdownShow, setIsProfileDropdownShow] = useState(false);
  const profileUrl = useUserState();

  const handleErrorProfileImage = (e: React.SyntheticEvent<EventTarget>) => {
    const target = e.target as HTMLImageElement;
    target.src = 'http://localhost:3000/img/defaultProfile.png';
  };

  const handleClickLoginButton = () => {
    window.location.href =
      'https://github.com/login/oauth/authorize?client_id=fc4c9ab6e6d189931371&redirect_uri=http://localhost:3000/login';
  };

  const handleClickProfileImage = () => {
    setIsProfileDropdownShow((prev) => !prev);
  };

  return (
    <HeaderStyle>
      <LogoStyle src={'http://localhost:3000/img/levellog-logo.png'} alt="레벨로그 로고" />
      {profileUrl ? (
        <ProfileImageStyle
          onClick={handleClickProfileImage}
          onError={handleErrorProfileImage}
          src={profileUrl}
          alt="프로필 이미지"
        />
      ) : (
        <Button onClick={handleClickLoginButton}>로그인</Button>
      )}
      <ProfileDropdown
        isProfileDropdownShow={isProfileDropdownShow}
        setIsProfileDropdownShow={setIsProfileDropdownShow}
      />
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
  position: relative;
`;

export const LogoStyle = styled.img`
  width: 40px;
  height: 42px;
`;

export const ProfileImageStyle = styled.img`
  width: 44px;
  height: 44px;
  border-radius: 22px;
  cursor: pointer;
`;

export default Header;
