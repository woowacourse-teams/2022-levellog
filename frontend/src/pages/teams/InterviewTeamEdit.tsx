import { useEffect } from 'react';

import useMember from 'hooks/useMember';
import { useTeam } from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import TeamAddForm from 'components/teams/TeamAddForm';
import TeamEditForm from 'components/teams/TeamEditForm';

const InterviewTeamEdit = () => {
  const { teamInfoRef, team, onSubmitTeamEditForm } = useTeam();

  const handleSubmitTeamEditForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(team);
    // onSubmitTeamEditForm({ participants });
  };

  return (
    <form onSubmit={handleSubmitTeamEditForm}>
      <ContentHeader title={'인터뷰 팀 생성하기'}>
        <Button type={'submit'}>수정하기</Button>
      </ContentHeader>
      <TeamEditForm teamInfoRef={teamInfoRef} />
    </form>
  );
};

export default InterviewTeamEdit;
