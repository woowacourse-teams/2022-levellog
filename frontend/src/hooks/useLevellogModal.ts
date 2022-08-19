import { useState } from 'react';

import useLevellog from 'hooks/useLevellog';

import { LevellogParticipantType } from 'types/levellog';
import { ParticipantType } from 'types/team';

const useLevellogModal = () => {
  const { levellogInfo, getLevellog } = useLevellog();
  const [isLevellogModalOpen, setIsLevellogModalOpen] = useState(false);
  const [levellogParticipant, setLevellogParticipant] = useState({} as ParticipantType);

  const onClickOpenLevellogModal = async ({ teamId, participant }: LevellogParticipantType) => {
    await getLevellog({ teamId, levellogId: participant.levellogId });
    setIsLevellogModalOpen(true);
    setLevellogParticipant(participant);
  };

  const handleClickCloseLevellogModal = () => {
    setIsLevellogModalOpen(false);
  };

  return {
    levellogInfo,
    levellogParticipant,
    isLevellogModalOpen,
    onClickOpenLevellogModal,
    handleClickCloseLevellogModal,
  };
};

export default useLevellogModal;
