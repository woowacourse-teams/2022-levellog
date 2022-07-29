import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import styled from 'styled-components';

import { useTeams } from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import InterviewTeam from 'components/teams/InterviewTeam';
import { InterviewTeamType } from 'types/team';

const InterviewTeams = () => {
  const { teams, getTeams, handleClickInterviewGroup } = useTeams();

  useEffect(() => {
    getTeams();
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
  @media (max-width: 560px) {
    justify-content: center;
  }
`;

export default InterviewTeams;
