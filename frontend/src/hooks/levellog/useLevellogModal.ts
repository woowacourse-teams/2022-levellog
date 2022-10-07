import { useState } from 'react';
import { useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';

import useModal from 'hooks/useModal';

import { requestGetLevellog } from 'apis/levellog';
import { LevellogParticipantType } from 'types/levellog';
import { ParticipantType } from 'types/team';

const useLevellogModal = () => {
  const { isModalOpen, onClickOpenModal, onClickCloseModal } = useModal();
  const [levellogParticipant, setLevellogParticipant] = useState({} as ParticipantType);
  const { teamId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const {
    isLoading: levellogModalLoading,
    mutate: getLevellog,
    data: levellogInfo,
  } = useMutation(({ levellogId }: { levellogId: string }) => {
    return requestGetLevellog({
      accessToken,
      teamId,
      levellogId,
    });
  });

  const onClickOpenLevellogModal = ({ participant }: LevellogParticipantType) => {
    onClickOpenModal();
    setLevellogParticipant(participant);
    getLevellog({ levellogId: participant.levellogId });
  };

  return {
    levellogModalLoading,
    levellogParticipant,
    isLevellogModalOpen: isModalOpen,
    levellogInfo,
    onClickOpenLevellogModal,
    handleClickCloseLevellogModal: onClickCloseModal,
  };
};

export default useLevellogModal;
