import styled from 'styled-components';

import hamburgerMenu from 'assets/images/hamburgerMenu.png';

import Image from 'components/@commons/Image';

const Profile = ({
  loginUserProfileUrl,
  isShowProfileDropdown,
  handleClickProfile,
  handleErrorProfile,
}: ProfileProps) => {
  return (
    <S.Container
      isShowProfileDropdown={isShowProfileDropdown}
      onClick={handleClickProfile}
      onError={handleErrorProfile}
    >
      <S.HamburgerBar src={hamburgerMenu} />
      <Image src={loginUserProfileUrl} sizes={'SMALL'} />
    </S.Container>
  );
};

export default Profile;

interface ProfileProps {
  loginUserProfileUrl: string;
  isShowProfileDropdown: boolean;
  handleClickProfile: (e: React.SyntheticEvent<EventTarget>) => void;
  handleErrorProfile: (e: React.SyntheticEvent<EventTarget>) => void;
}

const S = {
  Container: styled.button<{ isShowProfileDropdown: boolean }>`
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 4.8125rem;
    height: 2.625rem;
    padding-left: 0.75rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    border-radius: 2rem;
    // 왜 안 되는거임?
    box-shadow: ${(props) =>
      props.isShowProfileDropdown
        ? '0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY};'
        : ''};
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
