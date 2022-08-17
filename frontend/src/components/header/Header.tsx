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
      margin: 0 calc((100vw - 1600px) / 2);
    }
    @media (min-width: 1187.5px) and (max-width: 1620px) {
      margin: 0 calc((100vw - 1187.5px) / 2);
    }
    @media (min-width: 775px) and (max-width: 1220px) {
      margin: 0 calc((100vw - 775px) / 2);
    }
    @media (min-width: 560px) and (max-width: 800px) {
      margin: 0 calc((100vw - 362.5px) / 2);
    }
    @media (max-width: 560px) {
      margin: 0 1.25rem;
    }
  `,

  ProfileImage: styled.img`
    width: 2.75rem;
    height: 2.75rem;
    border-radius: 1.375rem;
    cursor: pointer;
  `,

  Line: styled.div`
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,
};

export default Header;
