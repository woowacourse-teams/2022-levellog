import { useEffect, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';

import styled, { CSSProperties } from 'styled-components';
import { InterviewTeamType, ParticipantType } from 'types';

import useTeams from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import Interviewer from 'components/Interviewer';

const InterviewDetail = () => {
  const { teamId } = useParams();
  const location = useLocation();
  const [team, setTeam] = useState<{ [key: string]: any }>({});
  const { teamLookup } = useTeams();

  const requestInterviewTeam = async () => {
    const res = await teamLookup(teamId);
    setTeam(res.data);
  };

  useEffect(() => {
    const interviewTeam = location.state as InterviewTeamType;
    if (interviewTeam) {
      setTeam(interviewTeam);

      return;
    }

    requestInterviewTeam();
  }, []);

  if (Object.keys(team).length === 0) return;

  return (
    <FlexBox gap={4.375}>
      <InterviewTeamHeader>
        <FlexBox gap={1.125}>
          <Image src={team.teamImage} sizes={'LARGE'} />
          <FlexBox flexFlow="column" justifyContent="space-between">
            <h3>{team.title}</h3>
            <FlexBox gap={1}>
              <p>{team.place}</p>
              <p>{team.startAt}</p>
            </FlexBox>
          </FlexBox>
        </FlexBox>
        <Button>그룹 수정하기</Button>
      </InterviewTeamHeader>
      <InterviewTeamContent>
        {team.participants.map((participant: ParticipantType) => (
          <Interviewer key={participant.id} {...participant} />
        ))}
      </InterviewTeamContent>
    </FlexBox>
  );
};

const InterviewTeamHeader = styled.div<CSSProperties>`
  width: 100%;
  height: 60px;
  display: flex;
  margin-top: 50px;
  justify-content: space-between;
  align-items: center;
`;

const InterviewTeamContent = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
`;

export default InterviewDetail;
