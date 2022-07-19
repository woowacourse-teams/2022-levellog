import { useNavigate } from 'react-router-dom';

import styled from 'styled-components';

import { useUser } from 'hooks/useUser';

import Button from './@commons/Button';

const ProfileDropdown = ({
  isProfileDropdownShow,
  setIsProfileDropdownShow,
}: ProfileDropdownProps) => {
  const navigate = useNavigate();
  const { userInfoDispatch } = useUser();

  const handleClickProfileButton = () => {
    // profile route가 들어가야 한다.
    navigate('');
  };

  const handleClickLogoutButton = () => {
    localStorage.removeItem('accessToken');
    userInfoDispatch({ id: '', profileUrl: '' });
    setIsProfileDropdownShow(false);
  };

  return (
    <ProfileDropdownStyle isProfileDropdownShow={isProfileDropdownShow}>
      <Button onClick={handleClickProfileButton}>프로필</Button>
      <Button onClick={handleClickLogoutButton}>로그아웃</Button>
    </ProfileDropdownStyle>
  );
};

interface ProfileDropdownProps {
  isProfileDropdownShow: boolean;
  setIsProfileDropdownShow: any;
}

const ProfileDropdownStyle = styled.div`
  width: 106px;
  height: 88px;
  background-color: #b4b4b4;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  border-radius: 8px;
  padding: 10px 0 10px 14px;
  position: absolute;
  top: 70px;
  right: 0;
  transition: all 0.2s;
  opacity: ${(props: Omit<ProfileDropdownProps, 'setIsProfileDropdownShow'>) =>
    props.isProfileDropdownShow ? '0.99' : '0'};
`;

export default ProfileDropdown;
