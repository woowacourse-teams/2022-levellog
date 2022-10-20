import { useState } from 'react';

import { useMutation } from '@tanstack/react-query';

import useModal from 'hooks/useModal';
import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { requestDeletePreQuestion, requestGetPreQuestion } from 'apis/preQuestion';
import { PreQuestionCustomHookType, PreQuestionParticipantType } from 'types/preQuestion';
import { ParticipantType } from 'types/team';

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
    ({
      levellogId,
      preQuestionId,
    }: Pick<PreQuestionCustomHookType, 'levellogId' | 'preQuestionId'>) => {
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

  const onClickOpenPreQuestionModal = ({ participant }: PreQuestionParticipantType) => {
    onClickOpenModal();
    setPreQuestionParticipant(participant);
    getPreQuestion({ levellogId: participant.levellogId });
  };

  const onClickDeletePreQuestion = async ({
    levellogId,
    preQuestionId,
  }: Pick<PreQuestionCustomHookType, 'levellogId' | 'preQuestionId'>) => {
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
