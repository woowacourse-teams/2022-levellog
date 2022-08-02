import { useEffect } from 'react';

import styled, { CSSProperties } from 'styled-components';

import useLevellogModal from 'hooks/useLevellogModal';
import { useTeam } from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import LevellogViewModal from 'components/levellogs/LevellogViewModal';
import Interviewer from 'components/teams/Interviewer';
import { InterviewTeamType, ParticipantType } from 'types/team';

const InterviewDetail = () => {
  const { teamLocationState, team, userInTeam, getTeam } = useTeam();
  const {
    levellog,
    participant,
    isOnModal,
    onClickToggleModal,
    onClickDeleteLevellog,
    handleClickCloseLevellogModal,
  } = useLevellogModal();

  useEffect(() => {
    if (!teamLocationState) {
      getTeam();
    }
  }, []);

  if (team && Object.keys(team).length === 0) return <div>실패</div>;

  return (
    <>
      {isOnModal === true && (
        <LevellogViewModal
          levellog={levellog}
          participant={participant}
          getTeam={getTeam}
          onClickDeleteLevellog={onClickDeleteLevellog}
          handleClickCloseLevellogModal={handleClickCloseLevellogModal}
        />
      )}
      <FlexBox gap={4.375}>
        <S.Header>
          <FlexBox gap={1}>
            <S.OwnerImage>
              <Image src={(team as InterviewTeamType).teamImage} sizes={'LARGE'} />
            </S.OwnerImage>
            <FlexBox flexFlow="column" gap={1.125}>
              <S.Title>{(team as InterviewTeamType).title}</S.Title>
              <FlexBox gap={1}>
                <S.TitleContent>{(team as InterviewTeamType).place}</S.TitleContent>
                <S.TitleContent>{(team as InterviewTeamType).startAt}</S.TitleContent>
              </FlexBox>
            </FlexBox>
          </FlexBox>
          <Button>그룹 수정하기</Button>
        </S.Header>
        <S.Container>
          {(team as InterviewTeamType).participants.map((participant: ParticipantType) => (
            <Interviewer
              key={participant.memberId}
              participant={participant}
              userInTeam={userInTeam}
              onClickToggleModal={onClickToggleModal}
            />
          ))}
        </S.Container>
      </FlexBox>
    </>
  );
};

const S = {
  Header: styled.div<CSSProperties>`
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 3.75rem;
    margin-top: 3.125rem;
  `,

  Container: styled.div`
    display: flex;
    flex-flow: row wrap;
    gap: 2.5rem;
    @media (max-width: 560px) {
      justify-content: center;
    }
  `,

  Title: styled.h3`
    width: 11.5rem;
    word-break: break-all;
  `,

  TitleContent: styled.p`
    word-break: break-all;
  `,

  OwnerImage: styled.div`
    @media (max-width: 620px) {
      display: none;
    }
  `,
};

export default InterviewDetail;
