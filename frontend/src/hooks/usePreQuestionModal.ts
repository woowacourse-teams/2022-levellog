import { useState } from 'react';

import usePreQuestion from 'hooks/usePreQuestion';

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
    alert('사전질문 삭제가 완료되었습니다.');
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
