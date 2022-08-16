import { useEffect } from 'react';
import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useTeams from 'hooks/useTeams';
import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FilterButton from 'components/@commons/FilterButton';
import InterviewTeam from 'components/teams/InterviewTeam';
import { InterviewTeamType } from 'types/team';

const InterviewTeams = () => {
  const { teams, getTeams, handleClickInterviewGroup } = useTeams();

  useEffect(() => {
    getTeams();
  }, []);

  return (
    <>
      <ContentHeader title={'인터뷰 팀'}>
        <div>
          <FilterButton isActive={true}>진행중인 인터뷰</FilterButton>
          <FilterButton>종료된 인터뷰</FilterButton>
          <FilterButton>나의 인터뷰</FilterButton>
        </div>
        <span />
      </ContentHeader>
      <S.Container onClick={handleClickInterviewGroup}>
        {teams.map((team: InterviewTeamType) => (
          <InterviewTeam key={team.id} {...team} />
        ))}
      </S.Container>
    </>
  );
};

const S = {
  Container: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 2.5rem;
    @media (max-width: 560px) {
      justify-content: center;
    }
  `,
};

export default InterviewTeams;
