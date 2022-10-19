import { ShowSnackbarProps } from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

export const WrongAccessToken = ({ message, showSnackbar }: WrongAccessTokenProps) => {
  if (message === MESSAGE.WRONG_TOKEN) {
    showSnackbar({ message: MESSAGE.NEED_RE_LOGIN });
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userId');
    window.location.replace('/');

    return false;
  }

  return true;
};

interface WrongAccessTokenProps {
  message: string;
  showSnackbar: ({ message }: ShowSnackbarProps) => void;
}
