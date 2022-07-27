import { useContext, useState } from 'react';

import profileDefaultImage from 'assets/images/defaultProfile.png';
import { LOGIN_PATH } from 'constants/constants';

import { UserContext, UserDispatchContext } from 'contexts/userContext';

const useUser = () => {
  const { id, profileUrl } = useContext(UserContext);
  const userInfoDispatch = useContext(UserDispatchContext);
  const [isShowProfileDropdown, setIsShowProfileDropdown] = useState(false);

  const handleClickProfileImage = () => {
    setIsShowProfileDropdown((prev) => !prev);
  };

  const handleErrorProfileImage = (e: React.SyntheticEvent<EventTarget>) => {
    const target = e.target as HTMLImageElement;
    target.src = `${profileDefaultImage}`;
  };

  const handleClickLogoutButton = () => {
    localStorage.removeItem('accessToken');
    userInfoDispatch({ id: '', profileUrl: '' });
    setIsShowProfileDropdown(false);
  };

  const handleClickLoginButton = () => {
    window.location.href = LOGIN_PATH.LOCAL_HOST;
  };

  return {
    loginUserId: id,
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
