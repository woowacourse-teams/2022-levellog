import { useEffect } from 'react';

import { useTeam } from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import TeamEditForm from 'components/teams/TeamEditForm';

const InterviewTeamEdit = () => {
  const {
    teamInfoRef,
    getTeamOnRef,
    onSubmitTeamEditForm,
    members,
    nicknameValue,
    setNicknameValue,
    participants,
    updateMembers,
    updateParticipants,
  } = useTeam();

  const handleSubmitTeamEditForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSubmitTeamEditForm();
  };

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNicknameValue(e.target.value);
  };

  useEffect(() => {
    updateMembers({ nicknameValue });
  }, [nicknameValue, participants]);

  return (
    <form onSubmit={handleSubmitTeamEditForm}>
      <ContentHeader title={'인터뷰 팀 수정하기'}>
        <Button type={'submit'}>수정하기</Button>
      </ContentHeader>
      <TeamEditForm
        teamInfoRef={teamInfoRef}
        participants={participants}
        members={members}
        getTeamOnRef={getTeamOnRef}
        nicknameValue={nicknameValue}
        setNicknameValue={setNicknameValue}
        handleChangeInput={handleChangeInput}
        updateParticipants={updateParticipants}
      />
    </form>
  );
};

export default InterviewTeamEdit;
