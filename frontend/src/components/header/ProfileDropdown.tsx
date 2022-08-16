import styled from 'styled-components';

const ProfileDropdown = ({
  isShowProfileDropdown,
  loginUserNickname,
  handleClickLogoutButton,
}: ProfileDropdownProps) => {
  return (
    <S.Container isShowProfileDropdown={isShowProfileDropdown}>
      <S.Greeting>안녕하세요!</S.Greeting>
      <S.Name>{loginUserNickname}</S.Name>
      <S.Line />
      <S.Content onClick={handleClickLogoutButton}>로그아웃</S.Content>
    </S.Container>
  );
};

interface ProfileDropdownProps {
  isShowProfileDropdown: boolean;
  loginUserNickname: string;
  handleClickLogoutButton: () => void;
}

const S = {
  Container: styled.div<{ isShowProfileDropdown: boolean }>`
    display: ${(props) => (props.isShowProfileDropdown ? 'flex' : 'none')};
    position: absolute;
    top: 4.375rem;
    right: 10rem;
    z-index: 10;
    flex-direction: column;
    justify-content: space-around;
    width: 12.5rem;
    height: fit-content;
    padding: 0.625rem 0 0.625rem 0.875rem;
    box-shadow: 0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY};
    border-radius: 0.5rem;
    background-color: ${(props) => props.theme.default.WHITE};
    transition: all 0.2s;
    @media (max-width: 1024px) {
      right: 5rem;
    }
    @media (max-width: 560px) {
      right: 2.5rem;
    }
  `,

  Greeting: styled.div`
    font-weight: 300;
  `,

  Name: styled.h4`
    padding: 0.625rem 0;
    font-size: 1.25rem;
  `,

  Line: styled.div`
    position: absolute;
    left: 0;
    top: 3.75rem;
    width: 12.5rem;
    border-bottom: 1px solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,

  Content: styled.div`
    width: 6.625rem;
    margin-top: 0.625rem;
    color: ${(props) => props.theme.default.BLACK};
    font-weight: 400;
    cursor: pointer;
  `,
};

export default ProfileDropdown;
