import { useState } from 'react';
import { Link } from 'react-router-dom';

import styled from 'styled-components';

import { useUser } from 'hooks/useUser';

import profileDefaultImage from 'assets/images/defaultProfile.png';
import levellogLogo from 'assets/images/levellogLogo.png';

import Button from './@commons/Button';
import ProfileDropdown from './ProfileDropdown';

const Header = () => {
  const [isProfileDropdownShow, setIsProfileDropdownShow] = useState(false);
  const { profileUrl } = useUser();

  const handleErrorProfileImage = (e: React.SyntheticEvent<EventTarget>) => {
    const target = e.target as HTMLImageElement;
    target.src = `${profileDefaultImage}`;
  };

  const handleClickLoginButton = () => {
    window.location.href =
      'https://github.com/login/oauth/authorize?client_id=fc4c9ab6e6d189931371&redirect_uri=http://localhost:3000/login';
  };

  const handleClickProfileImage = () => {
    setIsProfileDropdownShow((prev) => !prev);
  };

  return (
    <HeaderContainer>
      <Link to="/">
        <LogoStyle src={levellogLogo} alt="레벨로그 로고" />
      </Link>
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
    </HeaderContainer>
  );
};

export const HeaderContainer = styled.div`
  display: flex;
  position: relative;
  height: 70px;
  padding: 0 10rem;
  border-bottom: 1px solid #000000;
  justify-content: space-between;
  align-items: center;
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
