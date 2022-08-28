import { RequestType } from 'types';

import { ShowSnackbarProps } from 'hooks/useSnackbar';

import { MESSAGE } from 'constants/constants';

export const 엑세스토큰이없는경우헤더제거 = ({
  accessToken,
  method,
  url,
  headers,
}: RequestType) => {
  if (!accessToken) {
    return {
      accessToken,
      method,
      url,
    };
  }

  return { accessToken, method, url, headers };
};

export const 토큰이올바르지못한경우홈페이지로 = ({
  message,
  showSnackbar,
}: 토큰이올바르지못한경우홈페이지로Props) => {
  if (window.location.pathname === '/login') {
    localStorage.removeItem('accessToken');
    window.location.href = '/';

    return false;
  }

  if (message === MESSAGE.WRONG_TOKEN) {
    showSnackbar({ message: MESSAGE.NEED_RE_LOGIN });
    localStorage.removeItem('accessToken');
    window.location.href = '/';

    return false;
  }

  return true;
};

interface 토큰이올바르지못한경우홈페이지로Props {
  message: string;
  showSnackbar: ({ message }: ShowSnackbarProps) => void;
}
