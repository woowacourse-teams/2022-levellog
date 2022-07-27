import useUser from './useUser';
import { getUserAuthority } from 'apis/login';

const useAuth = () => {
  const { userInfoDispatch } = useUser();
  const accessToken = localStorage.getItem('accessToken');

  const requestMyInfo = async () => {
    const res = await getUserAuthority({ accessToken });
    // userInfoDispatch()
  };

  return { requestMyInfo };
};

export default useAuth;
