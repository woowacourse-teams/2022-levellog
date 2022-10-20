import styled from 'styled-components';

import useTeamAdd from 'hooks/team/useTeamAdd';

import TeamForm from 'components/teams/TeamForm';

const InterviewTeamAdd = () => {
  const { teamInfoRef, handleClickTeamAddButton } = useTeamAdd();

  return (
    <S.Container>
      <TeamForm
        purpose={'추가하기'}
        teamInfoRef={teamInfoRef}
        handleClickTeamButton={handleClickTeamAddButton}
      />
    </S.Container>
  );
};

const S = {
  Container: styled.main`
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
    @media (max-width: 560px) {
      padding: 0 1.25rem;
      padding-bottom: 200px;
    }
  `,
};

export default InterviewTeamAdd;
