import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import defaultProfile from 'assets/images/defaultProfile.png';
import levellogLogo from 'assets/images/levellogNewLogo.png';

import { LogoStyle } from 'components/@commons/Style';
import Profile from 'components/header/Profile';
import ProfileDropdown from 'components/header/ProfileDropdown';

const Header = () => {
  const {
    loginUserProfileUrl,
    loginUserNickname,
    isShowProfileDropdown,
    handleClickLogoutButton,
    handleClickProfile,
    handleErrorProfile,
  } = useUser();

  return (
    <>
      <S.Container>
        <Link to={'/'}>
          <LogoStyle src={levellogLogo} alt={'레벨로그 로고'} />
        </Link>
        <Profile
          isShowProfileDropdown={isShowProfileDropdown}
          loginUserProfileUrl={loginUserProfileUrl ? loginUserProfileUrl : defaultProfile}
          handleClickProfile={handleClickProfile}
          handleErrorProfile={handleErrorProfile}
        />
        <ProfileDropdown
          isShowProfileDropdown={isShowProfileDropdown}
          loginUserNickname={loginUserNickname}
          handleClickLogoutButton={handleClickLogoutButton}
        />
      </S.Container>
      <S.Line />
    </>
  );
};

const S = {
  Container: styled.header`
    display: flex;
    position: relative;
    justify-content: space-between;
    align-items: center;
    height: 4.375rem;
    @media (min-width: 1620px) {
      padding: 0 calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      padding: 0 1.25rem;
    }
  `,

  ProfileImage: styled.img`
    width: 2.75rem;
    height: 2.75rem;
    border-radius: 1.375rem;
    cursor: pointer;
  `,

  Line: styled.div`
    position: absolute;
    left: 0;
    width: 100%;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,
};

export default Header;
