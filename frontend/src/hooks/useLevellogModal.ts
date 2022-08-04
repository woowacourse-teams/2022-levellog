import { useState } from 'react';

import useLevellog from 'hooks/useLevellog';

import { LevellogCustomHookType, LevellogParticipantType } from 'types/levellog';
import { ParticipantType } from 'types/team';

const useLevellogModal = () => {
  const { levellog, getLevellog, deleteLevellog } = useLevellog();
  const [isLevellogModalOpen, setIsLevellogModalOpen] = useState(false);
  const [levellogParticipant, setLevellogParticipant] = useState({} as ParticipantType);

  const onClickOpenLevellogModal = async ({ teamId, participant }: LevellogParticipantType) => {
    await getLevellog({ teamId, levellogId: participant.levellogId });
    setIsLevellogModalOpen(true);
    setLevellogParticipant(participant);
  };

  const onClickDeleteLevellog = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
    await deleteLevellog({ teamId, levellogId });
    setIsLevellogModalOpen(false);
  };

  const handleClickCloseLevellogModal = (e: React.MouseEvent<HTMLElement>) => {
    setIsLevellogModalOpen(false);
  };

  return {
    levellog,
    levellogParticipant,
    isLevellogModalOpen,
    deleteLevellog,
    onClickOpenLevellogModal,
    onClickDeleteLevellog,
    handleClickCloseLevellogModal,
  };
};

export default useLevellogModal;
