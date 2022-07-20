import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import styled from 'styled-components';
import { InterviewTeamType } from 'types';

import useTeams from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import InterviewTeam from 'components/teams/InterviewTeam';

const InterviewTeams = () => {
  const [teams, setTeams] = useState<InterviewTeamType[]>([]);
  const navigate = useNavigate();
  const { teamsLookup } = useTeams();

  const handleClickInterviewGroup = (e: React.MouseEvent<HTMLElement>) => {
    const target = e.target as HTMLElement;
    const team = teams.find((team) => +team.id === +target.id);

    navigate(`/interview/teams/${target.id}`, {
      state: team,
    });
  };

  const requestGroups = async () => {
    const res = await teamsLookup();
    setTeams(res);
  };

  useEffect(() => {
    requestGroups();
  }, []);

  return (
    <>
      <ContentHeader title={'Interview Group'}>
        <Button>추가하기</Button>
      </ContentHeader>
      <InterviewTeamsContainer onClick={handleClickInterviewGroup}>
        {teams.map((team: InterviewTeamType) => (
          <InterviewTeam key={team.id} {...team} />
        ))}
      </InterviewTeamsContainer>
    </>
  );
};

const InterviewTeamsContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
`;

export default InterviewTeams;
