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
import Header from 'components/header/Header';
import InterviewTeam from 'components/teams/InterviewTeam';
import { InterviewTeamType } from 'types/team';

const InterviewTeams = () => {
  const { teams, getTeams, handleClickInterviewGroup } = useTeams();

  useEffect(() => {
    getTeams();
  }, []);

  return (
    <S.GridContainer>
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
    @media (min-width: 775px) and (max-width: 1207.5px) {
      padding: 0 calc((100vw - 775px) / 2);
      padding-bottom: 200px;
    }
    @media (min-width: 560px) and (max-width: 800px) {
      padding: 0 calc((100vw - 362.5px) / 2);
      padding-bottom: 200px;
    }
    @media (max-width: 560px) {
      padding: 0 1.25rem;
      padding-bottom: 200px;
    }
  `,

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
