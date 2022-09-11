import { useState } from 'react';

import useLevellog from 'hooks/useLevellog';
import useModal from 'hooks/useModal';

import { LevellogParticipantType } from 'types/levellog';
import { ParticipantType } from 'types/team';

const useLevellogModal = () => {
  const { isModalOpen, onClickOpenModal, onClickCloseModal } = useModal();
  const { levellogInfo, getLevellog } = useLevellog();
  const [levellogParticipant, setLevellogParticipant] = useState({} as ParticipantType);

  const onClickOpenLevellogModal = async ({ teamId, participant }: LevellogParticipantType) => {
    await getLevellog({ teamId, levellogId: participant.levellogId });
    onClickOpenModal();
    setLevellogParticipant(participant);
  };

  return {
    levellogInfo,
    levellogParticipant,
    isLevellogModalOpen: isModalOpen,
    onClickOpenLevellogModal,
    handleClickCloseLevellogModal: onClickCloseModal,
  };
};

export default useLevellogModal;
