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

export const NotCorrectToken = ({ message, showSnackbar }: NotCorrectTokenProps) => {
  if (message === MESSAGE.WRONG_TOKEN) {
    showSnackbar({ message: MESSAGE.NEED_RE_LOGIN });
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userId');
    window.location.replace('/');

    return false;
  }

  return true;
};

interface NotCorrectTokenProps {
  message: string;
  showSnackbar: ({ message }: ShowSnackbarProps) => void;
}
