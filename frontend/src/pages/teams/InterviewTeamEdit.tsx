import { useEffect } from 'react';

import useTeam from 'hooks/useTeam';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import TeamForm from 'components/teams/TeamForm';

const InterviewTeamEdit = () => {
  const {
    teamInfoRef,
    getTeamOnRef,
    handleSubmitTeamEditForm,
    members,
    nicknameValue,
    setNicknameValue,
    participants,
    updateMembers,
    updateParticipants,
  } = useTeam();

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
      <TeamForm
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
