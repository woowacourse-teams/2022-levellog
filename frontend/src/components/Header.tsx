import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import levellogLogo from 'assets/images/levellogLogo.png';

import Button from 'components/@commons/Button';
import { LogoStyle } from 'components/@commons/Style';
import ProfileDropdown from 'components/ProfileDropdown';

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
    <S.Container>
      <Link to={'/'}>
        <LogoStyle src={levellogLogo} alt={'레벨로그 로고'} />
      </Link>
      {loginUserProfileUrl ? (
        <S.ProfileImage
          onClick={handleClickProfileImage}
          onError={handleErrorProfileImage}
          src={loginUserProfileUrl}
          alt={'프로필 이미지'}
        />
      ) : (
        <Button onClick={handleClickLoginButton}>로그인</Button>
      )}
      <ProfileDropdown
        isShowProfileDropdown={isShowProfileDropdown}
        handleClickLogoutButton={handleClickLogoutButton}
      />
    </S.Container>
  );
};

const S = {
  Container: styled.header`
    display: flex;
    position: relative;
    justify-content: space-between;
    align-items: center;
    height: 4.375rem;
    padding: 0 10rem;
    border-bottom: 0.0625rem solid ${(props) => props.theme.default.BLACK};
    @media (max-width: 1024px) {
      padding: 0 5rem;
    }
    @media (max-width: 560px) {
      padding: 0 2.5rem;
    }
  `,

  ProfileImage: styled.img`
    width: 2.75rem;
    height: 2.75rem;
    border-radius: 1.375rem;
    cursor: pointer;
  `,
};

export default Header;
