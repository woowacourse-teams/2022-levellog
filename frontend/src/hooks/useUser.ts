import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import profileDefaultImage from 'assets/images/defaultProfile.png';
import { GITHUB_LOGIN_URL, ROUTES_PATH } from 'constants/constants';

import { UserContext, UserDispatchContext } from 'contexts/userContext';

const useUser = () => {
  const [isShowProfileDropdown, setIsShowProfileDropdown] = useState(false);
  const { id, nickname, profileUrl } = useContext(UserContext);
  const userInfoDispatch = useContext(UserDispatchContext);
  const navigate = useNavigate();

  const handleClickProfileImage = () => {
    setIsShowProfileDropdown((prev) => !prev);
  };

  const handleErrorProfileImage = (e: React.SyntheticEvent<EventTarget>) => {
    const target = e.target as HTMLImageElement;
    target.src = `${profileDefaultImage}`;
  };

  const handleClickLogoutButton = () => {
    localStorage.removeItem('accessToken');
    userInfoDispatch({ id: '', nickname: '', profileUrl: '' });
    setIsShowProfileDropdown(false);
    navigate(ROUTES_PATH.HOME);
  };

  const handleClickLoginButton = () => {
    window.location.href = GITHUB_LOGIN_URL;
  };

  return {
    loginUserId: id,
    loginUserNickname: nickname,
    loginUserProfileUrl: profileUrl,
    isShowProfileDropdown,
    handleClickProfileImage,
    handleClickLogoutButton,
    handleClickLoginButton,
    handleErrorProfileImage,
    setIsShowProfileDropdown,
    userInfoDispatch,
  };
};

export default useUser;
