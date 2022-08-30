import ReactDOM from 'react-dom';

const ModalPortal = ({ children }: ModalPortalProps) => {
  const modal = document.getElementById('modal') as HTMLElement;

  return ReactDOM.createPortal(children, modal);
};

interface ModalPortalProps {
  children: JSX.Element[];
}

export default ModalPortal;
