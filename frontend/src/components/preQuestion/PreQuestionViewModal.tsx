import { useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';

import ModalPortal from 'ModalPortal';
import styled from 'styled-components';

import Button from 'components/@commons/Button';
import UiViewer from 'components/@commons/UiViewer';
import { PreQuestionCustomHookType } from 'types/preQuestion';
import { ParticipantType } from 'types/team';

const PreQuestionViewModal = ({
  preQuestion,
  participant,
  getTeam,
  onClickDeletePreQuestion,
  handleClickClosePreQuestionModal,
}: LevellogViewModalProps) => {
  const { memberId, levellogId, preQuestionId, nickname } = participant;
  const { teamId } = useParams();

  const handleClickDeleteLevellog = async () => {
    if (levellogId && preQuestionId) {
      await onClickDeletePreQuestion({ levellogId, preQuestionId });
      getTeam();
    }
  };

  return (
    <ModalPortal>
      <S.Dimmer id="dimmer" onClick={handleClickClosePreQuestionModal} />
      <S.Container>
        <S.Header>
          <S.Title>{nickname}의 사전질문</S.Title>
          <S.CloseButton id="closeButton" onClick={handleClickClosePreQuestionModal}>
            X
          </S.CloseButton>
        </S.Header>
        <S.Levellog>
          <UiViewer content={preQuestion} />
        </S.Levellog>
        <S.Footer>
          <Link
            to={`/pre-questions/teams/${teamId}/levellog/${levellogId}/pre-question/${preQuestionId}`}
          >
            <Button>수정하기</Button>
          </Link>
          <Button onClick={handleClickDeleteLevellog}>삭제하기</Button>
        </S.Footer>
      </S.Container>
    </ModalPortal>
  );
};

interface LevellogViewModalProps {
  preQuestion: string;
  participant: ParticipantType;
  getTeam: () => void;
  onClickDeletePreQuestion: ({
    levellogId,
    preQuestionId,
  }: Pick<PreQuestionCustomHookType, 'levellogId' | 'preQuestionId'>) => Promise<void>;
  handleClickClosePreQuestionModal: (e: React.MouseEvent<HTMLElement>) => void;
}

const S = {
  Dimmer: styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: ${(props) => props.theme.default.OPACITY_BLACK};
  `,

  Container: styled.div`
    position: fixed;
    top: 50%;
    left: 50%;
    padding: 0.875rem 1.875rem 1.875rem 1.875rem;
    border-radius: 0.5rem;
    background-color: ${(props) => props.theme.default.GRAY};
    transform: translate(-50%, -50%);
  `,

  Header: styled.div`
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: 3.625rem;
  `,

  Title: styled.h1`
    font-size: 1.875rem;
    @media (max-width: 35rem) {
      font-size: 1.25rem;
    }
    @media (max-height: 35rem) {
      font-size: 1.25rem;
    }
  `,

  CloseButton: styled.button`
    width: 1.125rem;
    height: 1.125rem;
    border-style: none;
    background-color: ${(props) => props.theme.default.GRAY};
    font-size: 1.375rem;
    font-weight: 800;
    cursor: pointer;
  `,

  Levellog: styled.div`
    overflow: auto;
    width: 42.5rem;
    height: 40.5rem;
    padding: 1rem;
    border-radius: 0.25rem;
    background-color: ${(props) => props.theme.default.WHITE};
    line-height: 1.875rem;
    word-spacing: 0.0625rem;
    @media (max-width: 51.875rem) {
      width: 31.25rem;
    }
    @media (max-height: 51.875rem) {
      height: 31.875rem;
    }
    @media (max-width: 35rem) {
      width: 16.25rem;
    }
    @media (max-height: 40rem) {
      height: 18.75rem;
    }
  `,

  Footer: styled.div`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    width: 100%;
    margin-top: 1.25rem;
  `,
};

export default PreQuestionViewModal;
