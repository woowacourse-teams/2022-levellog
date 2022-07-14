import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { getUserAuthority } from '../api/login';

import { useUser } from '../hooks/useContext';

const Login = () => {
  // const profileUrlDispatch = useUserDispatch();
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
