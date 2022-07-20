import ReactDOM from 'react-dom';

const ModalPortal = ({ children }) => {
  const modal = document.getElementById('modal');
  return ReactDOM.createPortal(children, modal);
};

export default ModalPortal;
