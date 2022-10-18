import styled from 'styled-components';

import Button from 'components/@commons/Button';

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
    top: 4.75rem;
    right: 0.3125rem;
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
    top: 3.9375rem;
    width: 12.5rem;
    border-bottom: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,

  Content: styled(Button)`
    width: 6.625rem;
    margin-top: 0.625rem;
    padding: 0;
    background-color: ${(props) => props.theme.default.WHITE};
    color: ${(props) => props.theme.default.BLACK};
    text-align: left;
    font-size: 1rem;
    font-weight: 400;
    cursor: pointer;
  `,
};

export default ProfileDropdown;
