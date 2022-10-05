import { useEffect } from 'react';

import useTeamPage from 'hooks/team/useTeamPage';

import TeamForm from 'components/teams/TeamForm';

const InterviewTeamEdit = () => {
  const {
    state: {
      participantNicknameValue,
      watcherNicknameValue,
      watcherMembers,
      participantMembers,
      watchers,
      participants,
    },
    ref: { teamInfoRef },
    handle: {
      setParticipantNicknameValue,
      setWatcherNicknameValue,
      getTeamOnRef,
      updateMembers,
      updateWatchers,
      addToParticipants,
      addToWatcherParticipants,
      removeToParticipants,
      remoteToWatcherParticipants,
      handleChangeParticipantInput,
      handleChangeWatcherInput,
      handleClickTeamEditButton,
    },
  } = useTeamPage();

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
      getTeamOnRef={getTeamOnRef}
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
