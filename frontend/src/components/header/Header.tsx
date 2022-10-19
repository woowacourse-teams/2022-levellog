import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import defaultProfile from 'assets/images/defaultProfile.webp';
import levellogLogo from 'assets/images/levellogLogo.webp';
import { ROUTES_PATH } from 'constants/constants';

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
        <Link to={ROUTES_PATH.HOME} aria-label={'메인 페이지로 이동'}>
          <S.Logo src={levellogLogo} sizes={'MEDIUM'} alt={'레벨로그 로고'} />
        </Link>
        <Profile
          loginUserProfileUrl={loginUserProfileUrl ? loginUserProfileUrl : defaultProfile}
          loginUserNickname={loginUserNickname}
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
    box-sizing: content-box;
    height: 4.3125rem;
    @media (min-width: 1620px) {
      margin: 0 calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      margin: 0 1.25rem;
    }
  `,

  Logo: styled.img`
    height: 34px;
  `,

  ProfileImage: styled.img`
    width: 2.75rem;
    height: 2.75rem;
    border-radius: 1.375rem;
    cursor: pointer;
  `,

  Line: styled.div`
    border-bottom: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,
};

export default Header;
