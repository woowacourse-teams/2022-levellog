import { useEffect } from 'react';

import { useTeam } from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import TeamEditForm from 'components/teams/TeamEditForm';

const InterviewTeamEdit = () => {
  const { teamInfoRef, getTeamOnRef, team, onSubmitTeamEditForm } = useTeam();

  const handleSubmitTeamEditForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSubmitTeamEditForm();
  };

  useEffect(() => {
    getTeamOnRef();
  }, []);

  return (
    <form onSubmit={handleSubmitTeamEditForm}>
      <ContentHeader title={'인터뷰 팀 수정하기'}>
        <Button type={'submit'}>수정하기</Button>
      </ContentHeader>
      <TeamEditForm teamInfoRef={teamInfoRef} />
    </form>
  );
};

export default InterviewTeamEdit;
