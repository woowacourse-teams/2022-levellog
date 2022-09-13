import { useState } from 'react';

const useModal = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const onClickOpenModal = () => {
    setIsModalOpen(true);
  };

  const onClickCloseModal = () => {
    setIsModalOpen(false);
  };

  return {
    isModalOpen,
    onClickOpenModal,
    onClickCloseModal,
  };
};

export default useModal;
