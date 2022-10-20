import { Link } from 'react-router-dom';

import styled from 'styled-components';

import closeIcon from 'assets/images/close.svg';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import UiViewer from 'components/@commons/markdownEditor/UiViewer';
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
      <S.Container>
        <S.Header>
          <FlexBox alignItems={'center'} gap={0.375}>
            <Image src={preQuestion?.author.profileUrl} sizes={'MEDIUM'} />
            <S.AuthorText>{preQuestion?.author.nickname}의 사전질문</S.AuthorText>
          </FlexBox>
          <S.CloseButton id="closeButton" onClick={handleClickClosePreQuestionModal}>
            <Image src={closeIcon} sizes={'SMALL'} />
          </S.CloseButton>
        </S.Header>
        <S.PreQuestion>
          <UiViewer content={preQuestion?.content} />
        </S.PreQuestion>
        <S.Footer>
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
          <Button onClick={handleClickDeletePreQuestion}>삭제하기</Button>
        </S.Footer>
      </S.Container>
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

  Container: styled.div`
    position: fixed;
    top: 50%;
    left: 50%;
    max-width: 71rem;
    z-index: 30;
    border-radius: 0.875rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    transform: translate(-50%, -50%);
    @media (max-width: 520px) {
      width: calc(100% - 40px);
    }
  `,

  Header: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 0.375rem;
    padding: 0.875rem;
    width: 100%;
    border-bottom: 0.0625rem solid ${(props) => props.theme.new_default.GRAY};
  `,

  AuthorText: styled.p`
    font-size: 2rem;
    font-weight: 300;
    @media (max-width: 520px) {
      font-size: 16px;
    }
  `,

  CloseButton: styled.button`
    display: flex;
    align-items: center;
    width: 1.125rem;
    height: 1.125rem;
    margin-right: 0.875rem;
    border-style: none;
    background-color: ${(props) => props.theme.new_default.WHITE};
    font-size: 1.375rem;
    font-weight: 800;
    cursor: pointer;
  `,

  PreQuestion: styled.div`
    overflow: auto;
    width: 42.5rem;
    height: 40.5rem;
    padding: 1rem;
    border-radius: 0.25rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    line-height: 1.875rem;
    word-spacing: 0.0625rem;
    @media (max-width: 830px) {
      width: 31.25rem;
    }
    @media (max-height: 830px) {
      height: 31.875rem;
    }
    @media (max-width: 560px) {
      width: 100%;
      height: 18.75rem;
    }
  `,

  Footer: styled.div`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 1rem;
    width: 100%;
    border-top: 0.0625rem solid ${(props) => props.theme.new_default.GRAY};
    padding: 1rem 0.875rem 1.5rem 0;
  `,
};

export default PreQuestionViewModal;
