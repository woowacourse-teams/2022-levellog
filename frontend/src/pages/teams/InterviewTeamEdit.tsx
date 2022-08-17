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
    addToParticipants,
    removeToParticipants,
  } = useTeam();

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNicknameValue(e.target.value);
  };

  useEffect(() => {
    updateMembers({ nicknameValue });
  }, [nicknameValue, participants]);

  return (
    <TeamForm
      handleSubmitTeamForm={handleSubmitTeamEditForm}
      teamInfoRef={teamInfoRef}
      participants={participants}
      members={members}
      getTeamOnRef={getTeamOnRef}
      nicknameValue={nicknameValue}
      setNicknameValue={setNicknameValue}
      handleChangeInput={handleChangeInput}
      addToParticipants={addToParticipants}
      removeToParticipants={removeToParticipants}
    />
  );
};

export default InterviewTeamEdit;
