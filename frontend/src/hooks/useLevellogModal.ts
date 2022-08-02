import { useState } from 'react';

import useLevellog from './useLevellog';
import { LevellogCustomHookType, LevellogParticipantType } from 'types/levellog';
import { ParticipantType } from 'types/team';

const useLevellogModal = () => {
  const { levellog, getLevellog, deleteLevellog } = useLevellog();
  const [isOnModal, setIsOnModal] = useState(false);
  const [participant, setParticipant] = useState({} as ParticipantType);

  const onClickToggleModal = ({ teamId, participant }: LevellogParticipantType) => {
    getLevellog({ teamId, levellogId: participant.levellogId });
    setIsOnModal((prev) => !prev);
    setParticipant(participant);
  };

  const onClickDeleteLevellog = async ({
    teamId,
    levellogId,
  }: Omit<LevellogCustomHookType, 'inputValue'>) => {
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
    onClickDeleteLevellog,
    handleClickCloseLevellogModal,
  };
};

export default useLevellogModal;
