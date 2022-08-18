import { useEffect } from 'react';

import useTeam from 'hooks/useTeam';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import TeamForm from 'components/teams/TeamForm';

const InterviewTeamAdd = () => {
  const {
    teamInfoRef,
    onSubmitTeamAddForm,
    members,
    nicknameValue,
    setNicknameValue,
    participants,
    updateMembers,
    addToParticipants,
    removeToParticipants,
  } = useTeam();

  const handleSubmitTeamAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSubmitTeamAddForm({ participants });
  };

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNicknameValue(e.target.value);
  };

  useEffect(() => {
    updateMembers({ nicknameValue });
  }, [nicknameValue, participants]);

  return (
    <form onSubmit={handleSubmitTeamAddForm}>
      <ContentHeader title={'인터뷰 팀 생성하기'}>
        <Button type={'submit'}>만들기</Button>
      </ContentHeader>
      <TeamForm
        teamInfoRef={teamInfoRef}
        participants={participants}
        members={members}
        nicknameValue={nicknameValue}
        setNicknameValue={setNicknameValue}
        handleChangeInput={handleChangeInput}
        addToParticipants={addToParticipants}
        removeToParticipants={removeToParticipants}
      />
    </form>
  );
};

export default InterviewTeamAdd;
