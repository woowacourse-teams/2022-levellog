import React from 'react';
import { useEffect } from 'react';

import useMember from 'hooks/useMember';
import { useTeam } from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import TeamAddForm from 'components/teams/TeamAddForm';

const InterviewTeamAdd = () => {
  const {
    members,
    nicknameValue,
    setNicknameValue,
    participants,
    updateMembers,
    updateParticipants,
  } = useMember();

  const { teamInfoRef, onSubmitTeamAddForm } = useTeam();

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
      <TeamAddForm
        teamInfoRef={teamInfoRef}
        participants={participants}
        members={members}
        nicknameValue={nicknameValue}
        setNicknameValue={setNicknameValue}
        handleChangeInput={handleChangeInput}
        updateParticipants={updateParticipants}
      />
    </form>
  );
};

export default InterviewTeamAdd;
