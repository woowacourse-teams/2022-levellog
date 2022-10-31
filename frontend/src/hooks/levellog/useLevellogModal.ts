import { useState } from 'react';
import { useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';

import useModal from 'hooks/useModal';
import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { requestGetLevellog } from 'apis/levellog';
import { ParticipantType } from 'types/index';

const useLevellogModal = () => {
  const { showSnackbar } = useSnackbar();
  const { isModalOpen, onClickOpenModal, onClickCloseModal } = useModal();
  const [levellogParticipant, setLevellogParticipant] = useState({} as ParticipantType);
  const { teamId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const {
    isError: levellogModalError,
    mutate: getLevellog,
    data: levellogInfo,
  } = useMutation(
    ({ levellogId }: { levellogId: string }) => {
      return requestGetLevellog({
        accessToken,
        teamId,
        levellogId,
      });
    },
    {
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const onClickOpenLevellogModal = ({ participant }: Record<'participant', ParticipantType>) => {
    onClickOpenModal();
    setLevellogParticipant(participant);
    getLevellog({ levellogId: participant.levellogId });
  };

  return {
    levellogModalError,
    levellogParticipant,
    isLevellogModalOpen: isModalOpen,
    levellogInfo,
    onClickOpenLevellogModal,
    handleClickCloseLevellogModal: onClickCloseModal,
  };
};

export default useLevellogModal;
