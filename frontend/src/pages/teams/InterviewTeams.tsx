import { useEffect } from 'react';

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
      <S.Container onClick={handleClickInterviewGroup}>
        {teams.map((team: InterviewTeamType) => (
          <InterviewTeam key={team.id} {...team} />
        ))}
      </S.Container>
    </>
  );
};

const S = {
  Container: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 2.5rem;
    @media (max-width: 560px) {
      justify-content: center;
    }
  `,
};

export default InterviewTeams;
