import { useContext, useEffect, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';

import styled, { CSSProperties } from 'styled-components';
import { InterviewTeamType, ParticipantType } from 'types';

import useTeams from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import Interviewer from 'components/teams/Interviewer';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';

const InterviewDetail = () => {
  const { teamId } = useParams();
  const location = useLocation();
  const team = useContext(TeamContext);
  const teamInfoDispatch = useContext(TeamDispatchContext);
  const { teamLookup } = useTeams();

  const requestInterviewTeam = async () => {
    const res = await teamLookup(teamId);
    teamInfoDispatch(res.data);
  };

  useEffect(() => {
    const interviewTeam = location.state as InterviewTeamType;
    if (interviewTeam) {
      teamInfoDispatch(interviewTeam);

      return;
    }

    requestInterviewTeam();
  }, []);

  if (Object.keys(team).length === 0) return;

  return (
    <FlexBox gap={4.375}>
      <InterviewDetailHeader>
        <FlexBox gap={1}>
          <InterviewDetailOwnerImage>
            <Image src={team.teamImage} sizes={'LARGE'} />
          </InterviewDetailOwnerImage>
          <FlexBox flexFlow="column" gap={1.125}>
            <InterviewDetailTitle>{team.title}</InterviewDetailTitle>
            <FlexBox gap={1}>
              <InterviewDetailTitleContent>{team.place}</InterviewDetailTitleContent>
              <InterviewDetailTitleContent>{team.startAt}</InterviewDetailTitleContent>
            </FlexBox>
          </FlexBox>
        </FlexBox>
        <Button>그룹 수정하기</Button>
      </InterviewDetailHeader>
      <InterviewDetailContainer>
        {team.participants.map((participant: ParticipantType) => (
          <Interviewer key={participant.id} {...participant} />
        ))}
      </InterviewDetailContainer>
    </FlexBox>
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
