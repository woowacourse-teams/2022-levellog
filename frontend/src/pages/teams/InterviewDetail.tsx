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
  const { teamLocationState, team, getTeam } = useTeam();

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
        <InterviewDetailHeader>
          <FlexBox gap={1}>
            <InterviewDetailOwnerImage>
              <Image src={(team as InterviewTeamType).teamImage} sizes={'LARGE'} />
            </InterviewDetailOwnerImage>
            <FlexBox flexFlow="column" gap={1.125}>
              <InterviewDetailTitle>{(team as InterviewTeamType).title}</InterviewDetailTitle>
              <FlexBox gap={1}>
                <InterviewDetailTitleContent>
                  {(team as InterviewTeamType).place}
                </InterviewDetailTitleContent>
                <InterviewDetailTitleContent>
                  {(team as InterviewTeamType).startAt}
                </InterviewDetailTitleContent>
              </FlexBox>
            </FlexBox>
          </FlexBox>
          <Button>그룹 수정하기</Button>
        </InterviewDetailHeader>
        <InterviewDetailContainer>
          {(team as InterviewTeamType).participants.map((participant: ParticipantType) => (
            <Interviewer
              key={participant.id}
              participant={participant}
              onClickToggleModal={onClickToggleModal}
            />
          ))}
        </InterviewDetailContainer>
      </FlexBox>
    </>
  );
};

const InterviewDetailHeader = styled.div<CSSProperties>`
  display: flex;
  width: 100%;
  height: 60px;
  margin-top: 50px;
  justify-content: space-between;
  align-items: center;
`;

const InterviewDetailTitle = styled.h3`
  width: 11.5rem;
  word-break: break-all;
`;

const InterviewDetailTitleContent = styled.p`
  word-break: break-all;
`;

const InterviewDetailOwnerImage = styled.div`
  @media (max-width: 620px) {
    display: none;
  }
`;

const InterviewDetailContainer = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 40px;
  @media (max-width: 560px) {
    justify-content: center;
  }
`;

export default InterviewDetail;
