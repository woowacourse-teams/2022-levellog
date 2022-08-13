import { useEffect } from 'react';
import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useTeams from 'hooks/useTeams';

import { ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import InterviewTeam from 'components/teams/InterviewTeam';
import { InterviewTeamType } from 'types/team';

const InterviewTeams = () => {
  const { teams, getTeams, handleClickInterviewGroup } = useTeams();

  useEffect(() => {
    console.log(getTeams());
  }, []);

  console.log(teams);

  return (
    <>
      <ContentHeader title={'인터뷰 팀'}>
        <Link to={ROUTES_PATH.INTERVIEW_TEAMS_ADD}>
          <Button>추가하기</Button>
        </Link>
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
