import styled from 'styled-components';

import hamburgerMenu from 'assets/images/hamburger.svg';
import { GITHUB_AVATAR_SIZE_LIST } from 'constants/constants';

import Image from 'components/@commons/Image';

const Profile = ({
  loginUserProfileUrl,
  loginUserNickname,
  handleClickProfile,
  handleErrorProfile,
}: ProfileProps) => {
  return (
    <S.Container
      aria-label={
        loginUserProfileUrl.includes('avatars.githubusercontent')
          ? `${loginUserNickname}의 정보 드롭다운`
          : '로그인'
      }
      onClick={handleClickProfile}
      onError={handleErrorProfile}
    >
      <S.HamburgerBar src={hamburgerMenu} />
      <Image
        src={loginUserProfileUrl}
        sizes={'SMALL'}
        githubAvatarSize={
          loginUserProfileUrl.includes('avatars.githubusercontent')
            ? GITHUB_AVATAR_SIZE_LIST.SMALL
            : 0
        }
      />
    </S.Container>
  );
};

export default Profile;

interface ProfileProps {
  loginUserProfileUrl: string;
  loginUserNickname: string;
  handleClickProfile: (e: React.SyntheticEvent<EventTarget>) => void;
  handleErrorProfile: (e: React.SyntheticEvent<EventTarget>) => void;
}

const S = {
  Container: styled.button`
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 4.8125rem;
    height: 2.625rem;
    padding-left: 0.75rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    border-radius: 2rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    transition: all 0.2s;
    :hover {
      box-shadow: 0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY};
    }
  `,

  HamburgerBar: styled.img`
    width: 1rem;
    height: 1rem;
    color: ${(props) => props.theme.new_default.GRAY};
    cursor: pointer;
  `,
};
