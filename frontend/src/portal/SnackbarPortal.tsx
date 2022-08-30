import ReactDOM from 'react-dom';

const SnackbarPortal = ({ children }: SnackbarPortalProps) => {
  const snackbar = document.getElementById('snackbar') as HTMLElement;

  return ReactDOM.createPortal(children, snackbar);
};

interface SnackbarPortalProps {
  children: JSX.Element;
}

export default SnackbarPortal;
