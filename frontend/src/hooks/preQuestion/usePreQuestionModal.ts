import { useState } from 'react';

import { useMutation } from '@tanstack/react-query';

import useModal from 'hooks/useModal';
import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import {
  PreQuestionDeleteRequestType,
  requestDeletePreQuestion,
  requestGetPreQuestion,
} from 'apis/preQuestion';
import { ParticipantType } from 'types/index';

const usePreQuestionModal = () => {
  const { isModalOpen, onClickOpenModal, onClickCloseModal } = useModal();
  const { showSnackbar } = useSnackbar();
  const [preQuestionParticipant, setPreQuestionParticipant] = useState({} as ParticipantType);

  const accessToken = localStorage.getItem('accessToken');

  const {
    isLoading: preQuestionModalLoading,
    mutate: getPreQuestion,
    data: preQuestion,
  } = useMutation(({ levellogId }: { levellogId: string }) => {
    return requestGetPreQuestion({
      accessToken,
      levellogId,
    });
  });

  const { mutateAsync: deletePreQuestion } = useMutation(
    ({ levellogId, preQuestionId }: Omit<PreQuestionDeleteRequestType, 'accessToken'>) => {
      return requestDeletePreQuestion({
        accessToken,
        levellogId,
        preQuestionId,
      });
    },
    {
      onSuccess: () => {
        onClickCloseModal();
        showSnackbar({ message: MESSAGE.PREQUESTION_DELETE });
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const onClickOpenPreQuestionModal = ({ participant }: Record<'participant', ParticipantType>) => {
    onClickOpenModal();
    setPreQuestionParticipant(participant);
    getPreQuestion({ levellogId: participant.levellogId });
  };

  const onClickDeletePreQuestion = async ({
    levellogId,
    preQuestionId,
  }: Omit<PreQuestionDeleteRequestType, 'accessToken'>) => {
    await deletePreQuestion({
      levellogId,
      preQuestionId,
    });
  };

  return {
    preQuestionModalLoading,
    preQuestion,
    preQuestionParticipant,
    isPreQuestionModalOpen: isModalOpen,
    onClickOpenPreQuestionModal,
    onClickDeletePreQuestion,
    handleClickClosePreQuestionModal: onClickCloseModal,
  };
};

export default usePreQuestionModal;
