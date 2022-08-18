import { useState } from 'react';

import usePreQuestion from 'hooks/usePreQuestion';

import { MESSAGE } from 'constants/constants';

import { PreQuestionCustomHookType, PreQuestionParticipantType } from 'types/preQuestion';
import { ParticipantType } from 'types/team';

const usePreQuestionModal = () => {
  const { preQuestion, getPreQuestion, deletePreQuestion } = usePreQuestion();
  const [isPreQuestionModalOpen, setIsPreQuestionModalOpen] = useState(false);
  const [preQuestionParticipant, setPreQuestionParticipant] = useState({} as ParticipantType);

  const onClickOpenPreQuestionModal = async ({ participant }: PreQuestionParticipantType) => {
    await getPreQuestion({ levellogId: participant.levellogId });
    setIsPreQuestionModalOpen(true);
    setPreQuestionParticipant(participant);
  };

  const onClickDeletePreQuestion = async ({
    levellogId,
    preQuestionId,
  }: Pick<PreQuestionCustomHookType, 'levellogId' | 'preQuestionId'>) => {
    await deletePreQuestion({
      levellogId,
      preQuestionId,
    });
    setIsPreQuestionModalOpen(false);
    alert(MESSAGE.PREQUESTION_DELETE_CONFIRM);
  };

  const handleClickClosePreQuestionModal = (e: React.MouseEvent<HTMLElement>) => {
    setIsPreQuestionModalOpen(false);
  };

  return {
    preQuestion,
    preQuestionParticipant,
    isPreQuestionModalOpen,
    onClickOpenPreQuestionModal,
    onClickDeletePreQuestion,
    handleClickClosePreQuestionModal,
  };
};

export default usePreQuestionModal;
