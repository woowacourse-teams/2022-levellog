import { useState } from 'react';

const useSnackbar = () => {
  const [snackbars, setSnackbars] = useState([]);

  const showSnackbar = ({ message }: any) => {
    setSnackbars((prev): any => [...prev, `${message}`]);
    removeSnackbar();
    console.log(snackbars);
  };

  const removeSnackbar = () => {
    let showSnackbarTime = new Date().getTime();
    const callback = () => {
      const currentTime = new Date().getTime();
      if (currentTime - 2000 > showSnackbarTime) {
        setSnackbars((prev) => prev.slice(1, snackbars.length));
      } else {
        requestAnimationFrame(callback);
      }
    };
    requestAnimationFrame(callback);
  };

  return { snackbars, showSnackbar };
};

export default useSnackbar;
