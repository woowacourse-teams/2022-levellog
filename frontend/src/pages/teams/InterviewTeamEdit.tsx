import useTeamEdit from 'hooks/team/useTeamEdit';

import TeamForm from 'components/teams/TeamForm';

const InterviewTeamEdit = () => {
  const { teamInfoRef, handleClickTeamEditButton } = useTeamEdit();

  return (
    <TeamForm
      purpose={'수정하기'}
      teamInfoRef={teamInfoRef}
      handleClickTeamButton={handleClickTeamEditButton}
    />
  );
};

export default InterviewTeamEdit;
