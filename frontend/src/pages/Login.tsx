import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useUser } from 'hooks/useUser';

import { getUserAuthority } from 'apis/login';

const Login = () => {
  const { profileUrlDispatch } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    const loginGithub = async () => {
      const params = new URL(window.location.href).searchParams;
      const code = params.get('code');
      try {
        const res = await getUserAuthority(code);
        localStorage.setItem('accessToken', res.data.accessToken);
        profileUrlDispatch(res.data.profileUrl);
        navigate('/');
      } catch (err) {
        console.log(err);
      }
    };

    loginGithub();
  }, []);

  return <></>;
};

export default Login;
