import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import levellogLogo from 'assets/images/levellogLogo.png';

import Button from './@commons/Button';
import { LogoStyle } from './@commons/Style';
import ProfileDropdown from './ProfileDropdown';

const Header = () => {
  const {
    loginUserProfileUrl,
    isShowProfileDropdown,
    handleClickLogoutButton,
    handleClickProfileImage,
    handleClickLoginButton,
    handleErrorProfileImage,
  } = useUser();

  return (
    <HeaderContainer>
      <Link to="/">
        <LogoStyle src={levellogLogo} alt="레벨로그 로고" />
      </Link>
      {loginUserProfileUrl ? (
        <ProfileImageStyle
          onClick={handleClickProfileImage}
          onError={handleErrorProfileImage}
          src={loginUserProfileUrl}
          alt="프로필 이미지"
        />
      ) : (
        <Button onClick={handleClickLoginButton}>로그인</Button>
      )}
      <ProfileDropdown
        isShowProfileDropdown={isShowProfileDropdown}
        handleClickLogoutButton={handleClickLogoutButton}
      />
    </HeaderContainer>
  );
};

export const HeaderContainer = styled.header`
  display: flex;
  position: relative;
  height: 4.375rem;
  padding: 0 10rem;
  border-bottom: 1px solid #000000;
  justify-content: space-between;
  align-items: center;
  @media (max-width: 1024px) {
    padding: 0 5rem;
  }
  @media (max-width: 560px) {
    padding: 0 2.5rem;
  }
`;

const ProfileImageStyle = styled.img`
  width: 44px;
  height: 44px;
  border-radius: 22px;
  cursor: pointer;
`;

export default Header;
