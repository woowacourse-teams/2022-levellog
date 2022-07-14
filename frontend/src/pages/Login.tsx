import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { getUserAuthority } from '../api/login';

import { useUserDispatch } from '../hooks/useContext';

const Login = () => {
  const dispatch = useUserDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const loginGithub = async () => {
      const params = new URL(window.location.href).searchParams;
      const code = params.get('code');
      try {
        const res = await getUserAuthority(code);
        localStorage.setItem('accessToken', res.data.accessToken);
        dispatch(res.data.profileUrl);
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
