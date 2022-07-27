import { useState } from 'react';

import useLevellog from './useLevellog';

const useLevellogModal = () => {
  const { levellog, getLevellog, deleteLevellog } = useLevellog();
  const [isOnModal, setIsOnModal] = useState(false);
  const [participant, setParticipant] = useState({});

  const onClickToggleModal = ({ teamId, participant }) => {
    getLevellog({ teamId, levellogId: participant.levellogId });
    setIsOnModal((prev) => !prev);
    setParticipant(participant);
  };

  const onClickLevellogDelete = async ({ teamId, levellogId }) => {
    await deleteLevellog({ teamId, levellogId });
    setIsOnModal(false);
  };

  const handleClickCloseLevellogModal = () => {
    setIsOnModal(false);
  };

  return {
    levellog,
    participant,
    isOnModal,
    deleteLevellog,
    onClickToggleModal,
    onClickLevellogDelete,
    handleClickCloseLevellogModal,
  };
};

export default useLevellogModal;
