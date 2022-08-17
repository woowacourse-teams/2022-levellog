import { useEffect } from 'react';
import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useTeams from 'hooks/useTeams';

import plus from 'assets/images/add.png';
import { ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FilterButton from 'components/@commons/FilterButton';
import Image from 'components/@commons/Image';
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
      <Link to={ROUTES_PATH.INTERVIEW_TEAMS_ADD}>
        <S.TeamAddButton>
          {'팀 추가하기'}
          <Image src={plus} sizes={'TINY'} margin={'0 0 1px 5px'} />
        </S.TeamAddButton>
      </Link>
    </>
  );
};

const S = {
  Container: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 50px;
    margin-top: 50px;
    @media (max-width: 560px) {
      justify-content: center;
      margin-top: 20px;
      gap: 40px;
    }
  `,

  TeamAddButton: styled(Button)`
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    bottom: 110px;
    left: 0;
    right: 0;
    z-index: 10;
    width: 130px;
    height: 50px;
    margin: 0 auto;
    border-radius: 32px;
    font-size: 16px;
  `,
};

export default InterviewTeams;
