import styled from 'styled-components';

import useTeams from 'hooks/team/useTeams';

import EmptyTeams from 'pages/status/EmptyTeams';

import InterviewTeam from 'components/teams/InterviewTeam';
import { InterviewTeamType, TeamConditionsType } from 'types/team';

const Teams = ({ teamsCondition }: TeamsProps) => {
  const { teams } = useTeams(teamsCondition);

  return (
    <S.Container>
      {teams?.map((team: InterviewTeamType) => (
        <InterviewTeam key={team.id} team={team} />
      ))}
      {teams?.length === 0 && <EmptyTeams />}
    </S.Container>
  );
};

interface TeamsProps {
  teamsCondition: TeamConditionsType;
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
