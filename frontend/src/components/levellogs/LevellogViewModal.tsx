import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import { TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/button/Button';
import Modal from 'components/@commons/modal/Modal';
import ModalPortal from 'portal/ModalPortal';
import { LevellogInfoType } from 'types/levellog';
import { ParticipantType, TeamStatusType } from 'types/team';
import {
  levellogEditUriBuilder,
  preQuestionEditUriBuilder,
  preQuestionAddUriBuilder,
} from 'utils/util';

const LevellogViewModal = ({
  teamId,
  participant,
  levellogInfo,
  userInTeam,
  teamStatus,
  handleClickCloseLevellogModal,
}: LevellogViewModalProps) => {
  const { loginUserId } = useUser();

  const { levellogId, preQuestionId } = participant;

  if (levellogInfo?.author.id === loginUserId) {
    return (
      <ModalPortal>
        <S.Dimmer onClick={handleClickCloseLevellogModal} />
        <Modal
          modalContent={levellogInfo}
          contentName={'레벨로그'}
          handleClickCloseButton={handleClickCloseLevellogModal}
        >
          <>
            {teamStatus === TEAM_STATUS.READY && (
              <Link
                to={levellogEditUriBuilder({
                  teamId,
                  levellogId,
                  authorId: levellogInfo.author.id,
                })}
              >
                <Button>수정하기</Button>
              </Link>
            )}
          </>
        </Modal>
      </ModalPortal>
    );
  }

  return (
    <ModalPortal>
      <S.Dimmer onClick={handleClickCloseLevellogModal} />
      <Modal
        modalContent={levellogInfo}
        contentName={'레벨로그'}
        handleClickCloseButton={handleClickCloseLevellogModal}
      >
        <>
          {loginUserId &&
            userInTeam &&
            (participant.preQuestionId ? (
              <Link
                to={preQuestionEditUriBuilder({
                  teamId,
                  levellogId,
                  preQuestionId,
                  authorId: loginUserId,
                })}
              >
                <Button>사전질문 수정</Button>
              </Link>
            ) : (
              <Link to={preQuestionAddUriBuilder({ teamId, levellogId })}>
                <Button>사전질문 작성</Button>
              </Link>
            ))}
        </>
      </Modal>
    </ModalPortal>
  );
};

interface LevellogViewModalProps {
  teamId: string | undefined;
  participant: ParticipantType;
  levellogInfo: LevellogInfoType | undefined;
  userInTeam: Boolean;
  teamStatus: TeamStatusType;
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
};

export default LevellogViewModal;
