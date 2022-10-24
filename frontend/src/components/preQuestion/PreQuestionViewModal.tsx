import { Link } from 'react-router-dom';

import styled from 'styled-components';

import Button from 'components/@commons/button/Button';
import Modal from 'components/@commons/modal/Modal';
import ModalPortal from 'portal/ModalPortal';
import { PreQuestionCustomHookType, PreQuestionFormatType } from 'types/preQuestion';
import { ParticipantType } from 'types/team';
import { preQuestionEditUriBuilder } from 'utils/util';

const PreQuestionViewModal = ({
  teamId,
  preQuestion,
  participant,
  getTeam,
  onClickDeletePreQuestion,
  handleClickClosePreQuestionModal,
}: PreQuestionViewModalProps) => {
  const { levellogId, preQuestionId } = participant;

  const handleClickDeletePreQuestion = async () => {
    if (levellogId && preQuestionId) {
      await onClickDeletePreQuestion({ levellogId, preQuestionId });
      await getTeam();
    }
  };

  return (
    <ModalPortal>
      <S.Dimmer id="dimmer" onClick={handleClickClosePreQuestionModal} />
      <Modal
        modalContent={preQuestion}
        contentName={'사전질문'}
        handleClickCloseButton={handleClickClosePreQuestionModal}
      >
        <Link
          to={preQuestionEditUriBuilder({
            teamId,
            levellogId,
            preQuestionId,
            authorId: preQuestion?.author.id,
          })}
        >
          <Button>수정하기</Button>
        </Link>
        <S.RightButton onClick={handleClickDeletePreQuestion}>삭제하기</S.RightButton>
      </Modal>
    </ModalPortal>
  );
};

interface PreQuestionViewModalProps {
  teamId: string | undefined;
  preQuestion: PreQuestionFormatType | undefined;
  participant: ParticipantType;
  getTeam: () => void;
  onClickDeletePreQuestion: ({
    levellogId,
    preQuestionId,
  }: Pick<PreQuestionCustomHookType, 'levellogId' | 'preQuestionId'>) => void;
  handleClickClosePreQuestionModal: (e: React.MouseEvent<HTMLElement>) => void;
}

const S = {
  Dimmer: styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 20;
    background-color: ${(props) => props.theme.new_default.DIMMER_BLACK};
  `,

  RightButton: styled(Button)`
    margin-left: 1rem;
  `,
};

export default PreQuestionViewModal;
