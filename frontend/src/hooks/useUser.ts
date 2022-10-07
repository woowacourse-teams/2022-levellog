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

  const handleClickProfile = () => {
    if (id) {
      setIsShowProfileDropdown((prev) => !prev);

      return;
    }
    window.location.href = GITHUB_LOGIN_URL;
  };

  const handleErrorProfile = (e: React.SyntheticEvent<EventTarget>) => {
    const target = e.target as HTMLImageElement;
    target.src = `${profileDefaultImage}`;
  };

  const handleClickLogoutButton = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userId');

    userInfoDispatch({ id: '', nickname: '', profileUrl: '' });
    setIsShowProfileDropdown(false);
    if (location.pathname === ROUTES_PATH.HOME) location.replace(ROUTES_PATH.HOME);
    navigate(ROUTES_PATH.HOME);
  };

  return {
    loginUserId: id,
    loginUserNickname: nickname,
    loginUserProfileUrl: profileUrl,
    isShowProfileDropdown,
    handleClickProfile,
    handleClickLogoutButton,
    handleErrorProfile,
    setIsShowProfileDropdown,
    userInfoDispatch,
  };
};

export default useUser;
