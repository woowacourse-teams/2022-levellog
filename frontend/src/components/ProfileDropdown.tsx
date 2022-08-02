import styled from 'styled-components';

const ProfileDropdown = ({
  isShowProfileDropdown,
  handleClickLogoutButton,
}: ProfileDropdownProps) => {
  return (
    <S.Container isShowProfileDropdown={isShowProfileDropdown}>
      <S.Content>프로필</S.Content>
      <S.Content onClick={handleClickLogoutButton}>로그아웃</S.Content>
    </S.Container>
  );
};

interface ProfileDropdownProps {
  isShowProfileDropdown: boolean;
  handleClickLogoutButton: () => void;
}

const S = {
  Container: styled.div`
    display: ${(props: Pick<ProfileDropdownProps, 'isShowProfileDropdown'>) =>
      props.isShowProfileDropdown ? 'flex' : 'none'};
    position: absolute;
    top: 4.375rem;
    right: 10rem;
    z-index: 10;
    flex-direction: column;
    justify-content: space-around;
    width: 6.625rem;
    height: 5.5rem;
    padding: 0.625rem 0 0.625rem 0.875rem;
    border-radius: 0.5rem;
    background-color: ${(props) => props.theme.default.GRAY};
    transition: all 0.2s;
    @media (max-width: 1024px) {
      right: 5rem;
    }
    @media (max-width: 560px) {
      right: 2.5rem;
    }
  `,

  Content: styled.div`
    width: 6.625rem;
    margin-bottom: 0.625rem;
    color: ${(props) => props.theme.default.WHITE};
    font-weight: 700;
  `,
};

export default ProfileDropdown;
