import { useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';

import ModalPortal from 'ModalPortal';
import styled from 'styled-components';

import useTeam from 'hooks/useTeam';
import useUriBuilder from 'hooks/useUriBuilder';
import useUser from 'hooks/useUser';

import closeIcon from 'assets/images/close.svg';
import { TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import UiViewer from 'components/@commons/UiViewer';
import { LevellogInfoType } from 'types/levellog';
import { ParticipantType } from 'types/team';

const LevellogViewModal = ({
  levellogInfo,
  participant,
  userInTeam,
  teamId,
  handleClickCloseLevellogModal,
}: LevellogViewModalProps) => {
  const { levellogId, preQuestionId } = participant;
  const { author, content } = levellogInfo;
  const { loginUserId } = useUser();
  const { team } = useTeam();
  const { levellogEditUriBuilder, preQuestionAddUriBuilder, preQuestionEditUriBuilder } =
    useUriBuilder();

  if (author.id === loginUserId) {
    return (
      <ModalPortal>
        <S.Dimmer onClick={handleClickCloseLevellogModal} />
        <S.Container>
          <S.Header>
            <FlexBox alignItems={'center'} gap={0.375}>
              <Image src={author.profileUrl} sizes={'MEDIUM'} />
              <S.AuthorText>{author.nickname}의 레벨로그</S.AuthorText>
            </FlexBox>
            <S.CloseButton onClick={handleClickCloseLevellogModal}>
              <Image src={closeIcon} sizes={'SMALL'} />
            </S.CloseButton>
          </S.Header>
          <S.Levellog>
            <UiViewer content={content} />
          </S.Levellog>
          <S.Footer>
            {team.status === TEAM_STATUS.READY && (
              <Link to={levellogEditUriBuilder({ teamId, levellogId, authorId: author.id })}>
                <Button>수정하기</Button>
              </Link>
            )}
          </S.Footer>
        </S.Container>
      </ModalPortal>
    );
  }

  return (
    <ModalPortal>
      <S.Dimmer onClick={handleClickCloseLevellogModal} />
      <S.Container>
        <S.Header>
          <FlexBox alignItems={'center'} gap={0.375}>
            <Image src={author.profileUrl} sizes={'MEDIUM'} />
            <S.AuthorText>{author.nickname}의 레벨로그</S.AuthorText>
          </FlexBox>
          <S.CloseButton onClick={handleClickCloseLevellogModal}>
            <Image src={closeIcon} sizes={'SMALL'} />
          </S.CloseButton>
        </S.Header>
        <S.Levellog>
          <UiViewer content={content} />
        </S.Levellog>
        <S.Footer>
          {loginUserId &&
            userInTeam &&
            (participant.preQuestionId ? (
              <Link
                to={preQuestionEditUriBuilder({
                  teamId,
                  levellogId,
                  preQuestionId,
                  authorId: author.id,
                })}
              >
                <Button>사전질문 수정</Button>
              </Link>
            ) : (
              <Link to={preQuestionAddUriBuilder({ teamId, levellogId })}>
                <Button>사전질문 작성</Button>
              </Link>
            ))}
        </S.Footer>
      </S.Container>
    </ModalPortal>
  );
};

interface LevellogViewModalProps {
  teamId: string;
  levellogInfo: LevellogInfoType;
  participant: ParticipantType;
  userInTeam: Boolean;
  handleClickCloseLevellogModal: (e: React.MouseEvent<HTMLElement>) => void;
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

export default LevellogViewModal;
