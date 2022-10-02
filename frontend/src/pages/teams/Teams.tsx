import styled from 'styled-components';

import useTeamsQuery from 'hooks/queries/useTeamsQuery';

import { useQuery } from '@tanstack/react-query';
import InterviewTeam from 'components/teams/InterviewTeam';
import { InterviewTeamType, TeamsConditionType } from 'types/team';

const Teams = ({ teamsCondition }: TeamsProps) => {
  const { getTeams } = useTeamsQuery();
  const { data, isError, isLoading } = useQuery(
    ['teams', teamsCondition],
    () => getTeams({ teamsCondition }),
    {
      suspense: true,
    },
  );

  if (isLoading) return <h3>Loading...</h3>;
  if (isError) return <h3>Error!</h3>;

  console.log(data);

  return (
    <S.Container>
      {data.teams.map((team: InterviewTeamType) => (
        <InterviewTeam key={team.id} team={team} />
      ))}
    </S.Container>
  );
};

interface TeamsProps {
  teamsCondition: TeamsConditionType;
}

const S = {
  Container: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 3.125rem;
    @media (max-width: 560px) {
      justify-content: center;
      gap: 2.5rem;
    }
  `,
};

export default Teams;
