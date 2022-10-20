import { useContext } from 'react';

import { SnackbarDispatchContext } from 'contexts/snackbarContext';

const useSnackbar = () => {
  const dispatch = useContext(SnackbarDispatchContext);

  const showSnackbar = ({ message }: ShowSnackbarProps) => {
    dispatch({ type: 'delete' });
    dispatch({ type: 'add', message });
    removeSnackbar();
  };

  const removeSnackbar = () => {
    let showSnackbarTime = new Date().getTime();
    const callback = () => {
      const currentTime = new Date().getTime();
      if (currentTime - 2000 > showSnackbarTime) {
        dispatch({ type: 'delete' });
      } else {
        requestAnimationFrame(callback);
      }
    };
    requestAnimationFrame(callback);
  };

  return { showSnackbar };
};
export interface ShowSnackbarProps {
  message: string;
}

export default useSnackbar;
