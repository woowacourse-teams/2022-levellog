import { useNavigate } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

const ProfileDropdown = ({
  isProfileDropdownShow,
  setIsProfileDropdownShow,
}: ProfileDropdownProps) => {
  const navigate = useNavigate();
  const { userInfoDispatch } = useUser();

  const handleClickProfileButton = () => {
    navigate('');
  };

  const handleClickLogoutButton = () => {
    localStorage.removeItem('accessToken');
    userInfoDispatch({ id: '', profileUrl: '' });
    setIsProfileDropdownShow(false);
  };

  return (
    <ProfileDropdownContainer isProfileDropdownShow={isProfileDropdownShow}>
      <ProfileDropdownContent onClick={handleClickProfileButton}>프로필</ProfileDropdownContent>
      <ProfileDropdownContent onClick={handleClickLogoutButton}>로그아웃</ProfileDropdownContent>
    </ProfileDropdownContainer>
  );
};

interface ProfileDropdownProps {
  isProfileDropdownShow: boolean;
  setIsProfileDropdownShow: any;
}

const ProfileDropdownContainer = styled.div`
  display: flex;
  position: absolute;
  top: 70px;
  right: 10rem;
  width: 106px;
  height: 88px;
  padding: 10px 0 10px 14px;
  background-color: #b4b4b4;
  flex-direction: column;
  justify-content: space-around;
  border-radius: 8px;
  transition: all 0.2s;
  display: ${(props: Omit<ProfileDropdownProps, 'setIsProfileDropdownShow'>) =>
    props.isProfileDropdownShow ? 'block' : 'none'};
  z-index: 10;
  @media (max-width: 1024px) {
    right: 5rem;
  }
  @media (max-width: 560px) {
    right: 2.5rem;
  }
`;

const ProfileDropdownContent = styled.div`
  width: 106px;
  margin-bottom: 0.625rem;
  color: ${(props) => props.theme.default.WHITE};
  font-weight: 700;
`;

export default ProfileDropdown;
