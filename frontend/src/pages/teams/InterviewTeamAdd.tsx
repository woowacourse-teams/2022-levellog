import React from 'react';
import { useEffect } from 'react';

import styled from 'styled-components';

import useTeam from 'hooks/useTeam';

import Header from 'components/header/Header';
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
    <S.GridContainer>
      <Header />
      {/* <TeamForm
        handleSubmitTeamForm={handleSubmitTeamAddForm}
        teamInfoRef={teamInfoRef}
        participants={participants}
        members={members}
        nicknameValue={nicknameValue}
        setNicknameValue={setNicknameValue}
        handleChangeInput={handleChangeInput}
        addToParticipants={addToParticipants}
        removeToParticipants={removeToParticipants}
      /> */}
    </S.GridContainer>
  );
};

const S = {
  GridContainer: styled.main`
    overflow: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    @media (min-width: 1620px) {
      padding: 0 calc((100vw - 1600px) / 2);
      padding-bottom: 200px;
    }
    @media (min-width: 1187.5px) and (max-width: 1620px) {
      padding: 0 calc((100vw - 1187.5px) / 2);
      padding-bottom: 200px;
    }
    /* @media (min-width: 775px) and (max-width: 1207.5px) {
      padding: 0 calc((100vw - 775px) / 2);
      padding-bottom: 200px;
    }
    @media (min-width: 560px) and (max-width: 800px) {
      padding: 0 calc((100vw - 362.5px) / 2);
      padding-bottom: 200px;
    } */
    @media (max-width: 560px) {
      padding: 0 1.25rem;
      padding-bottom: 200px;
    }
  `,
};

export default InterviewTeamAdd;
