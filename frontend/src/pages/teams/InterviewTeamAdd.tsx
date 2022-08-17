import React from 'react';
import { useEffect } from 'react';

import useTeam from 'hooks/useTeam';

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
    <TeamForm
      handleSubmitTeamForm={handleSubmitTeamAddForm}
      teamInfoRef={teamInfoRef}
      participants={participants}
      members={members}
      nicknameValue={nicknameValue}
      setNicknameValue={setNicknameValue}
      handleChangeInput={handleChangeInput}
      addToParticipants={addToParticipants}
      removeToParticipants={removeToParticipants}
    />
  );
};

export default InterviewTeamAdd;
