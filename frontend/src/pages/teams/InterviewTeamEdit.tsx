import { useEffect } from 'react';

import useTeamEdit from 'hooks/team/useTeamEdit';

import TeamForm from 'components/teams/TeamForm';

const InterviewTeamEdit = () => {
  const {
    participantNicknameValue,
    watcherNicknameValue,
    watcherMembers,
    participantMembers,
    watchers,
    participants,
    teamInfoRef,
    setParticipantNicknameValue,
    setWatcherNicknameValue,
    updateMembers,
    updateWatchers,
    addToParticipants,
    addToWatcherParticipants,
    removeToParticipants,
    remoteToWatcherParticipants,
    handleChangeParticipantInput,
    handleChangeWatcherInput,
    handleClickTeamEditButton,
  } = useTeamEdit();

  useEffect(() => {
    updateMembers({ nicknameValue: participantNicknameValue });
  }, [participantNicknameValue]);

  useEffect(() => {
    updateWatchers({ nicknameValue: watcherNicknameValue });
  }, [watcherNicknameValue]);

  return (
    <TeamForm
      purpose={'수정하기'}
      participantNicknameValue={participantNicknameValue}
      watcherNicknameValue={watcherNicknameValue}
      participants={participants}
      participantMembers={participantMembers}
      watcherMembers={watcherMembers}
      watchers={watchers}
      teamInfoRef={teamInfoRef}
      setParticipantNicknameValue={setParticipantNicknameValue}
      setWatcherNicknameValue={setWatcherNicknameValue}
      addToParticipants={addToParticipants}
      addToWatcherParticipants={addToWatcherParticipants}
      removeToParticipants={removeToParticipants}
      remoteToWatcherParticipants={remoteToWatcherParticipants}
      handleChangeWatcherInput={handleChangeWatcherInput}
      handleChangeParticipantInput={handleChangeParticipantInput}
      handleClickTeamButton={handleClickTeamEditButton}
    />
  );
};

export default InterviewTeamEdit;
