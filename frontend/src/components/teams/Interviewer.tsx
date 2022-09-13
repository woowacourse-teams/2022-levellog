import { useParams } from 'react-router-dom';

import styled from 'styled-components';

import useUser from 'hooks/useUser';

import feedbackIcon from 'assets/images/feedbackIcon.svg';
import interviewQuestionIcon from 'assets/images/interviewQuestionIcon.svg';
import levellogIcon from 'assets/images/levellogIcon.svg';
import preQuestionIcon from 'assets/images/preQuestionIcon.svg';
import { GITHUB_AVATAR_SIZE_LIST, TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import CustomLink from 'components/@commons/CustomLink';
import Image from 'components/@commons/Image';
import Role from 'components/@commons/Role';
import VisibleButtonList from 'components/VisibleButtonList';
import { LevellogParticipantType } from 'types/levellog';
import { PreQuestionParticipantType } from 'types/preQuestion';
import { ParticipantType, TeamStatusType } from 'types/team';
import {
  feedbacksGetUriBuilder,
  interviewQuestionsGetUriBuilder,
  levellogAddUriBuilder,
  preQuestionAddUriBuilder,
} from 'utils/util';

const Interviewer = ({
  participant,
  interviewees,
  interviewers,
  teamStatus,
  userInTeam,
  onClickOpenLevellogModal,
  onClickOpenPreQuestionModal,
}: InterviewerProps) => {
  const { loginUserId } = useUser();
  const { teamId } = useParams();

  const role = {
    interviewee: false,
    interviewer: false,
  };

  if (loginUserId) {
    role.interviewee = interviewees.includes(Number(participant.memberId));
    role.interviewer = interviewers.includes(Number(participant.memberId));
  }

  if (!teamId) return <S.Container />;

  const handleClickOpenLevellogModal = () => {
    if (typeof teamId === 'string') {
      onClickOpenLevellogModal({ teamId, participant });
    }
  };

  const handleClickOpenPreQuestionModal = () => {
    onClickOpenPreQuestionModal({ participant });
  };

  return (
    <S.Container>
      {role.interviewee && role.interviewer === false && <Role role={'인터뷰이'} />}
      {role.interviewer && role.interviewee === false && <Role role={'인터뷰어'} />}
      {role.interviewee && role.interviewer && <Role role={'상호 인터뷰'} />}
      <S.Profile>
        <Image
          src={participant.profileUrl}
          sizes={'HUGE'}
          githubAvatarSize={GITHUB_AVATAR_SIZE_LIST.HUGE}
        />
        <S.NicknameBox>
          <S.Nickname>{participant.nickname}</S.Nickname>
        </S.NicknameBox>
      </S.Profile>
      <S.Content>
        <S.ButtonBox>
          <VisibleButtonList
            type={'levellogLook'}
            interviewerId={participant.memberId}
            loginUserId={loginUserId}
            levellogId={participant.levellogId}
          >
            <S.Button disabled={!participant.levellogId} onClick={handleClickOpenLevellogModal}>
              <Image src={levellogIcon} sizes={'SMALL'} borderRadius={false} />
              <S.ButtonText>레벨로그 보기</S.ButtonText>
            </S.Button>
          </VisibleButtonList>

          <VisibleButtonList
            type={'levellogWrite'}
            interviewerId={participant.memberId}
            loginUserId={loginUserId}
            levellogId={participant.levellogId}
          >
            <CustomLink
              path={levellogAddUriBuilder({ teamId })}
              disabled={teamStatus !== TEAM_STATUS.READY}
            >
              <S.Button disabled={teamStatus !== TEAM_STATUS.READY}>
                <Image src={levellogIcon} sizes={'SMALL'} borderRadius={false} />
                <S.ButtonText>레벨로그 작성</S.ButtonText>
              </S.Button>
            </CustomLink>
          </VisibleButtonList>

          <VisibleButtonList
            type={'interviewQuestionLook'}
            interviewerId={participant.memberId}
            loginUserId={loginUserId}
          >
            <CustomLink
              path={interviewQuestionsGetUriBuilder({ teamId, levellogId: participant.levellogId })}
              disabled={!participant.levellogId || !userInTeam}
            >
              <S.Button disabled={!participant.levellogId || !userInTeam}>
                <Image src={interviewQuestionIcon} sizes={'SMALL'} borderRadius={false} />
                <S.ButtonText>인터뷰질문 보기</S.ButtonText>
              </S.Button>
            </CustomLink>
          </VisibleButtonList>

          <VisibleButtonList
            type={'preQuestionLook'}
            interviewerId={participant.memberId}
            loginUserId={loginUserId}
            preQuestionId={participant.preQuestionId}
          >
            <S.Button
              disabled={!participant.levellogId || !userInTeam}
              onClick={handleClickOpenPreQuestionModal}
            >
              <Image src={preQuestionIcon} sizes={'SMALL'} borderRadius={false} />
              <S.ButtonText>사전질문 보기</S.ButtonText>
            </S.Button>
          </VisibleButtonList>

          <VisibleButtonList
            type={'preQuestionWrite'}
            interviewerId={participant.memberId}
            loginUserId={loginUserId}
            preQuestionId={participant.preQuestionId}
          >
            <CustomLink
              path={preQuestionAddUriBuilder({ teamId, levellogId: participant.levellogId })}
              disabled={!participant.levellogId || !userInTeam}
            >
              <S.Button disabled={!participant.levellogId || !userInTeam}>
                <Image src={preQuestionIcon} sizes={'SMALL'} borderRadius={false} />
                <S.ButtonText>사전질문 작성</S.ButtonText>
              </S.Button>
            </CustomLink>
          </VisibleButtonList>

          <CustomLink
            path={feedbacksGetUriBuilder({ teamId, levellogId: participant.levellogId })}
            disabled={!participant.levellogId || !userInTeam}
          >
            <S.Button disabled={!participant.levellogId || !userInTeam}>
              <Image src={feedbackIcon} sizes={'SMALL'} borderRadius={false} />
              <S.ButtonText>피드백 작성 / 보기</S.ButtonText>
            </S.Button>
          </CustomLink>
        </S.ButtonBox>
      </S.Content>
    </S.Container>
  );
};

interface InterviewerProps {
  participant: ParticipantType;
  interviewees: (number | null)[];
  interviewers: (number | null)[];
  teamStatus: TeamStatusType;
  userInTeam: Boolean;
  onClickOpenLevellogModal: ({ teamId, participant }: LevellogParticipantType) => void;
  onClickOpenPreQuestionModal: ({ participant }: PreQuestionParticipantType) => void;
}

const S = {
  Container: styled.div`
    position: relative;
    width: 17.5rem;
    height: 24rem;
    padding: 1.25rem 2.125rem 0 2.125rem;
    border-radius: 0.625rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    box-shadow: 0.0625rem 0.25rem 0.625rem ${(props) => props.theme.new_default.GRAY};
  `,

  Profile: styled.div`
    position: relative;
    width: 7.5rem;
    height: 8.125rem;
    margin: 0 auto;
  `,

  NicknameBox: styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 6.875rem;
    left: -2.875rem;
    width: 13.25rem;
    height: 2.5rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.GRAY};
    background-color: ${(props) => props.theme.new_default.WHITE};
  `,

  IconImage: styled(Image)`
    border-radius: 0;
  `,

  Nickname: styled.p`
    font-weight: 600;
  `,

  Content: styled.div`
    display: flex;
    flex-direction: column;
    gap: 1.125rem;
  `,

  ButtonBox: styled.div`
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin-top: 2.375rem;
  `,

  Button: styled(Button)`
    display: flex;
    align-items: center;
    gap: 1rem;
    padding: 0.625rem 0.75rem;
    border-radius: 2rem;
    background-color: ${(props) => props.theme.default.INVISIBLE};
    font-size: 1.125rem;
    font-weight: 600;
    color: ${(props) =>
      props.disabled ? props.theme.new_default.GRAY : props.theme.new_default.BLACK};
    :hover {
      box-shadow: 0.25rem 0.25rem 0.25rem ${(props) => props.theme.new_default.GRAY};
    }
  `,

  ButtonText: styled.p`
    font-size: 1.25rem;
  `,
};

export default Interviewer;
