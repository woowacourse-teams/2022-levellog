import styled from 'styled-components';

import closeIcon from 'assets/images/close.svg';
import { GITHUB_AVATAR_SIZE_LIST } from 'constants/constants';

import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/image/Image';
import UiViewer from 'components/@commons/markdownEditor/UiViewer';

const Modal = ({ modalContent, contentName, children, handleClickCloseButton }: ModalProps) => {
  return (
    <S.Container>
      <S.Header>
        <FlexBox alignItems={'center'} gap={0.375}>
          <Image
            src={modalContent.author.profileUrl}
            sizes={'MEDIUM'}
            githubAvatarSize={GITHUB_AVATAR_SIZE_LIST.MEDIUM}
          />
          <S.AuthorText>
            {modalContent.author.nickname}의 {contentName}
          </S.AuthorText>
        </FlexBox>
        <S.CloseButton onClick={handleClickCloseButton}>
          <Image src={closeIcon} sizes={'SMALL'} />
        </S.CloseButton>
      </S.Header>
      <S.Levellog>
        <UiViewer content={modalContent.content} />
      </S.Levellog>
      <S.Footer>{children}</S.Footer>
    </S.Container>
  );
};

interface ModalProps {
  modalContent: {
    author: {
      id: string;
      nickname: string;
      profileUrl: string;
    };
    content: string;
  };
  contentName: string;
  children: JSX.Element;
  handleClickCloseButton: (e: React.MouseEvent<HTMLElement>) => void;
}

const S = {
  Container: styled.div`
    position: fixed;
    top: 50%;
    left: 50%;
    max-width: 71rem;
    z-index: 30;
    border-radius: 0.875rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    transform: translate(-50%, -50%);
    @media (max-width: 560px) {
      width: calc(100% - 2.5rem);
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

  AuthorText: styled.h2`
    font-size: 2rem;
    font-weight: 300;
    @media (max-width: 560px) {
      font-size: 1rem;
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

  Levellog: styled.div`
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
    width: 100%;
    border-top: 0.0625rem solid ${(props) => props.theme.new_default.GRAY};
    padding: 1rem 0.875rem 1.5rem 0;
  `,
};

export default Modal;
